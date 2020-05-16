package com.atguigu13.mr.example1;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/*
 * 1.输入
 * 		atguigu-a.txt,1
 * 		atguigu-a.txt,1
 * 		atguigu-a.txt,1
 * 2.输出
 * 		atguigu-a.txt,3
 */
public class Example1Reducer1 extends Reducer<Text, IntWritable, Text, IntWritable> {

	private IntWritable out_value = new IntWritable();

	@Override
	protected void reduce(Text key, Iterable<IntWritable> values,
			Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {

		int sum = 0;
		for (IntWritable value : values) {
			sum += value.get(); // IntWritable => int
		}
		out_value.set(sum);
		context.write(key, out_value);
	}

}
