package com.analysis.hotitem

import java.sql.Timestamp
import java.util.Properties

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.ListStateDescriptor
import org.apache.flink.api.java.tuple.{Tuple, Tuple1}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer

// 定义输入数据的样例类
case class UserBehavior(userId: Long, itemId: Long, categoryId: Int, behavior: String, timestamp: Long)

// 定义窗口聚合结果的样例类
case class ItemViewCount(itemId: Long, windowEnd: Long, count: Long)

object HotItems {

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)


    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)


    //    val path: String = getClass.getClassLoader.getResource("UserBehavior.csv").getPath
    //    val inputDS: DataStream[String] = env.readTextFile(path)
    val properties = new Properties()

    val inputDS = env.addSource(new FlinkKafkaConsumer[String](
      "hotItems",
      new SimpleStringSchema(),
      properties
    ))


    val dataDS: DataStream[UserBehavior] = inputDS.map(data => {
      val arr: Array[String] = data.split(",")
      UserBehavior(arr(0).toLong, arr(1).toLong, arr(2).toInt, arr(3), arr(4).toLong)
    })
      .assignAscendingTimestamps(_.timestamp * 1000L) // 默认延迟是1ms


    // 过滤出pv行为
    val aggDS = dataDS
      .filter(_.behavior == "pv")
      .keyBy("itemId")
      .timeWindow(Time.hours(1), Time.minutes(5))
      .aggregate(new CountAgg(), new ItemCountWindowResult())

    // 对窗口聚合结果按照窗口结果进行分组聚合获取topN
    val res = aggDS
      .keyBy("windowEnd")
      .process(new TopNHotItems(5)) // 状态编程使用 process

    res.print("res")

    env.execute("hotitem job")
  }


}

// [in acc out]
class CountAgg() extends AggregateFunction[UserBehavior, Long, Long] {
  override def createAccumulator(): Long = 0L

  override def add(value: UserBehavior, accumulator: Long): Long = accumulator + 1

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}

// 输入是预聚合的输出
class ItemCountWindowResult() extends WindowFunction[Long, ItemViewCount, Tuple, TimeWindow] {
  override def apply(key: Tuple,
                     window: TimeWindow,
                     input: Iterable[Long],
                     out: Collector[ItemViewCount]): Unit = {

    val itemId = key.asInstanceOf[Tuple1[Long]].f0
    val windowEnd = window.getEnd
    val count = input.iterator.next()
    out.collect(ItemViewCount(itemId, windowEnd, count))

  }
}


// 自定义KeyedProcssFunction
class TopNHotItems(n: Int) extends KeyedProcessFunction[Tuple, ItemViewCount, String] {

  lazy val itemListState =
    getRuntimeContext.getListState(new ListStateDescriptor[ItemViewCount]("list_state", classOf[ItemViewCount]))

  override def processElement(value: ItemViewCount,
                              ctx: KeyedProcessFunction[Tuple, ItemViewCount, String]#Context,
                              out: Collector[String]): Unit = {

    itemListState.add(value)

    // 注册定时器  keyedstream 以后的操作只针对key 注册多个只触发一次  可以判断list是否为空添加
    ctx.timerService().registerEventTimeTimer(value.windowEnd + 100L)


  }

  override def onTimer(timestamp: Long,
                       ctx: KeyedProcessFunction[Tuple, ItemViewCount, String]#OnTimerContext,
                       out: Collector[String]): Unit = {
    // 定时器取数据 从状态中获取数据 排序 topN
    val allItemList = ListBuffer[ItemViewCount]()
    import scala.collection.JavaConversions._
    for (item <- itemListState.get()) {
      allItemList += item
    }

    val sortedCountList = allItemList.sortBy(_.count)(Ordering.Long.reverse).take(n)

    // 清空状态
    itemListState.clear()

    // 将排名信息格式化String 方便输出
    val result = new StringBuilder
    result.append("时间：").append(new Timestamp(timestamp - 100)).append("\n")
    // 遍历列表 输出topN信息
    for (i <- sortedCountList.indices) {
      val curr = sortedCountList(i)
      result.append("Top").append(i + 1).append(":").append(" 商品ID=").append(curr.itemId)
        .append(" 访问量：").append(curr.count).append("\n")

    }

    result.append("-----------------------------\n")

    out.collect(result.toString())

  }
}