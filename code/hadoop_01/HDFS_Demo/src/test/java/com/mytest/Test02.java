package com.mytest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Test02 {
	private FileSystem fs;
	private FileSystem localFs;
	private Configuration conf = new Configuration();
	
	@Before
	public void init() throws IOException, URISyntaxException {
		fs = FileSystem.get(new URI("hdfs://hadoop102:9000"),conf);
		localFs = FileSystem.get(new Configuration());
	}
	@After
	public void close() throws IOException {
		if(fs != null) {
			fs.close();
		}
	}
	
	@Test
	public void testCustomUpload() throws IOException {
		Path src = new Path("d:/temp.txt");
		Path dest = new Path("d:/temp.txt");
		FSDataInputStream is = localFs.open(src);
		FSDataOutputStream os = fs.create(dest,true);
		byte[] buffer = new byte[1024];
		for(int i = 0;i<1024*10;i++) {
			is.read(buffer);
			os.write(buffer);
		}
		IOUtils.closeStream(is);
		IOUtils.closeStream(os);
	}
	
	@Test
	public void testFirstBlock() throws IOException {
		Path src = new Path("/sts.zip");
		Path dest = new Path ("d:/firstblock");
		FSDataInputStream is = fs.open(src);
		FSDataOutputStream os = localFs.create(dest,true);
		byte[] buffer = new byte[1024];
		for(int i = 0;i<1024*128;i++) {
			is.read(buffer);
			os.write(buffer);
		}
		IOUtils.closeStream(is);
		IOUtils.closeStream(os);
		
	}
	
	@Test
	public void testSecondBlock() throws IOException {
		Path src = new Path("/sts.zip");
		Path dest = new Path ("e:/thirdblock");
		FSDataInputStream is = fs.open(src);
		FSDataOutputStream os  = localFs.create(dest,true);
		is.seek(1024*1024*128*2);
		byte[] buffer = new byte[1024];
		for(int i= 0;i<1024*128;i++) {
			is.read(buffer);
			os.write(buffer);
		}
		IOUtils.closeStream(is);
		IOUtils.closeStream(os);
	}
	
	@Test
	public void testFinalBlock() throws IOException {
		Path src = new Path("/src.zip");
		Path dest = new Path("e:/fourthblock");
		FSDataInputStream is = fs.open(src);
		FSDataOutputStream os = localFs.create(dest,true);
		is.seek(1024*1024*1024*3);
		IOUtils.copyBytes(is,os,4096,true);
	}
}
