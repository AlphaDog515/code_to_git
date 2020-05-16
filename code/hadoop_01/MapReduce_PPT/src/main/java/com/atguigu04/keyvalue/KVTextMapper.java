package com.atguigu04.keyvalue;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class KVTextMapper extends Mapper<Text, Text, Text, LongWritable> {

	// 每一行第一个单词相同的行数
	// 1 设置value
	LongWritable v = new LongWritable(1);

	@Override
	protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {

		// banzhang ni hao

		// 2 写出
		context.write(key, v);
	}
}
