package com.atguigu10.mr.customif;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class CustomIFDriver {

	public static void main(String[] args) throws Exception {

		Path inputPath = new Path("D:\\testdata\\mrinput\\custom");
		Path outputPath = new Path("D:\\testdata\\mroutput\\custom");

		// 作为整个Job的配置
		Configuration conf = new Configuration();
		// 保证输出目录不存在
		FileSystem fs = FileSystem.get(conf);

		if (fs.exists(outputPath)) {

			fs.delete(outputPath, true);

		}

		// ①创建Job
		Job job = Job.getInstance(conf);

		job.setJarByClass(CustomIFDriver.class);

		// 为Job创建一个名字
		job.setJobName("wordcount");

		// ②设置Job
		// 设置Job运行的Mapper，Reducer类型，Mapper,Reducer输出的key-value类型
		job.setMapperClass(CustomIFMapper.class);
		job.setReducerClass(CustomIFReducer.class);

		// Job需要根据Mapper和Reducer输出的Key-value类型准备序列化器，通过序列化器对输出的key-value进行序列化和反序列化
		// 如果Mapper和Reducer输出的Key-value类型一致，直接设置Job最终的输出类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(BytesWritable.class);

		// 设置输入目录和输出目录
		FileInputFormat.setInputPaths(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputPath);

		// 设置输入和输出格式
		job.setInputFormatClass(MyInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		// ③运行Job
		job.waitForCompletion(true);

	}

}
