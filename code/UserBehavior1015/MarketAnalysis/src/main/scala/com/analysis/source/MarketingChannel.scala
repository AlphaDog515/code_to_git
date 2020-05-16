package com.analysis.source

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

case class MarketCount(windowStart: String, windowEnd: String, channel: String, behavior: String, count: Long)

class SimulateMarketEventSource extends RichParallelSourceFunction[MarketUserBehavior] {
  var running = true

  val behaviorSet = Seq("CLICK", "DOWNLOAD", "INSTALL", "UNINSTALL")
  val channelSet = Seq("appstore", "huaweiStroe", "weibo", "weichat")

  // 定义随机数生成器
  val rand = Random


  override def run(ctx: SourceFunction.SourceContext[MarketUserBehavior]): Unit = {
    // 控制测输数据的量
    val maxCounts = Long.MaxValue
    var count = 0L
    while (running && count < maxCounts) {
      val id = UUID.randomUUID().toString
      val behavior = behaviorSet(rand.nextInt(behaviorSet.size))
      val channel = channelSet(rand.nextInt(channelSet.size))
      val ts = System.currentTimeMillis()
      ctx.collect(MarketUserBehavior(id, behavior, channel, ts))
      count += 1
      Thread.sleep(50)
    }


  }

  override def cancel(): Unit = running = false
}

object MarketingChannel {

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val dataDS = env.addSource(new SimulateMarketEventSource)
      .assignAscendingTimestamps(_.timestamp)


    val resDS = dataDS
      .filter(_.behavior != "UNINSTALL")
      .keyBy(data => (data.channel, data.behavior))
      .timeWindow(Time.hours(1), Time.seconds(5))
      .process(new MarketCountByChannel)

    resDS.print("job")
    env.execute("channel job")
  }

}


// processfunction拿到的信息比windowfunct多
class MarketCountByChannel extends ProcessWindowFunction[MarketUserBehavior, MarketCount, (String, String), TimeWindow] {

  override def process(key: (String, String),
                       context: Context, elements: Iterable[MarketUserBehavior],
                       out: Collector[MarketCount]): Unit = {

    val windowStart: String = new Timestamp(context.window.getStart).toString
    val windowEnd: String = new Timestamp(context.window.getEnd).toString

    val channel: String = key._1
    val behavior = key._2

    val count = elements.size.toLong

    out.collect(MarketCount(windowStart, windowEnd, channel, behavior, count))

  }
}