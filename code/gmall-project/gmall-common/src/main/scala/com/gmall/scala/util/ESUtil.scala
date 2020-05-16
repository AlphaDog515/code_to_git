package com.gmall.scala.util

import io.searchbox.client.{JestClient, JestClientFactory}
import io.searchbox.client.config.HttpClientConfig
import io.searchbox.core.{Bulk, Index}

object ESUtil {

  val esUrl = "http://hadoop102:9200"

  val factory: JestClientFactory = new JestClientFactory

  val conf = new HttpClientConfig.Builder(esUrl)
    .maxTotalConnection(100)
    .connTimeout(10 * 1000) // 连接时间
    .readTimeout(10 * 1000) // 读取数据的超时时间
    .build()

  factory.setHttpClientConfig(conf)


  // 向es中插入单条数据
  def insertSingle(index: String, source: Object, id: String = null) = {
    val client: JestClient = factory.getObject
    val action = new Index.Builder(source)
      .index(index)
      .`type`("_doc")
      .id(id)
      .build()
    client.execute(action)
    client.shutdownClient()
  }


  def insertBulk(index: String, sources: Iterator[Any]) = {
    val client: JestClient = factory.getObject

    val bulk = new Bulk.Builder()
      .defaultIndex(index) // 多个doc应该进入同一个index中
      .defaultType("_doc")

    sources.foreach(source => {
      val action = new Index.Builder(source).build()
      bulk.addAction(action)
    })
    client.execute(bulk.build())
    client.shutdownClient();
  }

  def insertBulk_01(index: String, sources: Map[String, Any]) = {
    val client: JestClient = factory.getObject

    val bulk = new Bulk.Builder()
      .defaultIndex(index) // 多个doc应该进入同一个index中
      .defaultType("_doc")

    sources.foreach(source => {
      val action = new Index.Builder(source._2).id(source._1).build()
      bulk.addAction(action)
    })

    client.execute(bulk.build())
    client.shutdownClient();
  }


  def insertBulk_02(index: String, sources: Iterator[Any]) = {
    val client: JestClient = factory.getObject
    val bulk: Bulk.Builder = new Bulk.Builder().defaultIndex("index").defaultType("_doc")

    sources.foreach {
      case (id: String, data) =>
        val action: Index = new Index.Builder(data).id(id).build()
        bulk.addAction(action)
      case source =>
        val action = new Index.Builder(source._2).id(source._1).build()
        bulk.addAction(action)
      case _ =>
    }

    client.execute(bulk.build())
    client.shutdownClient()
  }


  def main(args: Array[String]): Unit = {

    //  val data = "{\"name\":\"wangwu\",\"age\":19}"
    // insertSingle("user", data)


    // val list = User(1, "a") :: User(2, "b") :: User(3, "c") :: Nil

    val map: Map[String, User] = Map[String, User]("111" -> User(18, "zhang"), "222" -> User(28, "zhao"), "333" -> User(38, "li"))
    insertBulk_01("user", map)

  }


  def getClient = factory.getObject



}

case class User(age: Int, name: String)