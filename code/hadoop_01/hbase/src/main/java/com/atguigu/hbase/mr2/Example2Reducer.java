package com.atguigu.hbase.mr2;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.Text;

/*
 * 1. Reducer输出的类型必须是Mutation，是固定的！
 * 			Mutation是所有 写数据类型的父类！
 * 
 * Reducer的任务就是讲Mapper输出的所有记录写出
 * 
 * public abstract class TableReducer<KEYIN, VALUEIN, KEYOUT>
extends Reducer<KEYIN, VALUEIN, KEYOUT, Mutation> {
}
 */
public class Example2Reducer extends TableReducer<Text, Put, Text>{
	
	

}
