package com.atguigu06.mr.flowbean.sort1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/*
 * 1.一旦启动这个线程，运行Job
 * 
 * 2.本地模式主要用于测试程序是否正确！
 * 
 * 3. 报错：
 * 	ExitCodeException exitCode=1: /bin/bash: line 0: fg: no job control
 */
public class FlowBeanDriver {

	public static void main(String[] args) throws Exception {

		Path inputPath = new Path("D:\\testdata\\mrinput\\sort");
		Path outputPath = new Path("D:\\testdata\\mroutput\\sort1");

		// 作为整个Job的配置
		Configuration conf = new Configuration();

		// 保证输出目录不存在
		FileSystem fs = FileSystem.get(conf);

		if (fs.exists(outputPath)) {

			fs.delete(outputPath, true);

		}

		// ①创建Job
		Job job = Job.getInstance(conf);

		// ②设置Job
		// 设置Job运行的Mapper，Reducer类型，Mapper,Reducer输出的key-value类型
		job.setMapperClass(FlowBeanMapper.class);
		job.setReducerClass(FlowBeanReducer.class);

		// Job需要根据Mapper和Reducer输出的Key-value类型准备序列化器，通过序列化器对输出的key-value进行序列化和反序列化
		// 如果Mapper和Reducer输出的Key-value类型一致，直接设置Job最终的输出类型

		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		// 设置输入目录和输出目录
		FileInputFormat.setInputPaths(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputPath);

		// 设置使用自定义的比较器
		job.setSortComparatorClass(MyDescComparator.class);
		// DecreasingComparator.class 是LongWritable的静态内部类

		// ③运行Job
		job.waitForCompletion(true);

	}

}
