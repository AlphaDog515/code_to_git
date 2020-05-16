package com.gmall.canal.course

import java.net.InetSocketAddress
import java.util

import com.alibaba.otter.canal.client.{CanalConnector, CanalConnectors}
import com.alibaba.otter.canal.protocol.{CanalEntry, Message}

import scala.collection.JavaConversions._

object Test05 {

  def main(args: Array[String]): Unit = {

    val address = new InetSocketAddress("hadoop102",11111)
    val connector: CanalConnector = CanalConnectors.newSingleConnector(address, "example", "", "")

    connector.connect()
    connector.subscribe("gmall.*")

    while (true){
      val msg: Message = connector.get(100)
      val entriesOP: Option[util.List[CanalEntry.Entry]] = if (msg != null) Some(msg.getEntries) else None

      if(entriesOP.isDefined && entriesOP.get.nonEmpty){
        val entries: util.List[CanalEntry.Entry] = entriesOP.get

      }
    }
  }


}
