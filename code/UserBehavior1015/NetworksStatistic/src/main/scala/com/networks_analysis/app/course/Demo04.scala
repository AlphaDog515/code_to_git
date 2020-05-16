package com.networks_analysis.app.course

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.AllWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.api.windowing.windows.TimeWindow.Serializer.TimeWindowSerializerSnapshot
import org.apache.flink.util.Collector


object Demo04 {


  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val path = ""
    val inputDS: DataStream[String] = env.readTextFile(path)

    val dataDS: DataStream[UserBehavior] = inputDS
      .map(data => {
        val dataArray = data.split(",")
        UserBehavior(dataArray(0).toLong, dataArray(1).toLong, dataArray(2).toInt, dataArray(3), dataArray(4).toLong)
      })
      .assignAscendingTimestamps(_.timestamp * 1000L)

    val uvDS = dataDS
      .filter(_.behavior == "pv")
      .timeWindowAll(Time.hours(1))
      .aggregate(new UvCountAgg, new UvCountResultWithIncreAgg)

    uvDS.print()
    env.execute("my job")


  }

}


class UvCountAgg extends AggregateFunction[UserBehavior, Set[Long], Long] {
  override def createAccumulator(): Set[Long] = Set[Long]()

  override def add(value: UserBehavior, accumulator: Set[Long]): Set[Long] = accumulator + value.userId

  override def getResult(accumulator: Set[Long]): Long = accumulator.size

  override def merge(a: Set[Long], b: Set[Long]): Set[Long] = a ++ b
}

class UvCountResultWithIncreAgg extends AllWindowFunction[Long, UvCount, TimeWindow] {
  override def apply(window: TimeWindow, input: Iterable[Long], out: Collector[UvCount]): Unit = {
    out.collect(UvCount(window.getEnd, input.head))
  }
}


class UvCountResult extends AllWindowFunction[UserBehavior, UvCount, TimeWindow] {
  override def apply(window: TimeWindow, input: Iterable[UserBehavior], out: Collector[UvCount]): Unit = {

    var idSet = Set[Long]()
    for (ub <- input) {
      idSet += ub.userId
    }
    out.collect(UvCount(window.getEnd, idSet.size))
  }

}