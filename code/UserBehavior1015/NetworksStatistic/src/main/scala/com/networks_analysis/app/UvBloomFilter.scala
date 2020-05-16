package com.networks_analysis.app

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.triggers.{Trigger, TriggerResult}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import redis.clients.jedis.Jedis

case class UserBehavior(userId: Long, itemId: Long, categoryId: Int, behavior: String, timestamp: Long)

case class UvCount(windowEnd: Long, count: Long)

object UvBloomFilter {

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val path = getClass.getClassLoader.getResource("").getPath
    val inputDS: DataStream[String] = env.readTextFile(path)

    val dataDS = inputDS
      .map(line => {
        val arr: Array[String] = line.split(",")
        UserBehavior(arr(0).toLong, arr(1).toLong, arr(2).toInt, arr(3), arr(4).toLong)
      })
      .assignAscendingTimestamps(_.timestamp * 1000L)

    val uvDS = dataDS
      .filter(_.behavior == "pv")
      .map(data => ("uv", data.userId))
      .keyBy(_._1)
      .timeWindow(Time.hours(1))
      .trigger(new MyTrigger)
      .process(new MyBloomFilter)


    uvDS.print("uvDS")
    env.execute("job")
  }

}

class MyTrigger extends Trigger[(String, Long), TimeWindow] {
  override def onElement(element: (String, Long), timestamp: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult = TriggerResult.FIRE_AND_PURGE

  override def onProcessingTime(time: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult = TriggerResult.CONTINUE

  override def onEventTime(time: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult = TriggerResult.CONTINUE

  override def clear(window: TimeWindow, ctx: Trigger.TriggerContext): Unit = {}
}

class Bloom(size: Long) extends Serializable {
  private val cap = size

  def hash(s: String, seed: Int) = {
    var res = 0
    for (i <- 0 until s.length) {
      res = res * seed + s.charAt(i)
    }
    (cap - 1) & res
  }

}


class MyBloomFilter extends ProcessWindowFunction[(String, Long), UvCount, String, TimeWindow] {

  var jedis: Jedis = _
  var bloom: Bloom = _

  override def open(parameters: Configuration): Unit = {
    jedis = new Jedis("hadoop103", 6379)
    bloom = new Bloom(1 << 30)
  }


  override def process(key: String,
                       context: Context, elements: Iterable[(String, Long)],
                       out: Collector[UvCount]): Unit = {

    val storedKey: String = context.window.getEnd.toString
    val countMap = "countMap"
    var count = 0L
    if (jedis.hget(countMap, storedKey) != null) {
      count = jedis.hget(countMap, storedKey).toLong
    }

    val userId = elements.last._2.toString
    val offset = bloom.hash(userId, 61)
    val isExist = jedis.getbit(storedKey, offset)

    if (!isExist) {
      jedis.setbit(storedKey, offset, true)
      jedis.hset(countMap, storedKey, (count + 1).toString)
    }

  }
}
