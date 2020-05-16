package com.gmall.realtime.app

import scala.util.control.Breaks._
import java.util

import com.alibaba.fastjson.JSON
import com.gmall.common.Constant
import com.gmall.realtime.bean.{AlertInfo, EventLog}
import com.gmall.realtime.util.MyKafkaUtil
import com.gmall.scala.util.ESUtil
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}

object AlertApp {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("AlertApp").setMaster("local[*]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))

    val sourceStream: DStream[String] = MyKafkaUtil.getKafkaStream(ssc, Constant.TOPIC_EVENT)
      .window(Minutes(5), Seconds(6)) // 5分钟 6 秒
    // sourceStream.print(1000)
    val eventLogStream: DStream[EventLog] = sourceStream.map(s => JSON.parseObject(s, classOf[EventLog]))

    val eventLogGrouped: DStream[(String, Iterable[EventLog])] = eventLogStream.map(eventLog => (eventLog.mid, eventLog)).groupByKey()

    val alertInfoStream: DStream[(Boolean, AlertInfo)] = eventLogGrouped.map {
      case (mid, eventLogs: Iterable[EventLog]) =>
        // 当前设备mid 5分钟登录过的所有用户  使用Java的集合
        val uidSet: util.HashSet[String] = new util.HashSet[String]()

        // 5分钟类所有的事件
        val eventList: util.ArrayList[String] = new util.ArrayList[String]()

        // 存储优惠券对应的产品id
        val itemSet: util.HashSet[String] = new util.HashSet[String]()

        var isClickItem = false

        breakable {
          eventLogs.foreach(log => {
            eventList.add(log.eventId)

            log.eventId match {
              case "coupon" =>
                uidSet.add(log.uid)
                itemSet.add(log.itemId)

              case "clickItem" => // 浏览商品   只要有一次浏览商品 就不应该产生预警信息
                isClickItem = true
                // 这儿不能写return  匿名函数里面不能使用return 退出的是最外层的函数
                break

              case _ =>
            }
          })
        }
        (uidSet.size() > 3 && !isClickItem, AlertInfo(mid, uidSet, itemSet, eventList, System.currentTimeMillis()))
    }

    // 写数据到es
    alertInfoStream.filter(_._1).map(_._2)
      .foreachRDD(rdd => {
        rdd.foreachPartition(alterInfos => {


          //连接es 写数据  关闭连接

          val data = alterInfos.map(info => (info.mid + info.ts / 1000 / 60, info))


          // 一分钟一次 并且mid不一样
          ESUtil.insertBulk_02(Constant.INDEX_ALERT, data)


        })
      })


    ssc.start()
    ssc.awaitTermination()

  }

}

/*
需求：同一设备，5分钟内三次及以上用不同账号登录并领取优惠劵，
并且在登录到领劵过程中没有浏览商品。
同时达到以上要求则产生一条预警日志。
同一设备，每分钟只记录一次预警。

1 同一设备 id分组
2 5分钟内 窗口 长度  滑动步长6s
3 登录用户数 聚合
4 只看有优惠券的数据
5 检测登录过是否浏览商品
6 交给ES实现


 */
