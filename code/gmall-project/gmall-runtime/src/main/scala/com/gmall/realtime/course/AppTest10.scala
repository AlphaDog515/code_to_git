package com.gmall.realtime.course

import java.util.Properties

import com.alibaba.fastjson.JSON
import com.gmall.common.Constant
import com.gmall.realtime.bean.{OrderDetail, OrderInfo, SaleDetail, UserInfo}
import com.gmall.realtime.util.{MyKafkaUtil, RedisUtil}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.json4s.jackson.Serialization
import redis.clients.jedis.Jedis

import scala.collection.JavaConversions._

object AppTest10 {


  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("test10").setMaster("local[3]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(2))

    val orderInfoDS: DStream[(String, OrderInfo)] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_ORDER_INFO)
      .map(line => {
        val json2Object = JSON.parseObject(line, classOf[OrderInfo])
        (json2Object.id, json2Object)
      })

    val orderDetailDS: DStream[(String, OrderDetail)] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_ORDER_DETAIL)
      .map(line => {
        val json2Object = JSON.parseObject(line, classOf[OrderDetail])
        (json2Object.order_id, json2Object)
      })

    val saleDetailDS: DStream[SaleDetail] = fullJoin(orderInfoDS, orderDetailDS)


    ssc.start()
    ssc.awaitTermination()

  }


  def joinUser(saleDetailDS: DStream[SaleDetail], ssc: StreamingContext) = {

    saleDetailDS.transform(saleRDD => {

      // 获取user Info
      val url = "jdbc:mysql://hadoop102:3306/gmall1015"
      val props = new Properties()
      props.setProperty("user", "root")
      props.put("password", "123456")


      val spark: SparkSession = SparkSession.builder().config(ssc.sparkContext.getConf).getOrCreate()
      import spark.implicits._
      val userDF: DataFrame = spark.read.jdbc(url, "user_info", props)
      val userDS: Dataset[UserInfo] = userDF.as[UserInfo]
      val userRDD: RDD[(String, UserInfo)] = userDS.rdd.map(info => (info.id, info))


      val saleDetailRDD: RDD[(String, SaleDetail)] = saleRDD.map(saleDetail => (saleDetail.user_id, saleDetail))
      val resRDD: RDD[(String, (SaleDetail, UserInfo))] = saleDetailRDD.join(userRDD)
      resRDD.map {
        case (_, (sale, info)) =>
          sale.mergeUserInfo(info)
      }
    })
  }

  def fullJoin(orderInfoDS: DStream[(String, OrderInfo)], orderDetailDS: DStream[(String, OrderDetail)]) = {
    orderInfoDS.fullOuterJoin(orderDetailDS).mapPartitions(it => {
      val client: Jedis = RedisUtil.getClient

      val res: Iterator[SaleDetail] = it.flatMap {
        case (orderId, (Some(orderInfo), opt)) =>
          cacheOrderInfo(client, orderInfo)
          val keys: List[String] = client.keys(s"order_detail:${orderId}:*").toList
          keys.map(key => {
            val orderDetailStr: String = client.get(key)
            client.del(key)
            val orderDetailObj: OrderDetail = JSON.parseObject(orderDetailStr, classOf[OrderDetail])
            SaleDetail().mergeOrderInfo(orderInfo).mergeOrderDetail(orderDetailObj)
          }) ::: (
            opt match {
              case Some(orderDetail) =>
                SaleDetail().mergeOrderInfo(orderInfo).mergeOrderDetail(orderDetail) :: Nil
              case None => Nil
            }
            )

        case (orderId, (None, Some(orderDetail))) =>
          val orderInfoStr = client.get("order_info:" + orderId)
          if (orderInfoStr != null && orderInfoStr.nonEmpty) {
            val orderInfo = JSON.parseObject(orderInfoStr, classOf[OrderInfo])
            SaleDetail().mergeOrderDetail(orderDetail).mergeOrderInfo(orderInfo) :: Nil

          } else {
            cacheOrderDetail(client, orderDetail)
            Nil
          }
      }

      client.close()
      res

    })
  }


  def saveToRedis(client: Jedis, key: String, value: AnyRef) = {
    implicit val df = org.json4s.DefaultFormats
    val value = Serialization.write(value)
    client.setex(key, 30 * 60, value)

  }

  def cacheOrderInfo(client: Jedis, orderInfo: OrderInfo) = {
    val key = s"order_info:${orderInfo.id}"
    saveToRedis(client, key, orderInfo)
  }

  def cacheOrderDetail(client: Jedis, orderDetail: OrderDetail) = {
    val key = s"order_detail:${orderDetail.order_id}:${orderDetail.id}"
    saveToRedis(client, key, orderDetail)
  }


}
