package com.atguigu13.mr.example1;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/*
 * 1.输入
 * 		atguigu,a.txt\t3
 * 		atguigu,b.txt\t3
 * 		
 * 2.输出
 * 		atguigu,a.txt\t3 b.txt\t3
 * 		
 */
public class Example1Reducer2 extends Reducer<Text, Text, Text, Text> {

	private Text out_value = new Text();

	@Override
	protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {

		StringBuffer sb = new StringBuffer();
		// 拼接value
		for (Text value : values) {
			sb.append(value.toString() + " ");
		}
		out_value.set(sb.toString());
		context.write(key, out_value);
	}
}
