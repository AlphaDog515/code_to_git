package com.analysis.hotitem.course

import java.sql.Timestamp

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.{EnvironmentSettings, Slide}
import org.apache.flink.table.api.scala._


case class UserBehavior(userId: Long, itemId: Long, categoryId: Int, behavior: String, timestamp: Long)


case class ItemViewCount(itemId: Long, windowEnd: Long, count: Long)

object HotItems {

  def main(args: Array[String]): Unit = {

    // 创建环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val settings: EnvironmentSettings = EnvironmentSettings
      .newInstance().useBlinkPlanner().inStreamingMode().build()

    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env, settings)

    val path = getClass.getResource("/UserBehavior.csv").getPath
    val inputDS: DataStream[String] = env.readTextFile(path)

    val dataDS = inputDS
      .map(data => {
        val arr = data.split(",")
        UserBehavior(arr(0).toLong, arr(1).toLong, arr(2).toInt, arr(3), arr(4).toLong)
      })
      .assignAscendingTimestamps(_.timestamp * 1000L)

    val dataTable = tableEnv.fromDataStream(dataDS, 'itemId, 'behavior, 'timestamp.rowtime as 'ts)

    val aggTable = dataTable
      .filter('behavior === "pv")
      .window(Slide over 1.hours every 5.minutes on 'ts as 'sw)
      .groupBy('sw, 'itemId)
      .select('itemId, 'itemId.count as 'cnt, 'sw.end as 'windowEnd)

    tableEnv.createTemporaryView("agg", aggTable, 'itemId, 'cnt, 'windowEnd)

    val resTable = tableEnv.sqlQuery(
      """
        |select * from
        |   (select *,row_number() over(partition by windowEnd order by cnt desc) as row_num from agg)
        |where row_num <= 5
        |""".stripMargin)

    resTable.toRetractStream[(Long, Long, Timestamp, Long)].print("res")
    env.execute("table api and sql")

  }


}
