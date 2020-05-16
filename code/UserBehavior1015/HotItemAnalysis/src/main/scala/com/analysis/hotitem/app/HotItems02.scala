package com.analysis.hotitem.app

import java.sql.Timestamp
import java.util.{Comparator, PriorityQueue}

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.api.java.tuple.{Tuple, Tuple1}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object HotItems02 {

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val path: String = getClass.getClassLoader.getResource("sensor.txt").getPath
    val inputDS: DataStream[String] = env.readTextFile(path)

    val dataDS = inputDS
      .map(data => {
        val arr: Array[String] = data.split(",")
        UserBehavior(arr(0).toLong, arr(1).toLong, arr(2).toInt, arr(3), arr(4).toLong)
      })
      .assignAscendingTimestamps(_.timestamp * 1000L)


    val aggDS = dataDS
      .filter(_.behavior == "pv")
      .keyBy("itemId")
      .timeWindow(Time.hours(1), Time.milliseconds(5))
      .aggregate(new MyAgg, new ItemRes)

    val res = aggDS
      .keyBy("windowEnd")
      .process(new ItemTopN(5))

    res.print("job")
    env.execute("job")

  }

}


class MyAgg extends AggregateFunction[UserBehavior, Long, Long] {
  override def createAccumulator(): Long = 0L

  override def add(value: UserBehavior, accumulator: Long): Long = 1 + accumulator

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}

class ItemRes extends WindowFunction[Long, ItemViewCount, Tuple, TimeWindow] {
  override def apply(key: Tuple,
                     window: TimeWindow,
                     input: Iterable[Long],
                     out: Collector[ItemViewCount]): Unit = {
    val itemId = key.asInstanceOf[Tuple1[Long]].f0
    val end: Long = window.getEnd
    val count: Long = input.iterator.next()
    out.collect(ItemViewCount(itemId, end, count))
  }
}

class ItemTopN(n: Int) extends KeyedProcessFunction[Tuple, ItemViewCount, String] {

  var itemListState: ListState[ItemViewCount] = _

  override def open(parameters: Configuration): Unit = {
    val listDes: ListStateDescriptor[ItemViewCount] = new ListStateDescriptor[ItemViewCount]("item_list", classOf[ItemViewCount])
    itemListState = getRuntimeContext.getListState(listDes)
  }

  override def processElement(value: ItemViewCount,
                              ctx: KeyedProcessFunction[Tuple, ItemViewCount, String]#Context,
                              out: Collector[String]): Unit = {

    ctx.timerService().registerEventTimeTimer(value.windowEnd + 100)
    itemListState.add(value)

  }

  override def onTimer(timestamp: Long,
                       ctx: KeyedProcessFunction[Tuple, ItemViewCount, String]#OnTimerContext,
                       out: Collector[String]): Unit = {

    val queue = new PriorityQueue[ItemViewCount](new Comparator[ItemViewCount] {
      override def compare(o1: ItemViewCount, o2: ItemViewCount): Int = {
        (o1.count - o2.count).toInt
      }
    })

    import scala.collection.JavaConversions._
    for (item <- itemListState.get()) {
      queue.add(item)
      if (queue.size() > n) {
        queue.remove()
      }
    }

    val list: List[ItemViewCount] = queue.toList.sortBy(-_.count)
    itemListState.clear()

    var result = "时间：" + new Timestamp(timestamp - 100) + "\n"
    for (i <- list.indices) {
      val curr = list(i)
      result = result + "Top" + (i + 1) + "商品ID=" + curr.itemId + "访问量=" + curr.count + "\n"
    }

    out.collect(result)
  }


}