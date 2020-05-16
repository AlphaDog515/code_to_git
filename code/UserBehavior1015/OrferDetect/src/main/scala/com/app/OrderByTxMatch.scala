package com.app

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

case class OrderEvent(orderId: Long, eventTpye: String, txId: String, eventTime: Long)

case class ReceiptEvent(orderId: Long, eventTpye: String, txId: String, eventTime: Long)

object OrderByTxMatch {
  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val path1 = getClass.getResource("").getPath

    val inputDS1 = env.readTextFile(path1)

    val path2 = getClass.getResource("").getPath

    val inputDS2 = env.readTextFile(path2)

    val orderEventDS = inputDS1
      .map(data => {
        val arr = data.split(",")
        OrderEvent(arr(0).toLong, arr(1), arr(2), arr(3).toLong)
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[OrderEvent](Time.seconds(3)) {
        override def extractTimestamp(element: OrderEvent): Long = element.eventTime * 1000L
      })
      .filter(_.txId != "")  // 过滤出

    // 使用txId联合



  }

}
