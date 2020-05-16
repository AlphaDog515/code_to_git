package com.analysis.hotitem.course

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.scala._
import org.apache.flink.types.Row

object HotItemsSql {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val settings: EnvironmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env, settings)

    val path = getClass.getResource("/UserBehavior.csv").getPath
    val inputDS: DataStream[String] = env.readTextFile(path)

    val dataDS = inputDS
      .map(data => {
        val arr = data.split(",")
        UserBehavior(arr(0).toLong, arr(1).toLong, arr(2).toInt, arr(3), arr(4).toLong)
      })
      .assignAscendingTimestamps(_.timestamp * 1000L)

    tableEnv.createTemporaryView("data_table", dataDS, 'itemId, 'behavior, 'timestamp.rowtime as 'ts)

    val t1 = tableEnv.sqlQuery(
      """
        |select itemId,count(itemId) as cnt, hop_end(ts,interval '5' minute,interval '1' hour) as windowEnd
        |from data_table where behavior = 'pv' group by hop(ts,interval '5' minute,interval '1' hour),itemId
        |""".stripMargin)

    val t2 = tableEnv.sqlQuery("select *,row_number() over(partition by windowEnd order by cnt desc) as row_num from " + t1)

    val res = tableEnv.sqlQuery("select * from " + t2 + " where row_num <= 5")

    res.toRetractStream[Row].print("res")
    env.execute("hot item with sql")


  }

}
