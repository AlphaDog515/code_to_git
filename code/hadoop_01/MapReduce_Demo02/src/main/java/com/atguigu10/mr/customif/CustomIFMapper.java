package com.atguigu10.mr.customif;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CustomIFMapper extends Mapper<Text, BytesWritable, Text, BytesWritable> {

}
