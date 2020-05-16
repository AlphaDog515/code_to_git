package com.app.detect.course


import java.sql.Timestamp

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector


case class AdClickEvent(userId: Long, adId: Long, province: String, city: String, timestamp: Long)

case class AdCountByProvince(province: String, windowEnd: String, count: Long)

case class BlackListWarning(userId: Long, adId: Long, msg: Long)

object BlackList {
  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val path: String = getClass.getResource("").getPath
    val inputDS = env.readTextFile(path)

    val adDS = inputDS
      .map(data => {
        val arr = data.split(",")
        AdClickEvent(arr(0).toLong, arr(1).toLong, arr(2), arr(3), arr(4).toLong)
      })
      .assignAscendingTimestamps(_.timestamp * 1000L)

    val filterDS = adDS
      .keyBy(data => (data.userId, data.adId))
      .process(new FilterBlackList(100L))

    val res = filterDS
      .keyBy(_.province)
      .timeWindow(Time.hours(1), Time.seconds(5))
      .aggregate(new AdCountAgg, new AdCountResult)

    res.print()
    filterDS.getSideOutput(new OutputTag[BlackListWarning]("black_list")).print("blackList")
    env.execute("job")

  }

}

class FilterBlackList(maxClickCount: Long) extends KeyedProcessFunction[(Long, Long), AdClickEvent, AdClickEvent] {

  lazy val countState =
    getRuntimeContext.getState(new ValueStateDescriptor[Long]("count", classOf[Long]))

  lazy val isSentState =
    getRuntimeContext.getState(new ValueStateDescriptor[Boolean]("is_sent", classOf[Boolean]))

  override def processElement(value: AdClickEvent,
                              ctx: KeyedProcessFunction[(Long, Long), AdClickEvent, AdClickEvent]#Context,
                              out: Collector[AdClickEvent]): Unit = {

    val count = countState.value()
    if (count == 0) {
      val ts = (ctx.timerService().currentProcessingTime() / (1000 * 60 * 60 * 24) + 1) * (1000 * 60 * 60 * 24)
      ctx.timerService().registerProcessingTimeTimer(ts)
    }

    if (count >= maxClickCount) {
      if (!isSentState.value()) {
        ctx.output(new OutputTag[BlackListWarning]("blacklist"), BlackListWarning(value.userId, value.adId, maxClickCount + ""))
        isSentState.update(true)
      }
      return
    }

    countState.update(count + 1)
    out.collect(value)

  }

  override def onTimer(timestamp: Long,
                       ctx: KeyedProcessFunction[(Long, Long), AdClickEvent, AdClickEvent]#OnTimerContext,
                       out: Collector[AdClickEvent]): Unit = {

    countState.clear()
    isSentState.clear()
  }

}

class AdCountAgg extends AggregateFunction[AdClickEvent, Long, Long] {
  override def createAccumulator(): Long = 0L

  override def add(value: AdClickEvent, accumulator: Long): Long = accumulator + 1L

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}

class AdCountResult extends WindowFunction[Long, AdCountByProvince, String, TimeWindow] {
  override def apply(key: String, window: TimeWindow, input: Iterable[Long], out: Collector[AdCountByProvince]): Unit = {
    val province = key
    val end = new Timestamp(window.getEnd).toString
    val count = input.iterator.next()
    out.collect(AdCountByProvince(province, end, count))
  }
}
