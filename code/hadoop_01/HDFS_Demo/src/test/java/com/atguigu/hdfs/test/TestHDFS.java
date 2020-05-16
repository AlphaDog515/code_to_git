package com.atguigu.hdfs.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

/*
  1. FileSystem: 文件系统的抽象基类
  	 FileSystem的实现取决于fs.defaultFS的配置！  				
 		有两种实现！
        LocalFileSystem： 本地文件系统   fs.defaultFS=file:///
        DistributedFileSystem： 分布式文件系统  fs.defaultFS=hdfs://xxx:9000
     
    声明用户身份：
     	 FileSystem fs = FileSystem.get(new URI("hdfs://hadoop101:9000"), conf, "atguigu");
      
   2. Configuration : 功能是读取配置文件中的参数   		
		Configuration在读取配置文件的参数时，根据文件名，从类路径按照顺序读取配置文件！
				先读取 xxx-default.xml，再读取xxx-site.xml
				
		Configuration类一加载，就会默认读取8个配置文件！
		将8个配置文件中所有属性，读取到一个Map集合中！		
		也提供了set(name,value)，来手动设置用户自定义的参数！
  			
 */
public class TestHDFS {

	// hadoop fs(运行一个通用的用户客户端) -mkdir /xxx
	// 创建一个客户端对象 ，调用创建目录的方法，路径作为方法的参数掺入
	@Test
	public void testMkdir() throws IOException, InterruptedException, URISyntaxException {

		Configuration conf = new Configuration();

		// 创建一个客户端对象
		// FileSystem fs=FileSystem.get(new URI("hdfs://hadoop102:9000"),conf);

		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:9000"), conf, "atguigu");
		
		

		System.out.println(fs.getClass().getName());

		fs.mkdirs(new Path("/demo/demo1"));

		fs.close();

	}

}
