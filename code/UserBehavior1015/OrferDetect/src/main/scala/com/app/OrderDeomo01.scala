package com.app

import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time


case class OrderEvent(orderId: Long, eventTpye: String, txId: String, eventTime: Long)

case class OrderResult(orderId: Long, resultMsg: String)

object OrderDeomo01 {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val path = getClass.getResource("").getPath

    val inputDS = env.readTextFile(path)

    val dataDS = inputDS
      .map(data => {
        val arr = data.split(",")
        OrderEvent(arr(0).toLong, arr(1), arr(2), arr(3).toLong)
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[OrderEvent](Time.seconds(3)) {
        override def extractTimestamp(element: OrderEvent): Long = element.eventTime * 1000L
      })

    val orderPattern = Pattern
      .begin[OrderEvent]("create").where(_.eventTpye == "create")
      .followedBy("pay").where(_.eventTpye == "pay")
      .within(Time.minutes(15))

    // 定义pattern 应用
    val patternDS = CEP.pattern(dataDS.keyBy(_.orderId), orderPattern)

    val outTag = new OutputTag[OrderResult]("order_timeout")




  }

}
