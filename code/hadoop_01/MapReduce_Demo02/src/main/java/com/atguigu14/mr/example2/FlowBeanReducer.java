package com.atguigu14.mr.example2;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FlowBeanReducer extends Reducer<LongWritable, Text, Text, LongWritable>{
	
	private int num = 0;

	// 输出记录在reduce控制
	@Override
	protected void reduce(LongWritable key, Iterable<Text> values,
			Reducer<LongWritable, Text, Text, LongWritable>.Context context) throws IOException, InterruptedException {

		int count = 0;
		// 只输出前十条
		/*for (Text value : values) {			
			if (num>=10) {
				return;
			}			
			context.write(value, key);			
			num++;			
		}*/
		
		//只输出前十条,如果第十条有并列的一并输出
		// 将前十条全部写出，如果第十条有并列的，一并将这条所在的组全部写出
		/*if (num<=9) {			
			for (Text value : values) {				
				context.write(value, key);				
				num++;				
			}			
		}*/
		
		//只输出前十名,如果第并列的一并输出
	   // 没输出一组，记1，输出前10组
		/*if (num<=9) {					
			for (Text value : values) {						
				context.write(value, key);						
			}			
			num++;
					
		}*/
		
		//只输出前十名,如果第并列名次跳号
		if (num <= 9) {
			for (Text value : values) {
				context.write(value, key);
				count++;
			}
			num += count;
		}
	}

}
