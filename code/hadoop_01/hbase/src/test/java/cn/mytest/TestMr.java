package cn.mytest;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

public class TestMr {

}

class Example2Mapper extends Mapper<LongWritable, Text, Text, Put> {
	private Text out_key = new Text();

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Put>.Context context)
			throws IOException, InterruptedException {
		String[] words = value.toString().split("\t");
		out_key.set(words[0]);
		Put put = new Put(Bytes.toBytes(words[0]));
		put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("cateory"), Bytes.toBytes(words[1]));

		context.write(out_key, put);
	}
}

class Example2Reducer extends TableReducer<Text, Put, Text> {

}

class Example2Driver {
	public static void main(String[] args) throws IOException {
		Configuration conf = HBaseConfiguration.create();
		Job job = Job.getInstance(conf);
		job.setJobName("example2");
		job.setJarByClass(Example2Driver.class);

		job.setMapperClass(Example2Mapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Put.class);

		FileInputFormat.setInputPaths(job, new Path(""));

		TableMapReduceUtil.initTableReducerJob("t5", Example2Reducer.class, job);

	}
}
