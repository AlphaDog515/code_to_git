package com.atguigu08.mr.flowbean.sort3;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 *13470253144	180	180	360
 * 
 * 封装流量到Bean
 * 
 * 
 */
public class FlowBeanMapper extends Mapper<LongWritable, Text, FlowBean, Text> {

	private FlowBean out_key = new FlowBean();
	private Text out_value = new Text();

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String[] words = value.toString().split("\t");

		// 封装总流量为key
		out_key.setUpFlow(Long.parseLong(words[1]));
		out_key.setDownFlow(Long.parseLong(words[2]));
		out_key.setSumFlow(Long.parseLong(words[3]));

		out_value.set(words[0]);

		context.write(out_key, out_value);

	}

}
