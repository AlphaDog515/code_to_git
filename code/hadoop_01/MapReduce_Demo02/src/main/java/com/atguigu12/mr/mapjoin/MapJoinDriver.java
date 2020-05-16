package com.atguigu12.mr.mapjoin;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class MapJoinDriver {
	
	public static void main(String[] args) throws Exception {
		
		Path inputPath=new Path("e:/mrinput/mapjoin");
		Path outputPath=new Path("e:/mroutput/mapjoin");
		

		//作为整个Job的配置
		Configuration conf = new Configuration();
		//保证输出目录不存在
		FileSystem fs=FileSystem.get(conf);
		
		if (fs.exists(outputPath)) {
			
			fs.delete(outputPath, true);
			
		}
		
		// ①创建Job
		Job job = Job.getInstance(conf);
		
		job.setJarByClass(MapJoinDriver.class);
		
		
		// 为Job创建一个名字
		job.setJobName("wordcount");
		
		// ②设置Job
		// 设置Job运行的Mapper，Reducer类型，Mapper,Reducer输出的key-value类型
		job.setMapperClass(MapJoinMapper.class);
		
		// 设置输入目录和输出目录
		FileInputFormat.setInputPaths(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputPath);
		
		// 设置分布式缓存
		job.addCacheFile(new URI("file:///e:/pd.txt"));
		
		//取消reduce阶段
		job.setNumReduceTasks(0);

		// ③运行Job
		job.waitForCompletion(true);
		
		
	}

}
