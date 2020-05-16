package com.atguigu13.mr.example1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/*
 * 1. Example1Driver 提交两个Job
 * 			Job2 必须 依赖于 Job1，必须在Job1已经运行完成之后，生成结果后，才能运行！
 * 
 * 2. JobControl: 定义一组MR jobs，还可以指定其依赖关系
 * 				可以通过addJob(ControlledJob aJob)向一个JobControl中添加Job对象！
 * 
 * 3. ControlledJob: 可以指定依赖关系的Job对象
 * 			addDependingJob(ControlledJob dependingJob): 为当前Job添加依赖的Job
 * 			public ControlledJob(Configuration conf) : 基于配置构建一个ControlledJob
 * 
 */
public class Example1Driver {

	public static void main(String[] args) throws Exception {

		// 定义路径
		Path inputPath = new Path("e:/mrinput/index");
		Path outputPath = new Path("e:/mroutput/index");
		Path finalOutputPath = new Path("e:/mroutput/finalindex");

		// 作为整个Job的配置
		Configuration conf1 = new Configuration();
		Configuration conf2 = new Configuration();
		// 设置分隔符
		conf2.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", "-");

		// 保证输出目录不存在
		FileSystem fs = FileSystem.get(conf1);
		if (fs.exists(outputPath)) {
			fs.delete(outputPath, true);
		}

		if (fs.exists(finalOutputPath)) {
			fs.delete(finalOutputPath, true);
		}

		// ①创建Job
		Job job1 = Job.getInstance(conf1);
		Job job2 = Job.getInstance(conf2);

		// 设置Job名称
		job1.setJobName("index1");
		job2.setJobName("index2");

		// ②设置Job1
		job1.setMapperClass(Example1Mapper1.class);
		job1.setReducerClass(Example1Reducer1.class);

		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(IntWritable.class);

		// 设置输入目录和输出目录
		FileInputFormat.setInputPaths(job1, inputPath);
		FileOutputFormat.setOutputPath(job1, outputPath);

		// ②设置Job2
		job2.setMapperClass(Example1Mapper2.class);
		job2.setReducerClass(Example1Reducer2.class);

		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);

		// 设置输入目录和输出目录
		FileInputFormat.setInputPaths(job2, outputPath);
		FileOutputFormat.setOutputPath(job2, finalOutputPath);

		// 设置job2的输入格式
		job2.setInputFormatClass(KeyValueTextInputFormat.class);

		// --------------------------------------------------------
		// 构建JobControl
		JobControl jobControl = new JobControl("index");

		// 创建运行的Job
		ControlledJob controlledJob1 = new ControlledJob(job1.getConfiguration());
		ControlledJob controlledJob2 = new ControlledJob(job2.getConfiguration());
		// 指定依赖关系
		controlledJob2.addDependingJob(controlledJob1);

		// 向jobControl设置要运行哪些job
		jobControl.addJob(controlledJob1);
		jobControl.addJob(controlledJob2);

		// 运行JobControl
		Thread jobControlThread = new Thread(jobControl);
		// 设置此线程为守护线程
		jobControlThread.setDaemon(true);
		jobControlThread.start();

		// 获取JobControl线程的运行状态
		while (true) {
			// 判断整个jobControl是否全部运行结束
			if (jobControl.allFinished()) {
				System.out.println(jobControl.getSuccessfulJobList());
				return;
			}
		}

	}

}
