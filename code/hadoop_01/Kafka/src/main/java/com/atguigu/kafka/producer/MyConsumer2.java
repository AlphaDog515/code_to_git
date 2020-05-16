package com.atguigu.kafka.producer;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

/*
 * 
 *       
 * 7. 消费某个特定的分区，从特定的offset开始消费，需要创建一个独立消费者

 * 
 */
public class MyConsumer2 {

	public static void main(String[] args) {
		// kafkaConsumer的配置
		Properties props = new Properties();

		// --from-beginning 从头消费
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop101:9092");
		// 消费者组id
		props.put("group.id", "test1");
		// 消费者id
		props.put("client.id", "a");
		// 是否允许自动提交offset，对于非独立消费者，如果设置了自动提交offset，此时kafka会自动讲上次消费的位置
		// 保存到 __consumer_offsets
		props.put("enable.auto.commit", "false");
		// 如果开启了自动提交offset，没间隔多少ms提交一次
		props.put("auto.commit.interval.ms", "1000");
		// 消费者的反序列化器要和生成的数据的序列化器一致
		props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(props);

		ArrayList<TopicPartition> list = new ArrayList<>();

		TopicPartition topicPartition = new TopicPartition("hello2", 0);

		// 消费哪些分区
		list.add(topicPartition);

		// 创建独立消费者，只告诉订阅分区
		consumer.assign(list);
		// 指定分区的消费位置
		consumer.seek(topicPartition, 31);

		while (true) {
			// 100为timeout，指当前buffer中没有数据时，就等待100ms
			// 消费数据
			ConsumerRecords<Integer, String> records = consumer.poll(100);

			// 用户自定义定义的消费逻辑
			for (ConsumerRecord<Integer, String> record : records) {

				System.out.printf(" offset= %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
			}

			// 手动同步提交
			// consumer.commitSync();
			// 手动异步提交
			// consumer.commitAsync();

			// 自己编写方法，这个方法可以讲拉取的offset存入到mysql中
			// myCommit()
		}

	}

}
