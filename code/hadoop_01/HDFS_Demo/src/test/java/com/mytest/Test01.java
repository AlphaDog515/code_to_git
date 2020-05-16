package com.mytest;

import java.io.FileNotFoundException;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Test01 {
	
	private FileSystem fs;
	private Configuration conf = new Configuration();
	
	@Before
	public void init() throws IOException, URISyntaxException {
		fs = FileSystem.get(new URI("hdfs://hadoop102:9000"),conf);
	}
	@After
	public void close() throws IOException {
		if(fs != null) {
			fs.close();
		}
	}

	@Test
	public void test01() throws IllegalArgumentException, IOException {
		fs.mkdirs(new Path("/demo/test1"));
	}
	
	@Test
	public void testUpload() throws IllegalArgumentException, IOException {
		fs.copyFromLocalFile(false, true, new Path("d:/temp.txt"), new Path("/"));
	}
	
	@Test
	public void testDownload() throws IllegalArgumentException, IOException {
		fs.copyToLocalFile(false,new Path("/wcinput"),new Path("d:/"),true);
	}
	
	@Test
	public void testDelete() throws IllegalArgumentException, IOException {
		fs.delete(new Path("/demo/test1"),true);
	}
	
	@Test
	public void testRename () throws IllegalArgumentException, IOException {
		fs.rename(new Path("/demo"), new Path("/cc"));
	}
	
	
	@Test
	public void testIfPathExists() throws IllegalArgumentException, IOException {
		System.out.println(fs.exists(new Path("/demo")));
	}
	
	
	@Test
	public void testFileIsDir() throws IOException {
		Path path = new Path("/wc");
		System.out.println(fs.isDirectory(path));
		System.out.println(fs.isFile(path));
		FileStatus fileStatus = fs.getFileStatus(path);
		
		FileStatus[] listStatus = fs.listStatus(path);
		for(FileStatus fsa : listStatus) {
			Path filePath = fsa.getPath();
			System.out.println(filePath.getName() + fsa.isDirectory());
			System.out.println(filePath.getName() + fsa.isFile());
		}
	}
	
	@Test
	public void testGetBlock() throws FileNotFoundException, IOException {
		Path path  = new Path ("/sts.zip");
		RemoteIterator<LocatedFileStatus> status = fs.listLocatedStatus(path);
		while (status.hasNext()) {
			LocatedFileStatus locatedFileStatus = status.next();
			System.out.println(locatedFileStatus.getOwner());
			System.out.println(locatedFileStatus.getGroup());
			BlockLocation[] blockLocations = locatedFileStatus.getBlockLocations();
			for(BlockLocation blockLocation : blockLocations) {
				System.out.println(blockLocation);
				System.out.println("-----------");
			}
		}
	}
	
}
