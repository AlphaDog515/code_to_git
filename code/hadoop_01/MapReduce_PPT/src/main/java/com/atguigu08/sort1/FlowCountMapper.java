package com.atguigu08.sort1;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FlowCountMapper extends Mapper<LongWritable, Text, FlowBean, Text> {

	FlowBean k = new FlowBean();
	Text v = new Text();

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, FlowBean, Text>.Context context)
			throws IOException, InterruptedException {

		// 1 获取一行
		String line = value.toString();

		// 2 切割字段
		String[] fields = line.split("\t");

		// 3 封装对象
		// 取出手机号码
		String phoneNum = fields[1];
		// 取出上行流量和下行流量
		long upFlow = Long.parseLong(fields[fields.length - 3]);
		long downFlow = Long.parseLong(fields[fields.length - 2]);

		v.set(phoneNum);
		k.setUpFlow(upFlow);
		k.setDownFlow(downFlow);
		k.setSumFlow(upFlow+downFlow);
		// 4 写出
		context.write(k, v);
	}
}
