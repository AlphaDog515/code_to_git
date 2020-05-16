package com.app.detect

import java.util

import org.apache.flink.api.common.state.{ListStateDescriptor, ValueStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer


case class LoginEvent(userId: Long, ip: String, eventType: String, eventTime: Long)

case class Warning(userId: Long, firstFailTime: Long, lastFailTime: Long, warningMsg: String)

object LoginFail {

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

    // 转换 使用processFunc
    val warningDS = dataDS
      .keyBy(_.userId)
      .process(new LoginFailWarning(2))

    warningDS.print()
    env.execute("login fail job")
  }

}


class LoginFailWarning(maxFailTimes: Int) extends KeyedProcessFunction[Long, LoginEvent, Warning] {

  // 定义状态  注册两秒的定时器
  lazy val loginFailListState =
    getRuntimeContext.getListState(new ListStateDescriptor[LoginEvent]("login_fail", classOf[LoginEvent]))

  lazy val timeTsState =
    getRuntimeContext.getState(new ValueStateDescriptor[Long]("timer_ts", classOf[Long]))

  override def processElement(value: LoginEvent, ctx: KeyedProcessFunction[Long, LoginEvent, Warning]#Context, out: Collector[Warning]): Unit = {

    // 判断当前数据是否失败   延迟数据的处理？
    if (value.eventType == "fail") {
      loginFailListState.add(value)
      if (timeTsState.value() == 0) {
        val ts = value.eventTime * 1000L + 2000L
        ctx.timerService().registerEventTimeTimer(ts)
        timeTsState.update(ts)
      }
    } else {
      ctx.timerService().deleteEventTimeTimer(timeTsState.value())
      loginFailListState.clear()
      timeTsState.clear()
    }


  }

  override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Long, LoginEvent, Warning]#OnTimerContext, out: Collector[Warning]): Unit = {
    val failList = new ListBuffer[LoginEvent]()
    val iter: util.Iterator[LoginEvent] = loginFailListState.get().iterator()
    while (iter.hasNext) {
      failList += iter.next()
      if (failList.length >= maxFailTimes) {
        out.collect(Warning(ctx.getCurrentKey, failList.head.eventTime, failList.last.eventTime, failList.length + "times"))
      }
    }
    loginFailListState.clear()
    timeTsState.clear()
  }
}