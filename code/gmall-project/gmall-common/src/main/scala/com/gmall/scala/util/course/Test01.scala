package com.gmall.scala.util.course

import io.searchbox.client.{JestClient, JestClientFactory}
import io.searchbox.client.config.HttpClientConfig
import io.searchbox.core.{Bulk, Index}

object Test01 {

  val esUrl = "http://hadoop102:9200"

  private val factory: JestClientFactory = new JestClientFactory

  private val conf: HttpClientConfig = new HttpClientConfig.Builder(esUrl)
    .maxTotalConnection(100)
    .connTimeout(10 * 1000)
    .readTimeout(10 * 1000)
    .build()

  factory.setHttpClientConfig(conf)


  def insertSingle(index: String, source: Object, id: String = null) = {
    val client: JestClient = factory.getObject
    val action: Index = new Index.Builder(source).index(index).`type`("_doc").id(id).build()
    client.execute(action)
    client.shutdownClient()
  }


  def insertBulk(index: String, sources: Iterator[Any]) = {
    val client: JestClient = factory.getObject
    val bulk: Bulk.Builder = new Bulk.Builder().defaultIndex(index).defaultType("_doc")

    sources.foreach(source =>{
      val action: Index = new Index.Builder(source).build()
      bulk.addAction(action)
    })

    client.execute(bulk.build())
    client.shutdownClient()
  }


  def main(args: Array[String]): Unit = {

  }

}
