package com.atguigu.kafka.producer;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

public class MyPartitioner implements Partitioner {

	// 从生产者的配置中读取参数
	@Override
	public void configure(Map<String, ?> configs) {
		System.out.println(configs.get("hello"));

	}

	// 为record生成一个分区号
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

		// 先去查询当前可用的分区一共有几个
		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
		int numPartitions = partitions.size();

		return (key.hashCode() & Integer.MAX_VALUE) % numPartitions;
	}

	// Producer.close()后，调用生产者使用的Partitioner的close()
	@Override
	public void close() {

	}

}
