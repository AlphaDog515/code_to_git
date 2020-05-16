package com.gmall.canal

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object MyKafkaUtil {

  private val props: Properties = new Properties()
  props.put("bootstrap-servers","hadoop102:9092,hadoop103:9092,hadoop104:9092")
  props.put("key-serializer","org.apache.kafka.common.serialization.StringSerializer")
  props.put("value-serializer","org.apache.kafka.common.serialization.StringSerializer")

  private val producer: KafkaProducer[String, String] = new KafkaProducer[String, String /*生产数据的kv*/ ](props)

  def send(topic: String, content: String) = {
    producer.send(new ProducerRecord[String, String](topic, content))
  }

}
