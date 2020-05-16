package com.atguigu15.mr.example3;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/*
 * keyin-valuein : （友:用户）
					(B:A),(C:A),(D:A).....
	reduce(): 	
	keyout-valueout  ：（友：用户，用户，用户，用户）
 */
public class Example3Reducer extends Reducer<Text, Text, Text, Text> {

	private Text out_value = new Text();

	@Override
	protected void reduce(Text key, Iterable<Text> value, Reducer<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {

		StringBuffer sb = new StringBuffer();

		for (Text text : value) {

			sb.append(text.toString() + ",");
		}

		out_value.set(sb.toString());

		context.write(key, out_value); // 【友\t用户1，用户2...】

	}

}
