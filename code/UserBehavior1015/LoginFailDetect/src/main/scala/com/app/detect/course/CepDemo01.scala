package com.app.detect.course

import java.util

import com.app.detect.{LoginEvent, Warning}
import org.apache.flink.cep.PatternSelectFunction
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object CepDemo01 {

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val path: String = getClass.getResource("/LoginLog.csv").getPath
    val inputDS: DataStream[String] = env.readTextFile(path)

    val loginDS = inputDS
      .map(data => {
        val arr = data.split(",")
        LoginEvent(arr(0).toLong, arr(1), arr(2), arr(3).toLong)
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[LoginEvent](Time.seconds(2)) {
        override def extractTimestamp(element: LoginEvent): Long = element.eventTime * 1000L
      })

    val loginPattern = Pattern
      .begin[LoginEvent]("firstFail").where(_.eventType == "fail")
      .next("secondFail").where(_.eventType == "fail")
      .within(Time.seconds(2))

    val loginFailDS: PatternStream[LoginEvent] = CEP.pattern(loginDS.keyBy(_.userId), loginPattern)

    val resDS = loginFailDS.select(new PatternFunDetect)
    resDS.print()
    env.execute("job")


  }

}


class PatternFunDetect extends PatternSelectFunction[LoginEvent, Warning] {
  override def select(map: util.Map[String, util.List[LoginEvent]]): Warning = {
    val first = map.get("firstFail").get(0)
    val last = map.get("senondFail").get(0)
    Warning(first.userId, first.eventTime, last.eventTime, "login fail")
  }
}