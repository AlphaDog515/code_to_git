package com.gmall.realtime.course

import java.util

import com.alibaba.fastjson.JSON
import com.gmall.common.Constant
import com.gmall.realtime.bean.{AlertInfo, EventLog}
import com.gmall.realtime.util.MyKafkaUtil
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}

import scala.util.control.Breaks

object AppTest04 {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("lcoal[2]").setAppName("app")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))

    val sourceStream: DStream[String] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_EVENT).window(Minutes(5), Seconds(6))

    val eventLogStream: DStream[EventLog] = sourceStream.map(s => JSON.parseObject(s, classOf[EventLog]))


    val groupedDS: DStream[(String, Iterable[EventLog])] = eventLogStream.map(log => (log.mid, log)).groupByKey()

    groupedDS.map {
      case (mid, enevtLog: Iterable[EventLog]) =>
        val uidSet: util.HashSet[String] = new util.HashSet[String]()
        val eventList: util.ArrayList[String] = new util.ArrayList[String]()
        val itemSet: util.HashSet[String] = new util.HashSet[String]()

        var isClickItem: Boolean = false
        Breaks.breakable {
          enevtLog.foreach(log => {
            eventList.add(log.eventId)
            log.eventId match {
              case "coupon" =>
                uidSet.add(log.uid)
                itemSet.add(log.itemId)
              case "clickItem" =>
                isClickItem = true
                Breaks.break
              case _ =>
            }

          })
        }

        (uidSet.size() > 3 && !isClickItem, AlertInfo(mid, uidSet, itemSet, eventList, System.currentTimeMillis()))
    }


    ssc.start()
    ssc.awaitTermination()

  }

}
