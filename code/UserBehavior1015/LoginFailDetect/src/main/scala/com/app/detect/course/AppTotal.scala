package com.app.detect.course

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object AppTotal {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val dataDS: DataStream[MarketUserBehavior] = env.addSource(new SimulateEventSource)
      .assignAscendingTimestamps(_.timestamp)

    dataDS
      .filter(_.behavior != "Uinstall")
      .map(data => ("total", 1L))
      .keyBy(_._1)
      .timeWindow(Time.hours(1), Time.seconds(5))
      .aggregate(new MyAgg, new MyWinFun)

  }

}

class MyAgg extends AggregateFunction[(String, Long), Long, Long] {
  override def createAccumulator(): Long = 0L

  override def add(value: (String, Long), accumulator: Long): Long = accumulator + 1

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}

class MyWinFun extends WindowFunction[Long, MarketCountView, String, TimeWindow] {
  override def apply(key: String, window: TimeWindow, input: Iterable[Long], out: Collector[MarketCountView]): Unit = {
    val start = window.getStart.toString
    val end = window.getEnd.toString
    val count = input.iterator.next()
    out.collect(MarketCountView(start, end, "total", "total", count))
  }
}
