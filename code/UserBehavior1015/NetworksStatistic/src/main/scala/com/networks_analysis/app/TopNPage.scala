package com.networks_analysis.app


import java.text.SimpleDateFormat

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector


case class ApacheEventLog(ip: String, userId: String, eventTime: Long, method: String, url: String)

case class PageViewCount(url: String, windowEnd: Long, count: Long)


object TopNPage {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //   val path = getClass.getClassLoader.getResource("apache.log").getPath
    //    val inputDS: DataStream[String] = env.readTextFile(path)
    val inputDS: DataStream[String] = env.socketTextStream("hadoop103", 7777)

    val dataDS = inputDS
      .map(line => {
        val arr: Array[String] = line.split(" ")
        val simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss")
        val timestamp: Long = simpleDateFormat.parse(arr(3)).getTime
        ApacheEventLog(arr(0), arr(1), timestamp, arr(5), arr(6))
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[ApacheEventLog](Time.seconds(1)) {
        override def extractTimestamp(element: ApacheEventLog): Long = element.eventTime
      })

    val lateTag = new OutputTag[ApacheEventLog]("late_data")

    val aggDS = dataDS
      .keyBy(_.url)
      .timeWindow(Time.minutes(10), Time.seconds(5))
      .allowedLateness(Time.minutes(1))
      .sideOutputLateData(lateTag)
      .aggregate(new PageCountAgg, new PageWindowRes)

    val lateDS = aggDS.getSideOutput(lateTag)

    val res = aggDS
      .keyBy(_.windowEnd)
      .process(new TopNHotPage)

    res.print("res")
    env.execute("top n job")


  }

}

class PageCountAgg extends AggregateFunction[ApacheEventLog, Long, Long] {
  override def createAccumulator(): Long = 0L

  override def add(value: ApacheEventLog, accumulator: Long): Long = accumulator + 1

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}

class PageWindowRes extends WindowFunction[Long, PageViewCount, String, TimeWindow] {
  override def apply(key: String, window: TimeWindow, input: Iterable[Long], out: Collector[PageViewCount]): Unit = {
    out.collect(PageViewCount(key, window.getEnd, input.head))
  }
}

class TopNHotPage extends KeyedProcessFunction[Long, PageViewCount, String] {


  lazy val pageCountMapState =
    getRuntimeContext.getMapState(new MapStateDescriptor[String, Long]("map_state", classOf[String], classOf[Long]))


  override def processElement(value: PageViewCount, ctx: KeyedProcessFunction[Long, PageViewCount, String]#Context, out: Collector[String]): Unit = {

    pageCountMapState.put(value.url, value.count)

    ctx.timerService().registerEventTimeTimer(value.windowEnd + 1)
    ctx.timerService().registerEventTimeTimer(value.windowEnd + 60 * 1000L)

  }


  override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Long, PageViewCount, String]#OnTimerContext, out: Collector[String]): Unit = {


  }
}
