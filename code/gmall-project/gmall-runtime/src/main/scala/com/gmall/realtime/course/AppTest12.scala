package com.gmall.realtime.course

import java.util

import com.alibaba.fastjson.JSON
import com.gmall.common.Constant
import com.gmall.realtime.bean.{AlertInfo, EventLog}
import com.gmall.realtime.util.MyKafkaUtil
import com.gmall.scala.util.ESUtil
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}

import scala.util.control.Breaks


object AppTest12 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("app").setMaster("local[3]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(2))

    val sourceDS: DStream[String] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_EVENT).window(Minutes(5), Seconds(6))
    val eventLogDS: DStream[EventLog] = sourceDS.map(line => JSON.parseObject(line, classOf[EventLog]))
    val alertInfoDS: DStream[(Boolean, AlertInfo)] = eventLogDS.map(log => (log.mid, log)).groupByKey()
      .map {
        case (mid, eventLogIt) =>
          val uidSet = new java.util.HashSet[String]()
          val eventList: util.ArrayList[String] = new util.ArrayList[String]()
          val itemSet: util.HashSet[String] = new util.HashSet[String]()
          var isClick = false

          Breaks.breakable {
            eventLogIt.foreach(log => {
              eventList.add(log.eventId)
              log.eventId match {
                case "coupon" =>
                  uidSet.add(log.uid)
                  itemSet.add(log.itemId)
                case "clickItem" =>
                  isClick = true
                  Breaks.break
              }
            })
          }

          (uidSet.size() >= 3 && !isClick, AlertInfo(mid, uidSet, itemSet, eventList, System.currentTimeMillis()))
      }


    alertInfoDS.filter(_._1).map(_._2).foreachRDD(rdd => {
      rdd.foreachPartition(alertInfo => {
        val data: Iterator[(String, AlertInfo)] = alertInfo.map(info => (info.mid + ":" + info.ts / 1000 / 60, info))
        ESUtil.insertBulk_02("",data)
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }

}
