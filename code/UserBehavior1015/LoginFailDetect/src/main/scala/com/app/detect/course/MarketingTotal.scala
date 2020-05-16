package com.app.detect.course

import java.sql.Timestamp
import java.util.UUID

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.util.Random

case class MarketUserBehavior(userId: String, behavior: String, channel: String, timestamp: Long)

case class MarketCountView(windowStart: String, windowEnd: String, channel: String, behavior: String, count: Long)

class SimulateEventSource extends RichParallelSourceFunction[MarketUserBehavior] {
  var running = true
  val behaviorSet = Seq("Click", "Download", "Install", "Uninstall")
  val channelSet = Seq("appstore", "huaweistore", "weibo")
  val rand = Random

  override def run(ctx: SourceFunction.SourceContext[MarketUserBehavior]): Unit = {
    val maxCounts = Long.MaxValue
    var count = 0L
    while (running && count < maxCounts) {
      val id = UUID.randomUUID().toString
      val behavior = behaviorSet(rand.nextInt(behaviorSet.size))
      val channel = channelSet(rand.nextInt(channelSet.size))
      val ts = System.currentTimeMillis()
      ctx.collect(MarketUserBehavior(id, behavior, channel, ts))
      count += 1
      Thread.sleep(50L)
    }

  }

  override def cancel(): Unit = running = false
}


object MarketingTotal {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    val dataDS: DataStream[MarketUserBehavior] = env.addSource(new SimulateEventSource)
      .assignAscendingTimestamps(_.timestamp)

    val resDS = dataDS
      .filter(_.behavior != "Uninstall")
      .keyBy(data => (data.channel, data.behavior))
      .timeWindow(Time.hours(1), Time.seconds(5))
      .process(new MarketChannel)

    resDS.print("job")
    env.execute("job")

  }

}

class MarketChannel extends ProcessWindowFunction[MarketUserBehavior, MarketCountView, (String, String), TimeWindow] {
  override def process(key: (String, String),
                       context: Context,
                       elements: Iterable[MarketUserBehavior],
                       out: Collector[MarketCountView]): Unit = {
    val start = new Timestamp(context.window.getStart).toString
    val end = new Timestamp(context.window.getEnd).toString
    val channel = key._1
    val behavior = key._2
    val count = elements.size
    out.collect(MarketCountView(start, end, channel, behavior, count))
  }
}
