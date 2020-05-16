package com.gmall.canal.course

import java.net.InetSocketAddress
import java.util
import java.util.Properties

import com.alibaba.fastjson.JSONObject

import scala.collection.JavaConversions._
import com.alibaba.otter.canal.client.{CanalConnector, CanalConnectors}
import com.alibaba.otter.canal.protocol.CanalEntry.{EntryType, EventType, RowChange, RowData}
import com.alibaba.otter.canal.protocol.{CanalEntry, Message}
import com.gmall.common.Constant
import com.google.protobuf.ByteString
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object Test03 {


  def main(args: Array[String]): Unit = {

    val address: InetSocketAddress = new InetSocketAddress("hadoop103", 11111)
    val connector: CanalConnector = CanalConnectors.newSingleConnector(address, "example", "", "")
    connector.connect()
    connector.subscribe("gmall.*")

    while(true){
      val msg: Message = connector.get(100)
      val entriesOP: Option[util.List[CanalEntry.Entry]] = if (msg != null) Some(msg.getEntries) else None
      if(entriesOP.isDefined && entriesOP.get.nonEmpty){
        val entries: util.List[CanalEntry.Entry] = entriesOP.get
        for (entry <- entries){
          if(entry != null && entry.hasEntryType && entry.getEntryType == EntryType.ROWDATA ){
            val storeValue  : ByteString = entry.getStoreValue
            val rowChange  = RowChange.parseFrom(storeValue)
            val rowDataList: util.List[CanalEntry.RowData] = rowChange.getRowDatasList

            handlerRowData(entry.getHeader.getTableName,rowDataList,rowChange.getEventType)


          }

        }


      }else{
        println("现在没有数据，等2秒！")
        Thread.sleep(2000)
      }

    }
  }

  def handlerRowData(tableName: String,
                     rowDataList: util.List[CanalEntry.RowData],
                     eventType: CanalEntry.EventType) = {

    if("order_info".equals(tableName) && eventType == EventType.INSERT && rowDataList != null && rowDataList.nonEmpty){

      for (rowData <- rowDataList){
        val row: util.List[CanalEntry.Column] = rowData.getAfterColumnsList
        val res: JSONObject = new JSONObject()
        for (col <- row){
          val key: String = col.getName
          val value: String = col.getValue
          res.put(key,value)
        }

        val props: Properties = new Properties()
        props.put("bootstrap.servers","")
        props.put("","")
        props.put("","")
        val producer: KafkaProducer[String, String] = new KafkaProducer[String, String]( props)

        producer.send(new ProducerRecord[String,String]("",res.toJSONString))

      }

    }


  }

}
