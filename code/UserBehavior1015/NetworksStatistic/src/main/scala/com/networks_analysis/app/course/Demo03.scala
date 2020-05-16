package com.networks_analysis.app.course

import org.apache.flink.api.common.functions.{AggregateFunction, RichMapFunction}
import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector


case class PvCount(windowEnd: Long, count: Long)

object Demo03 {

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val path = getClass.getClassLoader.getResource("UserBehavior.csv").getPath
    val inputDS: DataStream[String] = env.readTextFile(path)

    val dataDS: DataStream[UserBehavior] = inputDS
      .map(data => {
        val dataArray = data.split(",")
        UserBehavior(dataArray(0).toLong, dataArray(1).toLong, dataArray(2).toInt, dataArray(3), dataArray(4).toLong)
      })
      .assignAscendingTimestamps(_.timestamp * 1000L)


    val aggDS = dataDS
      .filter(_.behavior == "pv")
      .map(new MyMapper)
      .keyBy(_._1)
      .timeWindow(Time.hours(1))
      .aggregate(new PvCountAgg, new PvCountResult)

    val res = aggDS
      .keyBy(_.windowEnd)
      .process(new TotalPvCount)

    res.print()
    env.execute("my job")

  }

}

class MyMapper() extends RichMapFunction[UserBehavior, (String, Long)] {

  lazy val index = getRuntimeContext.getIndexOfThisSubtask

  override def map(value: UserBehavior): (String, Long) = {
    (index.toString, 1L)
  }

}

class PvCountAgg extends AggregateFunction[(String, Long), Long, Long] {
  override def createAccumulator(): Long = 0L

  override def add(value: (String, Long), accumulator: Long): Long = accumulator + 1

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}

class PvCountResult extends WindowFunction[Long, PvCount, String, TimeWindow] {
  override def apply(key: String,
                     window: TimeWindow,
                     input: Iterable[Long],
                     out: Collector[PvCount]): Unit = {

    out.collect(PvCount(window.getEnd, input.head))

  }
}

class TotalPvCount extends KeyedProcessFunction[Long, PvCount, PvCount] {

  lazy val totalCountState =
    getRuntimeContext.getState(new ValueStateDescriptor[Long]("total_count", classOf[Long]))

  override def processElement(value: PvCount,
                              ctx: KeyedProcessFunction[Long, PvCount, PvCount]#Context,
                              out: Collector[PvCount]): Unit = {
    totalCountState.update(totalCountState.value() + value.count)
    ctx.timerService().registerEventTimeTimer(value.windowEnd + 1)
  }

  override def onTimer(timestamp: Long,
                       ctx: KeyedProcessFunction[Long, PvCount, PvCount]#OnTimerContext,
                       out: Collector[PvCount]): Unit = {
    out.collect(PvCount(ctx.getCurrentKey, totalCountState.value()))
    totalCountState.clear()
  }
}
