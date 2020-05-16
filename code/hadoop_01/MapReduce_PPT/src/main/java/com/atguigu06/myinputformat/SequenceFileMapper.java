package com.atguigu06.myinputformat;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

// 文件路径加名字为key  文件内容为value
public class SequenceFileMapper extends Mapper<Text, BytesWritable, Text, BytesWritable> {

	@Override
	protected void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {

		context.write(key, value);
	}
}
