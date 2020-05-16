package com.atguigu.hbase.mr2;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * 1. MR读取HDFS中的数据，写入到hbase表中
 * 
 * 		InputFormat无需使用TableInputFormat!
 * 
 * 
 * 
 */
public class Example2Mapper extends Mapper<LongWritable, Text, Text, Put> {

	private Text out_key = new Text();

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Put>.Context context)
			throws IOException, InterruptedException {

		String[] words = value.toString().split("\t");

		// 封装rowkey列
		out_key.set(words[0]);

		Put put = new Put(Bytes.toBytes(words[0]));

		// 列簇，列名，值
		put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("category"), Bytes.toBytes(words[1]));

		context.write(out_key, put);

	}

}
