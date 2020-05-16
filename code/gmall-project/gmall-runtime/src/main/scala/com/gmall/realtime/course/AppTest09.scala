package com.gmall.realtime.course

import com.alibaba.fastjson.JSON
import com.gmall.common.Constant
import com.gmall.realtime.bean.{OrderDetail, OrderInfo, SaleDetail}
import com.gmall.realtime.util.{MyKafkaUtil, RedisUtil}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.json4s.jackson.Serialization
import redis.clients.jedis.Jedis

import scala.collection.JavaConversions._

object AppTest09 {

  def saveToRedis(client: Jedis, key: String, value: AnyRef) = {

    implicit val df = org.json4s.DefaultFormats
    val value: String = Serialization.write(value)
    client.set(key, value)

  }


  def cacheOrderInfo(client: Jedis, orderInfo: OrderInfo) = {
    val key = "order_info:" + orderInfo.id
    saveToRedis(client, key, orderInfo)
  }


  def cacheOrderDetail(client: Jedis, orderDetail: OrderDetail) = {
    val key = s"order_detail:${orderDetail.order_id}:${orderDetail.id}"
    saveToRedis(client, key, orderDetail)
  }


  def fullJoin(orderInfoDS: DStream[(String, OrderInfo)], orderDetailDS: DStream[(String, OrderDetail)]) = {
    orderInfoDS.fullOuterJoin(orderDetailDS).mapPartitions(it =>{
      val client: Jedis = RedisUtil.getClient
      it.flatMap{
        case (orderId,(Some(orderInfo),Some(orderDetail))) =>
          cacheOrderInfo(client,orderInfo)
          val first: SaleDetail = SaleDetail().mergeOrderInfo(orderInfo).mergeOrderDetail(orderDetail)
          val list: List[String] = client.keys(s"order_detail:${orderId}:*").toList
          first::list.map(key =>{
            val orderStr: String = client.get(key)
            val orderDetail1: OrderDetail = JSON.parseObject(orderStr, classOf[OrderDetail])
            SaleDetail().mergeOrderDetail(orderDetail1).mergeOrderInfo(orderInfo)
          })





        case (orderId,(Some(orderInfo),None ))=>
          Nil



        case (orderId,(None,Some(orderDetail))) =>



        Nil
      }




      it
    })
  }

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("test09")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(2))

    val orderInfoDS: DStream[(String, OrderInfo)] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_ORDER_INFO).map(line => {
      val orderInfo: OrderInfo = JSON.parseObject(line, classOf[OrderInfo])
      (orderInfo.id, orderInfo)
    })


    val orderDetailDS: DStream[(String, OrderDetail)] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_ORDER_DETAIL).map(line => {
      val orderDetail = JSON.parseObject(line, classOf[OrderDetail])
      (orderDetail.order_id, orderDetail)
    })


    fullJoin(orderInfoDS,orderDetailDS)

    ssc.start()
    ssc.awaitTermination()
  }


}
