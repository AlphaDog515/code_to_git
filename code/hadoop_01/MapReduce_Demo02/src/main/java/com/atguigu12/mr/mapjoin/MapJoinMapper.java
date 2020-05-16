package com.atguigu12.mr.mapjoin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.atguigu12.mr.mapjoin.JoinBean;

/*
 * 1. 在Hadoop中，hadoop为MR提供了分布式缓存
 * 		①用来缓存一些Job运行期间的需要的文件(普通文件，jar，归档文件(har))
 * 		②通过在Job的Configuration中，使用uri代替要缓存的文件
 * 		③分布式缓存会假设当前的文件已经上传到了HDFS，并且在集群的任意一台机器都可以访问到这个URI所代表的文件
 * 		④分布式缓存会在每个节点的task运行之前，提前将文件发送到节点
 * 		⑤分布式缓存的高效是由于每个Job只会复制一次文件，且可以自动在从节点对归档文件解归档
 
 */
public class MapJoinMapper extends Mapper<LongWritable, Text, JoinBean, NullWritable> {

	private JoinBean out_key = new JoinBean();
	private Map<String, String> pdDatas = new HashMap<String, String>();
	// 在map之前手动读取pd.txt中的内容

	@Override
	protected void setup(Mapper<LongWritable, Text, JoinBean, NullWritable>.Context context)
			throws IOException, InterruptedException {

		// 从分布式缓存中读取数据
		URI[] files = context.getCacheFiles();
		for (URI uri : files) {
			BufferedReader reader = new BufferedReader(new FileReader(new File(uri)));
			String line = "";
			// 循环读取pd.txt中的每一行 isBlank 多个空格返回true
			while (StringUtils.isNotBlank(line = reader.readLine())) {
				String[] words = line.split("\t");
				pdDatas.put(words[0], words[1]);
			}
			reader.close();
		}
	}

	// 对切片中order.txt的数据进行join，输出
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, JoinBean, NullWritable>.Context context)
			throws IOException, InterruptedException {

		String[] words = value.toString().split("\t");
		out_key.setOrderId(words[0]);
		out_key.setPname(pdDatas.get(words[1]));
		out_key.setAmount(words[2]);
		context.write(out_key, NullWritable.get());
	}

}
