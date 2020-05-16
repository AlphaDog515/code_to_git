package com.gmall.canal.course

import java.net.InetSocketAddress
import java.util
import java.util.Properties

import com.alibaba.fastjson.JSONObject
import com.alibaba.otter.canal.client.{CanalConnector, CanalConnectors}
import com.alibaba.otter.canal.protocol.CanalEntry.{EntryType, EventType, RowChange}
import com.alibaba.otter.canal.protocol.{CanalEntry, Message}
import com.gmall.common.Constant
import com.google.protobuf.ByteString
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}

import scala.collection.JavaConversions._

object Test01 {


  def main(args: Array[String]): Unit = {

    val address = new InetSocketAddress("hadoop103", 11111)

    val connector: CanalConnector = CanalConnectors.newSingleConnector(address, "example", "", "")

    connector.connect()

    connector.subscribe("gmall1015.*")

    while (true) {
      val msg: Message = connector.get(100)
      val entriesOption: Option[util.List[CanalEntry.Entry]] = if (msg != null) Some(msg.getEntries) else None
      if (entriesOption.isDefined && entriesOption.get.nonEmpty) {

        val entries: util.List[CanalEntry.Entry] = entriesOption.get //entry一条sql
        for (entry <- entries){
          if(entry != null && entry.hasEntryType && entry.getEntryType == EntryType.ROWDATA){
            val storeValue: ByteString = entry.getStoreValue
            val change: RowChange = RowChange.parseFrom(storeValue)
            val rowDatas: util.List[CanalEntry.RowData] = change.getRowDatasList

            handleData(entry.getHeader.getTableName,rowDatas,change.getEventType)

          }
        }
      } else {
        println("没有数据，2秒后获取！")
        Thread.sleep(2000)
      }
    }

  }
  def handleData(tableName: String,
                 rowDatas: util.List[CanalEntry.RowData],
                 eventType: CanalEntry.EventType) = {

    if("order_info" == tableName && eventType == EventType.INSERT && rowDatas != null && rowDatas.nonEmpty){
      for(rowData <- rowDatas){
        val res: JSONObject = new JSONObject()

        for (col <- rowData.getAfterColumnsList){
          val key: String = col.getName
          val value: String = col.getValue
          res.put(key,value)
        }

        // 写数据到Kafka，一行一行的写
        val props: Properties = new Properties()
        props.put("bootstrap.servers","hadoop102:9092,hadoop103:9092,hadoop104:9092")
        props.put("","")
        props.put("","")

        val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](props)

        producer.send(new ProducerRecord[String,String](Constant.TOPIC_ORDER_INFO,res.toJSONString))


      }
    }

  }

}
