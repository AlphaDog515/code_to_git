package com.gmall.realtime.app

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import com.alibaba.fastjson.{JSON, JSONObject}
import com.gmall.common.Constant
import com.gmall.realtime.bean.StartupLog
import com.gmall.realtime.util.{MyKafkaUtil, RedisUtil}
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis



object DauApp {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("app01").setMaster("local[2]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))
    val sourceStream: DStream[String] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_STARTUP)


    val startupLogDS: DStream[StartupLog] = sourceStream.map(jsonStr => {
      JSON.parseObject(jsonStr, classOf[StartupLog])
    })

    println(startupLogDS)

    val firstStartupDS = startupLogDS.transform(rdd => {
      // 这些代码都在driver中
      // 从redis中读取已经启动的设备
      // 过滤  set topic
      val client: Jedis = RedisUtil.getClient
      val key = Constant.TOPIC_STARTUP + ":" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())
      val mids: util.Set[String] = client.smembers(key)
      client.close()

      // 广播变量
      val midsBD: Broadcast[util.Set[String]] = ssc.sparkContext.broadcast(mids)

      // 某个mid在一批次中有多次 第一次会重复 需要进一步去重
      rdd.filter(log => !midsBD.value.contains(log.mid))
        .map(log => (log.mid, log))
        .groupByKey()
        .map {
          // case (_, it) => it.toList.sortBy(_.ts).head // 取的是第一次记录
          case (_, it) => it.minBy(_.ts)
        }

    })

    import org.apache.phoenix.spark._
    // 第一次的保存
    firstStartupDS.foreachRDD(rdd => {
      rdd.foreachPartition((it: Iterator[StartupLog]) => {
        val client: Jedis = RedisUtil.getClient
        //  client.sadd()
        it.foreach((log: StartupLog) => {
          client.sadd(Constant.TOPIC_STARTUP + ":" + log.logDate, log.mid)
        })
        client.close()
      })


      rdd.saveToPhoenix(Constant.DAU_TABLE,
        Seq("MID", "UID", "APPID", "AREA", "OS", "CHANNEL", "LOGTYPE", "VERSION", "TS", "LOGDATE", "LOGHOUR"),
        zkUrl = Some("hadoop102,hadoop103,hadoop104:2181")
      )
    })

    // 写道hbashe


    firstStartupDS.print(100)
    ssc.start()
    ssc.awaitTermination()
  }

}
