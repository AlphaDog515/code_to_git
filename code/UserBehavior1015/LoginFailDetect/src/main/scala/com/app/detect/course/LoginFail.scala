package com.app.detect.course

import org.apache.flink.api.common.state.{ListStateDescriptor, ValueState, ValueStateDescriptor}
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

    // 创建 2s 的定时器 失败添加 2s看次数

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //    val path = getClass.getResource("/").getPath
    //    val inputDS: DataStream[String] = env.readTextFile(path)
    val inputDS = env.socketTextStream("hadoop103", 7777)

    val dataDS = inputDS
      .map(line => {
        val arr = line.split(",")
        LoginEvent(arr(0).toLong, arr(1), arr(2), arr(3).toLong)
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[LoginEvent](Time.seconds(1)) {
        override def extractTimestamp(element: LoginEvent): Long = element.eventTime * 1000L
      })

    val warningDS = dataDS
      .keyBy(_.userId)
      .process(new LoginWarningFun(2L))

    warningDS.print("job")
    env.execute("login fail job")

  }

}

class LoginWarningFun(maxFailTimes: Long) extends KeyedProcessFunction[Long, LoginEvent, Warning] {

  lazy val loginFailListState =
    getRuntimeContext.getListState(new ListStateDescriptor[LoginEvent]("fail_list", classOf[LoginEvent]))

  lazy val timerTsState =
    getRuntimeContext.getState(new ValueStateDescriptor[Long]("ts", classOf[Long]))


  override def processElement(value: LoginEvent,
                              ctx: KeyedProcessFunction[Long, LoginEvent, Warning]#Context,
                              out: Collector[Warning]): Unit = {

    if (value.eventType == "fail") {
      loginFailListState.add(value)
      if (timerTsState == 0) {
        val ts = value.eventTime * 1000L + 2000L
        ctx.timerService().registerEventTimeTimer(ts)
        timerTsState.update(ts)
      }
    } else {
      ctx.timerService().deleteEventTimeTimer(timerTsState.value())
      loginFailListState.clear()
      timerTsState.clear()
    }

  }

  override def onTimer(timestamp: Long,
                       ctx: KeyedProcessFunction[Long, LoginEvent, Warning]#OnTimerContext,
                       out: Collector[Warning]): Unit = {

    val failList = new ListBuffer[LoginEvent]()
    val iter = loginFailListState.get().iterator()
    while (iter.hasNext) {
      failList += iter.next()
    }

    if (failList.length >= maxFailTimes) {
      out.collect(Warning(ctx.getCurrentKey,
        failList.head.eventTime,
        failList.last.eventTime,
        "login fail in 2s for" + failList.length + " times."))
    }

    loginFailListState.clear()
    timerTsState.clear()

  }

}
