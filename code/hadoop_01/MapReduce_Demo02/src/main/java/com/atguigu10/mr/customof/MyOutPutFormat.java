package com.atguigu10.mr.customof;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MyOutPutFormat extends FileOutputFormat<String, NullWritable> {

	@Override
	public RecordWriter<String, NullWritable> getRecordWriter(TaskAttemptContext job)
			throws IOException, InterruptedException {
		return new MyRecordWriter(job);
	}

}
