package com.analysis.hotitem

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.io.Source


object KafkaProducerUtil {


  def main(args: Array[String]): Unit = {

    writeToKafkaWithTopic("hot_items")

  }

  def writeToKafkaWithTopic(topic: String): Unit = {
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "hadoop103:9092")
    properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](properties)

    val path = getClass.getClassLoader.getResource("UserBehavior.csv").getPath
    val sourceIt: Iterator[String] = Source.fromFile(path).getLines()
    sourceIt.foreach(line => {
      val record = new ProducerRecord[String, String](topic, line)
      producer.send(record)
    })

    producer.close()

  }
}
