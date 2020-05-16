package com.atguigu08.sort1;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FlowCountReducer extends Reducer<FlowBean, Text, Text, FlowBean> {

	@Override
	protected void reduce(FlowBean flowBean, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		long sum_upFlow = 0;
		long sum_downFlow = 0;

		
			sum_upFlow = flowBean.getUpFlow();
			sum_downFlow = flowBean.getDownFlow();

		// 2 封装对象
		FlowBean resultBean = new FlowBean(sum_upFlow, sum_downFlow);
		for (Text text : values) {
			context.write(text, resultBean);
		}

		// 3 写出
		
	}
}
