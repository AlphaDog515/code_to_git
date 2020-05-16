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

object AppTest08 {

  def saveToRedis(client: Jedis, key: String, value: AnyRef) = {
    implicit val df = org.json4s.DefaultFormats
    val value = Serialization.write(value)
    client.set(key, value)
  }

  def cacheOrderInfo(client: Jedis, orderInfo: OrderInfo) = {
    val key = "order_info:" + orderInfo.id
    saveToRedis(client, key, orderInfo)
  }

  def cacheOrderDetail(client: Jedis, orderDetail: OrderDetail) = {
    val key = s"order_info:${orderDetail.order_id}:${orderDetail.id}"
    saveToRedis(client, key, orderDetail)
  }


  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("AppTest08").setMaster("local[2]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))

    val orderInfoDS = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_ORDER_INFO).map(line => {
      val orderInfo: OrderInfo = JSON.parseObject(line, classOf[OrderInfo])
      (orderInfo.id, orderInfo)
    })


    val orderDetailDS = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_ORDER_DETAIL).map(line => {
      val orderDetail: OrderDetail = JSON.parseObject(line, classOf[OrderDetail])
      (orderDetail.order_id, orderDetail)
    })


    val saleDetailDS = fullJoin(orderInfoDS, orderDetailDS)
    saleDetailDS.print(1000)

    ssc.start()
    ssc.awaitTermination()

  }


  def fullJoin(orderInfoDS: DStream[(String, OrderInfo)], orderDetailDS: DStream[(String, OrderDetail)]) = {


    orderInfoDS.fullOuterJoin(orderDetailDS).mapPartitions(it => {
      val client: Jedis = RedisUtil.getClient
      val res: Iterator[SaleDetail] = it.flatMap {
        case (orderId, (Some(orderInfo), Some(orderDetail))) =>
          cacheOrderInfo(client, orderInfo)
          val first: SaleDetail = SaleDetail().mergeOrderDetail(orderDetail).mergeOrderInfo(orderInfo)

          val list: List[String] = client.keys(s"order_info:${orderId}:*").toList
          val second: List[SaleDetail] = list.map(key => {
            val orderDetailJson: String = client.get(key)
            val orderDetail1: OrderDetail = JSON.parseObject(orderDetailJson, classOf[OrderDetail])
            SaleDetail().mergeOrderInfo(orderInfo).mergeOrderDetail(orderDetail1)
          })
          first :: second


        case (orderId, (Some(orderInfo), None)) =>
          cacheOrderInfo(client, orderInfo)
          val list: List[String] = client.keys(s"order_info:${orderId}:*").toList
          val second: List[SaleDetail] = list.map(key => {
            val orderDetailJson: String = client.get(key)
            val orderDetail1: OrderDetail = JSON.parseObject(orderDetailJson, classOf[OrderDetail])
            SaleDetail().mergeOrderInfo(orderInfo).mergeOrderDetail(orderDetail1)
          })
          second


        case (orderId, (None, Some(orderDetail))) =>
          val orderInfoJson: String = client.get("order_info:" + orderId)
          if (orderInfoJson != null && orderInfoJson.nonEmpty) {
            val orderInfo: OrderInfo = JSON.parseObject(orderInfoJson, classOf[OrderInfo])
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

}

/*
分别消费Kafka中的数据，双流fullJoin，
orderDetail存在时，配对成功的时候不需要缓存，否则缓存
orderInfo存在时，不管是否配对成功都需要在缓存中查找orderDetail，并存入缓存
 */
