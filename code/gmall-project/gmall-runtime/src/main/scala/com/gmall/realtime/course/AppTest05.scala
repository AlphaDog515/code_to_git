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

object AppTest05 {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("thisclass").setMaster("lcoal[*]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))
    val sourceStream: DStream[String] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_EVENT).window(Minutes(5), Seconds(6))

    val eventLogStream: DStream[EventLog] = sourceStream.map(s => JSON.parseObject(s, classOf[EventLog]))
    val groupedDS: DStream[(String, Iterable[EventLog])] = eventLogStream.map(log => (log.mid, log)).groupByKey()

    groupedDS.map {
      case (mid, itLog: Iterable[EventLog]) =>

        var isClickEvent: Boolean = false
        val uidSet: util.HashSet[String] = new util.HashSet[String]()
        val eventList: util.ArrayList[String] = new util.ArrayList[String]()
        val itemIdSet: util.HashSet[String] = new util.HashSet[String]()

        Breaks.breakable {
          itLog.foreach(log => {
            eventList.add(log.eventId)
            log.eventId match {
              case "coupon" =>
                uidSet.add(log.uid)
                itemIdSet.add(log.itemId)
              case "clickItem" =>
                isClickEvent = true
                Breaks.break
            }

          })
        }
        (uidSet.size() > 3 && !isClickEvent, AlertInfo(mid, uidSet, itemIdSet, eventList, System.currentTimeMillis()))

    }


    ssc.start()
    ssc.awaitTermination()
  }

}
