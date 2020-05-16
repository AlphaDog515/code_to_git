package com.atguigu.hdfs.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Test;

public class TestFileStatus {
	@Test
	public void testListFiles() throws IOException, InterruptedException, URISyntaxException {
		// 1获取文件系统
		Configuration configuration = new Configuration();
		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:9000"), configuration, "atguigu");

		// 2 获取文件详情,true表示递归获取  // /demo/input/hello.txt hi.txt
		RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
		//FileStatus fileStatus = fs.getFileStatus(new Path("/"));

		while (listFiles.hasNext()) {
			LocatedFileStatus status = listFiles.next();

			// 输出详情
			// 文件名称
			System.out.println(status.getPath().getName());
			// 长度
			System.out.println(status.getLen());
			// 权限
			System.out.println(status.getPermission());
			// 分组
			System.out.println(status.getGroup());

			// 获取存储的块信息
			BlockLocation[] blockLocations = status.getBlockLocations();

			for (BlockLocation blockLocation : blockLocations) {

				// 获取块存储的主机节点
				String[] hosts = blockLocation.getHosts();

				for (String host : hosts) {
					System.out.println(host);
				}
			}

			System.out.println("-----------文件分割线----------");
		}
		// 3 关闭资源
		fs.close();
	}

	
	
	@Test
	public void testListStatus() throws IOException, InterruptedException, URISyntaxException{
			
		// 1 获取文件配置信息
		Configuration configuration = new Configuration();
		FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:9000"), configuration, "atguigu");
			
		// 2 判断是文件还是文件夹
		FileStatus[] listStatus = fs.listStatus(new Path("/demo/input"));
			
		for (FileStatus fileStatus : listStatus) {
				
			// 如果是文件
			if (fileStatus.isFile()) {
					System.out.println("f:"+fileStatus.getPath().getName());
				}else {
					System.out.println("d:"+fileStatus.getPath().getName());
				}
			}
			
		// 3 关闭资源
		fs.close();
	}

}
