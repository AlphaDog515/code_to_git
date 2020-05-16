package com.gmall.realtime.app

import com.alibaba.fastjson.JSON
import com.gmall.common.Constant
import com.gmall.realtime.bean.OrderInfo
import com.gmall.realtime.util.MyKafkaUtil
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object OrderApp {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("app").setMaster("local[2]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))
    val sourceStream: DStream[String] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_ORDER_INFO)


    val orderInfoStream: DStream[OrderInfo] = sourceStream.map(s => JSON.parseObject(s, classOf[OrderInfo]))


    import org.apache.phoenix.spark._

    orderInfoStream.foreachRDD(rdd =>{
      rdd.saveToPhoenix("gmall_order_info1015",
        Seq(""),  // 这里与样例类的顺序一样 不是按照字段名 json解析是按照字段名
        zkUrl = Some("hadoop102,hadoop103,hadoop104:2181")
      )
    })





   // sourceStream.print(100)
    ssc.start()
    ssc.awaitTermination()
  }

}
