package com.atguigu13.mr.example1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * 1.输入
 * 		atguigu-a.txt\t3
 * 		atguigu-b.txt\t3
 * 		使用KeyValueTextInputFormat,可以使用一个分隔符，分隔符之前的作为key，之后的作为value
 * 2.输出
 * 		atguigu,a.txt\t3
 * 		atguigu,b.txt\t3
 */
public class Example1Mapper2 extends Mapper<Text, Text, Text, Text> {

}
