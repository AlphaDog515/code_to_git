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
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis


object AppTest11 {


  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[3]").setAppName("AppTest11")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))

    val sourceDS: DStream[String] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_STARTUP)
    val startupLogDS: DStream[StartupLog] = sourceDS.map(line => JSON.parseObject(line, classOf[StartupLog]))

    val firstDS: DStream[StartupLog] = startupLogDS.transform(rdd => {
      val client = RedisUtil.getClient
      val key = Constant.TOPIC_STARTUP + ":" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
      val mids: util.Set[String] = client.smembers(key) // 取出set集合的所有key
      client.close()

      val midsBD: Broadcast[util.Set[String]] = ssc.sparkContext.broadcast(mids)
      rdd.filter(log => !midsBD.value.contains(log.mid))
        .map(log => (log.mid, log))
        .groupByKey()
        .map {
          case (_, it) =>
            it.toList.minBy(_.ts)
        }
    })


    firstDS.foreachRDD(rdd => {
      rdd.foreachPartition(logIt => {
        val client: Jedis = RedisUtil.getClient
        logIt.foreach(log => {
          client.sadd(Constant.TOPIC_STARTUP + ":" + log.logDate, log.mid)
        })
        client.close()
      })
      import org.apache.phoenix.spark._
      rdd.saveToPhoenix(Constant.DAU_TABLE, Seq(""), zkUrl = Some(""))
    })


    ssc.start()
    ssc.awaitTermination()

  }

}
