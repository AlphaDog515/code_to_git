package com.atguigu.kafka.producer;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/*
 * 1. MyProducer是一个进程，在进程中创建 Producer客户端，发送消息
 * 		Producer客户端可以使用KafkaProducer
 * 
 * 2. ProducerConfig类中封装了Producer配置的所有可用参数
 * 
 * 3. 生成数据的流程（默认异步）
 * 		① 封装ProducerRecored
 * 		② 调用Producer.onsend(ProducerRecord p)
 * 		③ 调用拦截器对ProducerRecored进行拦截处理
 * 		④ 确保分区可用时，调用序列化器对key,value进行序列化
 * 		⑤ 对ProducerRecored分区，创建TopicPartition对象
 * 		⑥ 就记录收集到缓冲区，判断条件是否满足，一旦满足唤醒sender拉取数据
 * 
 * 4. 如果使用异步的方式发送数据，可以使用一个回调通知类，一旦数据被发走，此时
 * 		sender会讲数据封装给回调通知类，调用回调通知的onCompleted()方法！
 * 
 * 5. 同步发送
 * 		Producer.send().get()
 * 		
 * 6. 拦截器是在分区和序列化之前运行
 * 	    拦截器可以对ProducerRecord进行拦截处理
 */
public class MyProducer {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		// 生成者的配置信息
		Properties props = new Properties();

		// 设置这组拦截器
		ArrayList<Object> interceptors = new ArrayList<>();
		// 注意顺序先添加在前
		interceptors.add("com.atguigu.kafka.producer.TimeStampInterceptor");
		interceptors.add("com.atguigu.kafka.producer.CountInterceptor");

		// 设置自定义的拦截器
		props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);

		// 设置自定义的分区器
		props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.atguigu.kafka.producer.MyPartitioner");

		props.put("hello", "hello");
	
		// 连接的cluster的主机名和端口号
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092,hadoop103:9092,hadoop104:9092");
		
		// 生产者的ack配置
		props.put("acks", "all");
		
		// 一旦接收ack时超时重试次数
		props.put("retries", 3);
	
		// 一批数据是多大
		props.put("batch.size", 16384);
		
		// sender每间隔多少时间来拉取一次数据
		props.put("linger.ms", 100);
		
		// 缓冲区的大小
		props.put("buffer.memory", 33554432);

		// 根据ProducerRecord 的key-value类型选择合适的序列化器
		// 常用的基本数据类型的序列化器，kafka已经提供了，如果是自定义的类型，需要根据kafka的规范自定义序列化器
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer<Integer, String> producer = new KafkaProducer<>(props);

		for (int i = 0; i < 10; i++) {

			producer.send(new ProducerRecord<Integer, String>("hello2", 0, i, "atguigu" + Integer.toString(i)),
					new Callback() {

						// 在broker返回ack后，自动调用onCompletion方法
						@Override
						public void onCompletion(RecordMetadata metadata, Exception exception) {

							if (null == exception) {
								System.out.println(
										metadata.topic() + "-" + metadata.partition() + ":" + metadata.offset());
							}

						}
					});
		 
			 //System.out.println("开始发送："+i+"号记录");
			// 异步发送，使用回调
			/*producer.send(new ProducerRecord<Integer, String>("hello2",i, "atguigu"+Integer.toString(i)), new Callback() {
				
				 // 在broker返回ack后，自动调用onCompletion方法
				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					
					if (null ==exception) {
						System.out.println(metadata.topic()+"-"+metadata.partition()+":"+metadata.offset());
					}
					
				}
			}).get();
		 }*/
		 
		}
		producer.close();
	}

}
