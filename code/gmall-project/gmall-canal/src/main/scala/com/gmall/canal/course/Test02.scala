package com.gmall.canal.course

import java.net.InetSocketAddress
import java.util

import com.alibaba.fastjson.JSONObject
import com.alibaba.otter.canal.client.{CanalConnector, CanalConnectors}
import com.alibaba.otter.canal.protocol.CanalEntry.{EntryType, EventType, RowChange}
import com.alibaba.otter.canal.protocol.{CanalEntry, Message}
import com.gmall.canal.MyKafkaUtil
import com.gmall.common.Constant
import com.google.protobuf.ByteString

import scala.collection.JavaConversions._

object Test02 {


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
            handleData(rowDatas, entry.getHeader.getTableName, rowChange.getEventType)
          }
        }
      } else {
        println("没有数据,2秒后获取")
        Thread.sleep(2000)
      }

    }

  }

  def handleData(rowDatas: util.List[CanalEntry.RowData],
                 tableName: String,
                 eventType: CanalEntry.EventType) = {

    if (rowDatas != null && rowDatas.nonEmpty && tableName == "order_info" && eventType == EventType.INSERT) {
      for (row <- rowDatas) {
        val res = new JSONObject()
        for (col <- row.getAfterColumnsList) {
          val key: String = col.getName
          val value: String = col.getValue
          res.put(key, value)
        }
        MyKafkaUtil.send(Constant.TOPIC_ORDER_INFO, res.toJSONString)
      }

    }

  }

}
