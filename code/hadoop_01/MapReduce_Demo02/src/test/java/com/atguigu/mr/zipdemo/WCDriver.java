package com.atguigu.mr.zipdemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WCDriver {

	// 1049908 265933(deflate) 273665(bzip2)
	public static void main(String[] args) throws Exception {

		Path inputPath = new Path("e:/mrinput/wordcount");
		Path outputPath = new Path("e:/mroutput/wordcount");

		Configuration conf = new Configuration();

		// 在shuffle阶段使用压缩
		conf.set("mapreduce.map.output.compress", "true");
		conf.set("mapreduce.map.output.compress.codec", "org.apache.hadoop.io.compress.BZip2Codec");

		// 指定reduce输出采取压缩 418(无) 128(压)
		conf.set("mapreduce.output.fileoutputformat.compress", "true");
		conf.set("mapreduce.job.queuename", "b");

		FileSystem fs = FileSystem.get(conf);
		if (fs.exists(outputPath)) {
			fs.delete(outputPath, true);
		}
		// ①创建Job
		Job job = Job.getInstance(conf);
		job.setJarByClass(WCDriver.class);
		job.setJobName("wordcount");
		job.setMapperClass(WCMapper.class);
		job.setReducerClass(WCReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.setInputPaths(job, inputPath);
		FileOutputFormat.setOutputPath(job, outputPath);

		// 设置combiner
		// 之前： 4796722,58610767
		// 之后： 68282， 1049908
		job.setCombinerClass(WCReducer.class);

		// ③运行Job
		job.waitForCompletion(true);

	}

}
