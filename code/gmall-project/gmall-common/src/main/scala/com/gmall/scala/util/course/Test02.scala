package com.gmall.scala.util.course

import io.searchbox.client.{JestClient, JestClientFactory}
import io.searchbox.client.config.HttpClientConfig
import io.searchbox.core.{Bulk, Index}

object Test02 {

  val esUrl = "http://hadoop102:9200"

  private val factory: JestClientFactory = new JestClientFactory

  private val config: HttpClientConfig = new HttpClientConfig.Builder(esUrl)
    .maxTotalConnection(100)
    .connTimeout(10 * 1000).readTimeout(10 * 1000)
    .multiThreaded(true)
    .build()

  factory.setHttpClientConfig(config)


  def insertBulk(index: String, sources: Iterator[Any]) = {
    val client: JestClient = factory.getObject
    val bulk: Bulk.Builder = new Bulk.Builder().defaultIndex(index).defaultType("_doc")

    sources.foreach {
      case (id: String, data) =>
        val action: Index = new Index.Builder(data).id(id).build()
        bulk.addAction(action)
      case data =>
        val action: Index = new Index.Builder(data).build()
        bulk.addAction(action)
    }

    client.execute(bulk.build())
    client.shutdownClient()
  }


  def main(args: Array[String]): Unit = {

  }

}
