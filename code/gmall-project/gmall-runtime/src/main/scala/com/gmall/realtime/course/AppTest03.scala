package com.gmall.realtime.course

import com.alibaba.fastjson.JSON
import com.gmall.realtime.bean.OrderInfo
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object AppTest03 {

  def main(args: Array[String]): Unit = {
    // 从canal采集数据到Kafka，消费Kafka，存到hbase
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("app")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))
    val params = Map[String, String]()
    val sourceStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, params, Set(""))
      .map(_._2)
    import org.apache.phoenix.spark._
    val orderInfoDS: DStream[OrderInfo] = sourceStream.map(line => JSON.parseObject(line, classOf[OrderInfo]))

    orderInfoDS.foreachRDD(rdd => {
      rdd.saveToPhoenix("", Seq(""), zkUrl = Some(""))
    })
    ssc.start()
    ssc.awaitTermination()

  }

}
