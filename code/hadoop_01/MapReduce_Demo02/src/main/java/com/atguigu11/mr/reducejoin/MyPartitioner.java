package com.atguigu11.mr.reducejoin;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/*
 * 1.保证pid相同的key-value分到同一个区
 */
public class MyPartitioner extends Partitioner<NullWritable, JoinBean> {

	@Override
	public int getPartition(NullWritable key, JoinBean value, int numPartitions) {

		return (value.getPid().hashCode() & Integer.MAX_VALUE) % numPartitions;
	}

}
