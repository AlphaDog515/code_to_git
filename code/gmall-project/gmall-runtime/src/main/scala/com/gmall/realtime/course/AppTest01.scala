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

object AppTest01 {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("apptest01")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))

    val sourceStream: DStream[String] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_STARTUP)

    val logDS: DStream[StartupLog] = sourceStream.map(line => {
      val log: StartupLog = JSON.parseObject(line, classOf[StartupLog])
      log
    })

    // 去重
    val startupDS: DStream[StartupLog] = logDS.transform(rdd => {
      val client: Jedis = RedisUtil.getClient
      val mids: util.Set[String] = client.smembers(Constant.TOPIC_STARTUP + ":" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
      client.close()

      val midsBD: Broadcast[util.Set[String]] = ssc.sparkContext.broadcast(mids)

      rdd.filter(log => !midsBD.value.contains(log.mid))
        .map(log => (log.mid, log))
        .groupByKey()
        .map {
          case (_, it) => it.toList.minBy(_.ts)
        }
    })

    // 写到hbase
    import org.apache.phoenix.spark._
    startupDS.foreachRDD(rdd => {
      rdd.foreachPartition(logIt => {
        val client: Jedis = RedisUtil.getClient
        logIt.foreach(log => {
          client.sadd(Constant.TOPIC_STARTUP + ":" + log.logDate, log.mid)
        })
        client.close()
      })

      rdd.saveToPhoenix(Constant.DAU_TABLE, Seq(""), zkUrl = Some("hadoop102,hadoop103,hadoop104:2181"))
    })

    startupDS.print(1000)
    ssc.start()
    ssc.awaitTermination()


  }

}
