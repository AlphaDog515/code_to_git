package com.atguigu05.mr.flowbean.partition;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/*
 * 排序器：  排序器影响的是排序的速度(效率)， QuickSorter
 * 比较器：  比较器影响的是排序的结果
 * 
 * 
 * KEY, VALUE: Mapper输出的Key-value类型
 */
public class MyPartitioner extends Partitioner<Text, FlowBean> {

	// 计算分区 numPartitions为总的分区数，reduceTask的数量
	// 分区号必须为int型的值，且必须符合 0<= partitionNum < numPartitions
	@Override
	public int getPartition(Text key, FlowBean value, int numPartitions) {

		String suffix = key.toString().substring(0, 3);

		int partitionNum = 0;

		switch (suffix) {
		case "136":
			partitionNum = numPartitions - 1;
			break;
		case "137":
			partitionNum = numPartitions - 2;
			break;
		case "138":
			partitionNum = numPartitions - 3;
			break;
		case "139":
			partitionNum = numPartitions - 4;
			break;

		default:
			break;
		}

		return partitionNum;
	}

}
