package com.atguigu11.mr.reducejoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class CustomIFDriver {
	
	public static void main(String[] args) throws Exception {
		
		Path inputPath=new Path("D:\\testdata\\mrinput\\reducejoin");
		Path outputPath=new Path("D:\\testdata\\mroutput\\reducejoin");
		

		//作为整个Job的配置
		Configuration conf = new Configuration();
		//保证输出目录不存在
		FileSystem fs=FileSystem.get(conf);		
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
		job.setMapperClass(ReduceJoinMapper.class);
		job.setReducerClass(JoinBeanReducer.class);
		
		// Job需要根据Mapper和Reducer输出的Key-value类型准备序列化器，通过序列化器对输出的key-value进行序列化和反序列化
		// 如果Mapper和Reducer输出的Key-value类型一致，直接设置Job最终的输出类型
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(JoinBean.class);
		
		// 设置输入目录和输出目录
		FileInputFormat.setInputPaths(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputPath);
		
		// 设置分区器
		job.setPartitionerClass(MyPartitioner.class);
		
		//需要Join的数据量过大 order.txt 10亿，pd.txt 100w，提高MR并行运行的效率
		// Map阶段：  修改片大小，切的片多，MapTask运行就多
		// Reduce阶段：  修改ReduceTask数量
		
		job.setNumReduceTasks(3);	
		
		// ③运行Job
		job.waitForCompletion(true);		
	}
}
