package com.app.detect

import java.util

import org.apache.flink.cep.PatternSelectFunction
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object LoginFailCep {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val path = getClass.getResource("").getPath
    val inputDS: DataStream[String] = env.readTextFile(path)


    val dataDS = inputDS
      .map(data => {
        val arr = data.split(",")
        LoginEvent(arr(0).toLong, arr(1), arr(2), arr(3).toLong)
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[LoginEvent](Time.seconds(3)) {
        override def extractTimestamp(element: LoginEvent): Long = element.eventTime * 1000L
      })

    // 定义模式
    val loginFailPattern: Pattern[LoginEvent, LoginEvent] =
      Pattern.begin[LoginEvent]("firstFail").where(_.eventType == "fail") // 第一个个体模式
        .next("secondFail").where(_.eventType == "fail")
        .within(Time.seconds(2))
    // time(3) 非严格近邻

    // 获取patternDS
    val patternDS: PatternStream[LoginEvent] = CEP.pattern(dataDS.keyBy(_.userId), loginFailPattern)

    // 将检测到的事件序列转换输出报警信息
    val loginFailDS = patternDS.select(new LoginFailDetetect)

    loginFailDS.print()


    env.execute("cep job")
  }

}


class LoginFailDetetect extends PatternSelectFunction[LoginEvent, Warning] {

  // key 事件名称firstFail  list循环次数检测出来的是list
  override def select(map: util.Map[String, util.List[LoginEvent]]): Warning = {
    val firstLoginFail = map.get("firstFail").get(0)
    val secondLoginFail = map.get("secondFail").get(0)
    Warning(firstLoginFail.userId, firstLoginFail.eventTime, secondLoginFail.eventTime, "info")
  }
}