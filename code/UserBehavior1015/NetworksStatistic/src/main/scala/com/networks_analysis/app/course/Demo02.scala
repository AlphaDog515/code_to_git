package com.networks_analysis.app.course


import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util
import java.util.{Date, Map}

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer

case class ApacheEventLog(ip: String, userId: String, eventTime: Long, method: String, url: String)

case class PageViewCount(url: String, windowEnd: Long, count: Long)

object Demo02 {

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val inputDS = env.socketTextStream("hadoop103", 7777)

    val dataDS = inputDS
      .map(line => {
        val arr = line.split(",")
        val simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss")
        val date: Date = simpleDateFormat.parse(arr(3))
        val timestamp: Long = date.getTime
        ApacheEventLog(arr(0), arr(1), timestamp, arr(5), arr(6))
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[ApacheEventLog](Time.seconds(1)) {
        override def extractTimestamp(element: ApacheEventLog): Long = element.eventTime
      })

    val lateOutputTag = new OutputTag[ApacheEventLog]("late_data")
    val aggDS = dataDS
      .keyBy(_.url)
      .timeWindow(Time.minutes(10), Time.seconds(5))
      .allowedLateness(Time.minutes(1))
      .sideOutputLateData(lateOutputTag)
      .aggregate(new PageCountAgg, new PageCountWindowResult)

    val lateDS = aggDS.getSideOutput(lateOutputTag)

    val resDS = aggDS
      .keyBy(_.windowEnd)
      .process(new TopNHotPage(3))

    dataDS.print("data")
    aggDS.print("agg")
    lateDS.print("late")
    resDS.print("res")

    env.execute("top n job")

  }

}

class PageCountAgg extends AggregateFunction[ApacheEventLog, Long, Long] {
  override def createAccumulator(): Long = 0L

  override def add(value: ApacheEventLog, accumulator: Long): Long = accumulator + 1

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}

class PageCountWindowResult extends WindowFunction[Long, PageViewCount, String, TimeWindow] {
  override def apply(key: String,
                     window: TimeWindow,
                     input: Iterable[Long],
                     out: Collector[PageViewCount]): Unit = {
    out.collect(PageViewCount(key, window.getEnd, input.head))
  }
}

class TopNHotPage(n: Int) extends KeyedProcessFunction[Long, PageViewCount, String] {


  lazy val pageMapState =
    getRuntimeContext.getMapState(new MapStateDescriptor[String, Long]("page_count_map", classOf[String], classOf[Long]))


  override def processElement(value: PageViewCount,
                              ctx: KeyedProcessFunction[Long, PageViewCount, String]#Context,
                              out: Collector[String]): Unit = {
    pageMapState.put(value.url, value.count)
    ctx.timerService().registerEventTimeTimer(value.windowEnd + 1)
    ctx.timerService().registerEventTimeTimer(value.windowEnd + 60 * 1000L)

  }


  override def onTimer(timestamp: Long,
                       ctx: KeyedProcessFunction[Long, PageViewCount, String]#OnTimerContext,
                       out: Collector[String]): Unit = {

    if (timestamp == ctx.getCurrentKey + 60 * 1000L) {
      pageMapState.clear()
      return
    }

    val allPages = ListBuffer[(String, Long)]()
    val iter = pageMapState.entries().iterator()
    while (iter.hasNext) {
      val entry: Map.Entry[String, Long] = iter.next()
      allPages += ((entry.getKey, entry.getValue))
    }

    val sortPages: ListBuffer[(String, Long)] = allPages.sortWith(_._2 > _._2).take(n)

    var res = "时间：" + new Timestamp(timestamp - 1) + "\n"

    for (i <- sortPages.indices) {
      val curr: (String, Long) = sortPages(i)
      res = res + "Top" + (1 + i) + ": 页面url=" + curr._1 + " 访问量=" + curr._2 + "\n"
    }

    out.collect(res)
  }
}
