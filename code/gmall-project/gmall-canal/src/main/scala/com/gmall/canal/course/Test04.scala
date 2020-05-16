package com.gmall.canal.course

import java.net.InetSocketAddress
import java.util

import com.alibaba.fastjson.JSONObject
import com.alibaba.otter.canal.client.{CanalConnector, CanalConnectors}
import com.alibaba.otter.canal.protocol.CanalEntry.{EntryType, EventType, RowChange}
import com.alibaba.otter.canal.protocol.{CanalEntry, Message}
import com.gmall.canal.MyKafkaUtil
import com.google.protobuf.ByteString

import scala.collection.JavaConversions._

object Test04 {


  def main(args: Array[String]): Unit = {

    val address: InetSocketAddress = new InetSocketAddress("hadoop103", 11111)
    val connector: CanalConnector = CanalConnectors.newSingleConnector(address, "example", "", "")
    connector.connect()
    connector.subscribe("gmall1015.*")


    while (true) {
      val msg: Message = connector.get(100)

      val entriesOP: Option[util.List[CanalEntry.Entry]] = if (msg != null) Some(msg.getEntries) else None
      if (entriesOP.isDefined && entriesOP.get.nonEmpty) {

        val entries: util.List[CanalEntry.Entry] = entriesOP.get
        for (entry <- entries) {
          if (entry != null && entry.hasEntryType && entry.getEntryType == EntryType.ROWDATA) {

            val storeValue: ByteString = entry.getStoreValue
            val rowChange: RowChange = RowChange.parseFrom(storeValue)
            val rowDatas: util.List[CanalEntry.RowData] = rowChange.getRowDatasList

            handleData(entry.getHeader.getTableName, rowDatas, rowChange.getEventType)


          }


        }

      } else {
        Thread.sleep(2000)
      }


    }

  }

  def handleData(tableName: String,
                 rowDatas: util.List[CanalEntry.RowData],
                 eventType: CanalEntry.EventType) = {

    if ("order_info".equals(tableName) && eventType == EventType.INSERT && rowDatas != null && rowDatas.nonEmpty) {
      for (rowData <- rowDatas) {
        val result: JSONObject = new JSONObject()
        val list: util.List[CanalEntry.Column] = rowData.getAfterColumnsList
        for (col <- list) {
          val key: String = col.getName
          val value: String = col.getValue
          result.put(key, value)
        }
        MyKafkaUtil.send("", result.toString)
      }
    }
  }


}
