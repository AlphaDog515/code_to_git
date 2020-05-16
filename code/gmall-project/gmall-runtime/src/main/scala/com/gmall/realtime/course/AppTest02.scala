package com.gmall.realtime.course

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import com.alibaba.fastjson.JSON
import com.gmall.common.Constant
import com.gmall.realtime.bean.StartupLog
import com.gmall.realtime.util.{MyKafkaUtil, RedisUtil}
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

object AppTest02 {
  def main(args: Array[String]): Unit = {

    // 消费Kafka的数据去重存到hbase中
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("app")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))

    val sourceDS: DStream[String] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_STARTUP)

    val logDS: DStream[StartupLog] = sourceDS.map(line => {
      JSON.parseObject(line, classOf[StartupLog])
    })

    val resDS: DStream[StartupLog] = logDS.transform(rdd => {

      val client: Jedis = RedisUtil.getClient
      val mids: util.Set[String] = client.smembers(Constant.TOPIC_STARTUP + ":" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()))

      client.close()
      val bd: Broadcast[util.Set[String]] = ssc.sparkContext.broadcast(mids)


      val groupedRDD: RDD[(String, Iterable[StartupLog])] = rdd.filter(log => !bd.value.contains(log.mid))
        .map(log => (log.mid, log))
        .groupByKey()
      val resRDD: RDD[StartupLog] = groupedRDD.map {
        case (_, log) => log.toList.minBy(_.ts)
      }

      resRDD
    })


    resDS.foreachRDD(rdd => {
      // 写道redis
      rdd.foreachPartition(it => {
        val client: Jedis = RedisUtil.getClient
        it.foreach(log => {
          val key = Constant.TOPIC_STARTUP + ":" + log.logDate
          client.sadd(key, log.mid)
        })
        client.close()
      })

      import org.apache.phoenix.spark._
      rdd.saveToPhoenix("1015", Seq(""), zkUrl = Some(""))
    })


  }

}
