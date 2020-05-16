package com.app.detect.course

import java.util

import org.apache.flink.cep.PatternSelectFunction
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object PatternDemo01 {

  def main(args: Array[String]): Unit = {
    // 读取数据 分装 设置模式 选择 输出
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    val path = getClass.getResource("").getPath
    val inputDS = env.readTextFile(path)

    val dataDS = inputDS
      .map(line => {
        val arr = line.split(",")
        LoginEvent(arr(0).toLong, arr(1), arr(2), arr(3).toLong)
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[LoginEvent](Time.seconds(1)) {
        override def extractTimestamp(element: LoginEvent): Long = element.eventTime * 1000L
      })


    val pattern = Pattern
      .begin[LoginEvent]("firstFail").where(_.eventType == "fail")
      .next("secondFail").where(_.eventType == "fail")
      .within(Time.seconds(2))

    val patternDS: PatternStream[LoginEvent] = CEP.pattern(dataDS.keyBy(_.userId), pattern)

    val resDS = patternDS.select(new SelectFun)

    resDS.print("")
    env.execute("job")


  }

}

class SelectFun extends PatternSelectFunction[LoginEvent, Warning] {
  override def select(map: util.Map[String, util.List[LoginEvent]]): Warning = {
    val first = map.get("firstFail").get(0)
    val last = map.get("secondFail").get(0)
    Warning(first.userId, first.eventTime, last.eventTime, "login fail")
  }
}
