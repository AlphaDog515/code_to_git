package com.atguigu.kafka.producer;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class TimeStampInterceptor implements ProducerInterceptor<Integer, String> {

	// 从Producer的配置中读取配置
	@Override
	public void configure(Map<String, ?> configs) {

	}

	// 拦截处理
	@Override
	public ProducerRecord<Integer, String> onSend(ProducerRecord<Integer, String> record) {

		String value = record.value();

		value = System.currentTimeMillis() + value;

		return new ProducerRecord<Integer, String>(record.topic(), record.partition(), record.key(), value);
	}

	// 在record已经被server发送了ack确认消息时调用
	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

	}

	// Producer关闭时调用
	@Override
	public void close() {

	}

}
