package com.analysis.hotitem.app

import java.sql.Timestamp
import java.util.Properties

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.api.java.tuple.{Tuple, Tuple1}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer


case class UserBehavior(userId: Long, itemId: Long, categoryId: Int, behavior: String, timestamp: Long)

case class ItemViewCount(itemId: Long, windowEnd: Long, count: Long)

object HotItems01 {

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    // Kafka读取数据
    /*val properties = new Properties()
    properties.setProperty("bootstrap.servers", "hadoop103:9092")
    properties.setProperty("group.id", "consumer-group")
    properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("auto.offset.reset", "earliest")

    val kafkaConsumer =
      new FlinkKafkaConsumer[String]("hot_items", new SimpleStringSchema(), properties)
    val inputDS = env.addSource(kafkaConsumer)*/

   val inputDS: DataStream[String] = env.readTextFile("D:\\Scala\\workspace\\UserBehavior1015\\HotItemAnalysis\\src\\main\\resources\\UserBehavior.csv")


    val dataDS = inputDS.map(data => {
      val arr: Array[String] = data.split(",")
      UserBehavior(arr(0).toLong, arr(1).toLong, arr(2).toInt, arr(3), arr(4).toLong)
    })
      .assignAscendingTimestamps(_.timestamp * 1000L)


    val aggDS = dataDS
      .filter(_.behavior == "pv")
      .keyBy("itemId")
      .timeWindow(Time.hours(1), Time.minutes(5))
      .aggregate(new CountAgg, new ItemWindowResult)

  //  aggDS.print("aggDS")

    val res = aggDS
      .keyBy("windowEnd")
      .process(new TopNItems(5))

    res.print("job")
    env.execute("job test")

  }

}

class CountAgg extends AggregateFunction[UserBehavior, Long, Long] {
  override def createAccumulator(): Long = 0L

  override def add(value: UserBehavior, accumulator: Long): Long = accumulator + 1

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}

class ItemWindowResult extends WindowFunction[Long, ItemViewCount, Tuple, TimeWindow] {
  override def apply(key: Tuple,
                     window: TimeWindow,
                     input: Iterable[Long], out: Collector[ItemViewCount]): Unit = {

    val itemId = key.asInstanceOf[Tuple1[Long]].f0
    val windowEnd = window.getEnd
    val count = input.iterator.next()
    out.collect(ItemViewCount(itemId, windowEnd, count))
  }
}

class TopNItems(n: Int) extends KeyedProcessFunction[Tuple, ItemViewCount, String] {

  var itemListState: ListState[ItemViewCount] = _

  override def open(parameters: Configuration): Unit = {
    val listDes: ListStateDescriptor[ItemViewCount] = new ListStateDescriptor[ItemViewCount]("item_count_list", classOf[ItemViewCount])
    itemListState = getRuntimeContext.getListState(listDes)
  }

  override def onTimer(timestamp: Long,
                       ctx: KeyedProcessFunction[Tuple, ItemViewCount, String]#OnTimerContext,
                       out: Collector[String]): Unit = {

    val allItems = ListBuffer[ItemViewCount]()
    import scala.collection.JavaConversions._
    for (item <- itemListState.get()) {
      allItems += item
    }

    val sortedList: ListBuffer[ItemViewCount] = allItems.sortBy(_.count)(Ordering.Long.reverse).take(n)

    itemListState.clear()

    var result = "时间：" + new Timestamp(timestamp - 100) + "\n"

    for (i <- sortedList.indices) {
      val curr: ItemViewCount = sortedList(i)
      result = result + "Top" + (i + 1) + ": 商品ID=" + curr.itemId + " 访问量=" + curr.count + "\n"
    }

    result = result + "---------------------------"
    //   Thread.sleep(10000L)
  //  println(result)
    out.collect(result)


  }

  override def processElement(value: ItemViewCount,
                              ctx: KeyedProcessFunction[Tuple, ItemViewCount, String]#Context,
                              out: Collector[String]): Unit = {

  //  print("hello world")
    ctx.timerService().registerEventTimeTimer(value.windowEnd + 100L)

    itemListState.add(value)
  }
}