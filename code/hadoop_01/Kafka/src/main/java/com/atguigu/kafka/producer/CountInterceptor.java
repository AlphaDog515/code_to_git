package com.atguigu.kafka.producer;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class CountInterceptor implements ProducerInterceptor<Integer, String> {

	private int successCount;
	private int faildCount;

	// 从Producer的配置中读取配置
	@Override
	public void configure(Map<String, ?> configs) {

	}

	// 拦截处理
	@Override
	public ProducerRecord<Integer, String> onSend(ProducerRecord<Integer, String> record) {

		return record;
	}

	// 在record已经被server发送了ack确认消息时调用
	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

		// 没有异常，说明发送成功
		if (exception == null) {
			successCount++;
		} else {
			faildCount++;
		}

	}

	// Producer关闭时调用
	@Override
	public void close() {

		System.out.println("success:" + successCount);
		System.out.println("faild:" + faildCount);

	}

}
