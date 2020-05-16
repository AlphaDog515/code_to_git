package com.gmall.realtime.app

import java.util.Properties

import com.alibaba.fastjson.JSON
import com.gmall.common.Constant
import com.gmall.realtime.bean.{OrderDetail, OrderInfo, SaleDetail, UserInfo}
import com.gmall.realtime.util.{MyKafkaUtil, RedisUtil}
import com.gmall.scala.util.ESUtil
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization
import redis.clients.jedis.Jedis

import scala.collection.JavaConversions._

object SaleDetailApp01 {

  def saveToRedis(client: Jedis, key: String, value: AnyRef) = {
    // 样例类不能用fastJSON
    val value = Serialization.write(value)(DefaultFormats)
    // client.set(key, value)
    client.setex(key, 60 * 30, value)

  }

  def cacheOrderInfo(client: Jedis, orderInfo: OrderInfo) = {
    val key: String = "order_info" + orderInfo.id
    saveToRedis(client, key, orderInfo)


  }

  def cacheOrderDetail(client: Jedis, orderDetail: OrderDetail) = {
    val key: String = s"order_datail:${orderDetail.order_id}:${orderDetail.id}"
    saveToRedis(client, key, OrderDetail)
  }

  def fullJoin(orderInfoDS: DStream[(String, OrderInfo)], orderDetailDS: DStream[(String, OrderDetail)]) = {
    val fullDS: DStream[(String, (Option[OrderInfo], Option[OrderDetail]))] = orderInfoDS.fullOuterJoin(orderDetailDS)

    val res: DStream[SaleDetail] = fullDS.mapPartitions(it => {

      //
      val client: Jedis = RedisUtil.getClient
      val res: Iterator[SaleDetail] = it.flatMap {


        case (orderId, (Some(orderInfo), opt)) =>
          // 不管opt是否是null
          cacheOrderInfo(client, orderInfo)

          val keys = client.keys(s"order_detail:${orderId}:*").toList //
          // 配对以后 有可能缓冲区里面已经有等待了
          keys.map(key => {
            val orderDetailStr: String = client.get(key)
            client.del(key) // 防止这个order detail重复join
            val orderDetail: OrderDetail = JSON.parseObject(orderDetailStr, classOf[OrderDetail])
            SaleDetail().mergeOrderInfo(orderInfo).mergeOrderDetail(orderDetail)

          }) :::
            (opt match {
              case Some(orderDetail) =>
                SaleDetail().mergeOrderInfo(orderInfo).mergeOrderDetail(orderDetail) :: Nil
              case None => Nil
            })


        // 封装宽表  写到redis


        case (orderId, (None, Some(orderDetail))) =>
          // 读取orderinfo信息
          val orderInfoStr: String = client.get("order_info" + orderId)

          // 读到 读不到
          if (orderInfoStr != null && orderInfoStr.nonEmpty) {
            val orderInfo: OrderInfo = JSON.parseObject(orderInfoStr, classOf[OrderInfo])
            SaleDetail().mergeOrderInfo(orderInfo).mergeOrderDetail(orderDetail) :: Nil

          } else {
            cacheOrderDetail(client, orderDetail)
            Nil

          }


      }
      client.close()

      res
    })
    res
  }


  def joinUser(saleDetailDS: DStream[SaleDetail], ssc: StreamingContext) = {

    val url = "jdbc:mysql://hadoop102:3306/gmall1015"
    val props = new Properties()
    props.put("user", "root")
    props.setProperty("password", "123456")

    val spark: SparkSession = SparkSession.builder().config(ssc.sparkContext.getConf).getOrCreate()

    import spark.implicits._
    saleDetailDS.transform(saleDetailRDD => {
      // 读取MySQL数据
      // map join
      val userDF: DataFrame = spark.read.jdbc(url, "user_info", props)
      val userDS: Dataset[UserInfo] = userDF.as[UserInfo]
      val userInfoRDD: RDD[(String, UserInfo)] = userDS.rdd.map(user => (user.id, user))

      val saleRDD: RDD[(String, SaleDetail)] = saleDetailRDD.map(saleDetail => (saleDetail.user_id, saleDetail))

      val res: RDD[SaleDetail] = saleRDD.join(userInfoRDD)
        .map {
          case (userId, (saleDetail, userInfo)) =>
            saleDetail.mergeUserInfo(userInfo)
        }

     res
    })
  }


  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("SaleDetailApp")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))

    val orderInfoDS: DStream[(String, OrderInfo)] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_ORDER_INFO)
      .map(s => {
        val orderInfo: OrderInfo = JSON.parseObject(s, classOf[OrderInfo])
        (orderInfo.id, orderInfo)
      })


    val orderDetailDS: DStream[(String, OrderDetail)] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_ORDER_DETAIL)
      .map(s => {
        val orderDetail: OrderDetail = JSON.parseObject(s, classOf[OrderDetail])
        (orderDetail.order_id, orderDetail)
      })

    // 双流join
    val saleDetailDS = fullJoin(orderInfoDS, orderDetailDS)


    // MySQL查询
    val saleDetailDstream: DStream[SaleDetail] = joinUser(saleDetailDS, ssc)
    /*saleDetailDstream.foreachRDD(rdd =>{
      ESUtil.insertBulk_02("sale_detail_1015",rdd.collect().toIterator)
    })*/


    ssc.start()
    ssc.awaitTermination()

  }


}
