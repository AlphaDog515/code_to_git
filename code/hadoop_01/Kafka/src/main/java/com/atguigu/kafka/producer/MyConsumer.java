package com.atguigu.kafka.producer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/*
 * 1.	MyConsumer是一个进程，从这个进程上创建一个消费者，消费分区中的数据
 * 
 * 2.	ConsumerConfig
 * 
 * 3.	当设置为自动提交offset时，此时如果poll()已经消费了数据，
 * 	  	那么间隔若干秒后，系统会自动提交offset!
 * 	  	此时，如果之前poll()在执行消费逻辑时，报错，可能会有部分数据丢失！
 * 
 * 	 	解决： 采取手动提交，在执行了消费处理逻辑之后，手动提交！
 * 
 * 4. 	手动提交会有什么问题？
 * 			如果在提交offset之前，已经有部分数据已经完成，但是同一批次的其他数据在处理是出现异常！
 * 			那么，下次启动consumer，由于没有提交上次处理的offset，会造成部分已经处理完的数据重复处理！
 * 
 * 			解决： 事务
 * 
 * 5. 	Spring-kafka
 * 
 * 6. 	--from-beginning * 
 * 	
 *     public static final String AUTO_OFFSET_RESET_DOC = 
 *     "What to do when there is no initial offset in Kafka or 
 *     if the current offset does not exist any more on the server 
 *     (e.g. because that data has been deleted): <ul><li>earliest:
 *      automatically reset the offset to the earliest offset<li>latest: 
 *      automatically reset the offset to the latest offset</li><li>none:
 *       throw exception to the consumer if no previous offset is 
 *       found for the consumer's group</li><li>anything else: 
 *       throw exception to the consumer.</li></ul>";
 *       
 * 7. 	消费某个特定的分区，从特定的offset开始消费，需要创建一个独立消费者
 * 
 */
public class MyConsumer {

	public static void main(String[] args) {
		// kafkaConsumer的配置
		Properties props = new Properties();

		// --from-beginning 从头消费
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092");
	
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

		// 创建非独立消费者，只告诉订阅哪个主题，由kafka自动分配分区
		consumer.subscribe(Arrays.asList("hello2"));

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
