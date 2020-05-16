package com.atguigu15.mr.example3;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * keyin-valuein:  （A:B,C,D,F,E,O）
	map(): 将valuein拆分为若干好友，作为keyout写出
			将keyin作为valueout
	keyout-valueout: （友:用户）
					(B:A),(C:A),(D:A).....
 */
public class Example3Mapper1 extends Mapper<Text, Text, Text, Text> {

	private Text out_key = new Text();

	@Override
	protected void map(Text key, Text value, Mapper<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		// 使用key-value的输入格式
		String[] friends = value.toString().split(",");

		for (String friend : friends) {

			out_key.set(friend);

			context.write(out_key, key);  // 【友\t用户】

		}

	}

}
