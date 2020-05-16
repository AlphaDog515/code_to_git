package com.atguigu.hive.etl;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ETLMapper extends Mapper<LongWritable, Text, String, NullWritable> {

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, String, NullWritable>.Context context)
			throws IOException, InterruptedException {

		String result = ETLUtil.parseString(value.toString());

		if (result == null) {
			return;
		}
		context.write(result, NullWritable.get());

	}

}
