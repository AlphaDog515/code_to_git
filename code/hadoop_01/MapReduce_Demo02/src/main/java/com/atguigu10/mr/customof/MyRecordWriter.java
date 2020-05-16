package com.atguigu10.mr.customof;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class MyRecordWriter extends RecordWriter<String, NullWritable> {

	private Path atguiguPath = new Path("D:\\testdata\\mroutput\\outputformat\\atguigu.log");
	private Path otherPath = new Path("D:\\testdata\\mroutput\\outputformat\\other.log");

	private FSDataOutputStream atguguOS;
	private FSDataOutputStream otherOS;

	private FileSystem fs;

	private TaskAttemptContext context;

	public MyRecordWriter(TaskAttemptContext job) throws IOException {

		context = job;

		Configuration conf = job.getConfiguration();

		fs = FileSystem.get(conf);

		atguguOS = fs.create(atguiguPath);
		otherOS = fs.create(otherPath);
	}

	// 负责将key-value写出到文件
	@Override
	public void write(String key, NullWritable value) throws IOException, InterruptedException {

		if (key.contains("atguigu")) {

			atguguOS.write(key.getBytes());

			context.getCounter("MyCounter", "atguiguCounter").increment(1);
			// 统计输出的含有atguigu 的key-value个数

		} else {

			otherOS.write(key.getBytes());

			context.getCounter("MyCounter", "otherCounter").increment(1);

		}

	}

	// 关闭操作
	@Override
	public void close(TaskAttemptContext context) throws IOException, InterruptedException {

		if (atguguOS != null) {
			IOUtils.closeStream(atguguOS);
		}

		if (otherOS != null) {
			IOUtils.closeStream(otherOS);

		}

		if (fs != null) {
			fs.close();
		}

	}

}
