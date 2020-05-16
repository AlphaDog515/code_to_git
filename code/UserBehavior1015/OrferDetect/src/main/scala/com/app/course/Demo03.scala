package com.app.course

import java.util

import org.apache.flink.cep.{PatternSelectFunction, PatternTimeoutFunction}
import org.apache.flink.cep.scala.CEP
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

case class OrderEvent(orderId: Long, eventType: String, txId: String, eventTime: Long)

case class OrderResult(orderId: Long, resultMsg: String)

object Demo03 {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val path: String = getClass.getResource("/OrderLog.csv").getPath

    val dataDS = env.readTextFile(path)
      .map(data => {
        val arr = data.split(",")
        OrderEvent(arr(0).toLong, arr(1), arr(2), arr(3).toLong)
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[OrderEvent](Time.seconds(3)) {
        override def extractTimestamp(element: OrderEvent): Long = element.eventTime * 1000L
      })

    // 定义匹配模式
    val pattern = Pattern
      .begin[OrderEvent]("create").where(_.eventType == "create")
      .followedBy("pay").where(_.eventType == "pay")
      .within(Time.minutes(15))

    val patternDS = CEP.pattern(dataDS.keyBy(_.orderId), pattern)

    val orderTag = new OutputTag[OrderResult]("order timeout")

    val resDS = patternDS.select(orderTag, new TimeoutSelect, new OrderPaySelect)

    resDS.print()
    resDS.getSideOutput(orderTag).print()
    env.execute("job")


  }

}

class TimeoutSelect extends PatternTimeoutFunction[OrderEvent, OrderResult] {
  override def timeout(map: util.Map[String, util.List[OrderEvent]], l: Long): OrderResult = {
    val timeoutOrderId = map.get("create").iterator().next().orderId
    OrderResult(timeoutOrderId, "timeout at " + l)
  }
}

class OrderPaySelect extends PatternSelectFunction[OrderEvent, OrderResult] {
  override def select(map: util.Map[String, util.List[OrderEvent]]): OrderResult = {
    val payId = map.get("pay").get(0).orderId
    OrderResult(payId, "pay successfully")
  }
}
