package com.gmall.canal

import java.net.InetSocketAddress
import java.util

import com.alibaba.fastjson.JSONObject
import com.alibaba.otter.canal.client.CanalConnectors
import com.alibaba.otter.canal.protocol.CanalEntry.{EntryType, EventType, RowChange}
import com.alibaba.otter.canal.protocol.{CanalEntry, Message}
import com.gmall.common.Constant
import com.google.protobuf.ByteString

import scala.collection.JavaConversions._

object CanalClient {


  // 连接Canal 读数据 解析
  def main(args: Array[String]): Unit = {

    // inetsocketaddress对主机的封装
    val address: InetSocketAddress = new InetSocketAddress("hadoop103", 11111)

    val connector = CanalConnectors.newSingleConnector(address, "example", "", "")

    connector.connect
    connector.subscribe("gmall.*") // 不是正则 订阅gmall下的所有表

    while (true) {
      val msg: Message = connector.get(100) // 一次最多拉取100条 多条sql引起数据的变化
      val entriesOption = if (msg != null) Some(msg.getEntries) else None // 一个entry封装了一条SQL变化的结果

      if (entriesOption.isDefined && entriesOption.get.nonEmpty) {
        val entries: util.List[CanalEntry.Entry] = entriesOption.get
        for (entry <- entries) {
          if (entry != null && entry.hasEntryType && entry.getEntryType == EntryType.ROWDATA) {
            val stortValue: ByteString = entry.getStoreValue
            val rowChange: RowChange = RowChange.parseFrom(stortValue)
            val rowDatas: util.List[CanalEntry.RowData] = rowChange.getRowDatasList

            handleData(entry.getHeader.getTableName, rowDatas, rowChange.getEventType)

          }

        }

      } else {
        println("没有数据，2秒以后拉取数据")
        Thread.sleep(2000)
      }

    }

  }

  def handleData(tableName: String,
                 rowDatas: util.List[CanalEntry.RowData],
                 eventType: CanalEntry.EventType) = {


    if ("order_info" == tableName && eventType == EventType.INSERT && rowDatas != null && rowDatas.nonEmpty) {
      sendToKafka(Constant.TOPIC_ORDER_INFO, rowDatas)
    } else if ("order_detail" == tableName && eventType == EventType.INSERT && rowDatas != null && rowDatas.nonEmpty) {
      sendToKafka(Constant.TOPIC_ORDER_DETAIL, rowDatas)
    }
  }


  private def sendToKafka(topic: String, rowDatas: util.List[CanalEntry.RowData]): Unit = {
    for (rowData <- rowDatas) {
      val result: JSONObject = new JSONObject()
      val columnsList: util.List[CanalEntry.Column] = rowData.getAfterColumnsList
      for (column <- columnsList) {
        val key: String = column.getName
        val value: String = column.getValue
        result.put(key, value)
      }
      println(result.toJSONString)
      MyKafkaUtil.send(topic, result.toJSONString)
    }
  }
}
