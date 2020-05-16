package com.networks_analysis.app

import org.apache.flink.api.common.functions.{AggregateFunction, MapFunction, RichMapFunction}
import org.apache.flink.api.common.state.ValueStateDescriptor
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.util.Random


// 定义输入数据的样例类
case class UserBehavior(userId: Long, itemId: Long, categoryId: Int, behavior: String, timestamp: Long)

case class PvCountView(windEnd: Long, count: Long)

object PageView {

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)


    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val path = getClass.getClassLoader.getResource("apache.log").getPath
    val inputDS = env.readTextFile(path)


    val dataDS = inputDS.map(data => {
      val arr: Array[String] = data.split(",")
      UserBehavior(arr(0).toLong, arr(1).toLong, arr(2).toInt, arr(3), arr(4).toLong)
    })
      .assignAscendingTimestamps(_.timestamp * 1000L) // 默认延迟是1ms

    val pvDS = dataDS.filter(_.behavior == "pv")
      .map(data => ("pv", 1L))
      .keyBy(_._1)
      .timeWindow(Time.hours(1))
      .aggregate(new PvCountAg, new PvCountRes)

    pvDS.print("job")
    env.execute("job")

  }

}


class PvCountAg extends AggregateFunction[(String, Long), Long, Long] {
  override def createAccumulator(): Long = 0L

  override def add(value: (String, Long), accumulator: Long): Long = accumulator + 1

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}

class PvCountRes extends WindowFunction[Long, PvCountView, String, TimeWindow] {
  override def apply(key: String,
                     window: TimeWindow, input: Iterable[Long],
                     out: Collector[PvCountView]): Unit = {
    out.collect(PvCountView(window.getEnd, 1))
  }
}

// 均匀分配Key 随机生成
class MyMapper extends MapFunction[UserBehavior, (String, Long)] {
  override def map(value: UserBehavior): (String, Long) = {
    (Random.nextString(10), 1L)
  }
}

// 自定义process 将聚合窗口合并
class TotalResult extends KeyedProcessFunction[Long, PvCountView, PvCountView] {

  lazy val totalCountState = getRuntimeContext.getState(new ValueStateDescriptor[Long]("total_count", classOf[Long]))

  override def processElement(value: PvCountView,
                              ctx: KeyedProcessFunction[Long, PvCountView, PvCountView]#Context,
                              out: Collector[PvCountView]): Unit = {

    val curr = totalCountState.value()
    totalCountState.update(curr + value.count)

    // 注册定时器
    ctx.timerService().registerEventTimeTimer(value.windEnd + 1)
  }

  override def onTimer(timestamp: Long,
                       ctx: KeyedProcessFunction[Long, PvCountView, PvCountView]#OnTimerContext,
                       out: Collector[PvCountView]): Unit = {

    out.collect(PvCountView(ctx.getCurrentKey, totalCountState.value()))
    totalCountState.clear()  // 防止状态溢出 清空

  }
}


