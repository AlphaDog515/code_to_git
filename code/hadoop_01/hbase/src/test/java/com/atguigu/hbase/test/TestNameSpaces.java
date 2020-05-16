package com.atguigu.hbase.test;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Connection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.atguigu.hbase.tools.ConnectionUtil;
import com.atguigu.hbase.tools.NameSpaceUtil;

public class TestNameSpaces {

	private Connection conn;

	@Before
	public void init() throws IOException {

		conn = ConnectionUtil.getConn();

	}

	@After
	public void close() throws IOException {

		ConnectionUtil.close(conn);

	}

	@Test
	public void testListNSs() throws IOException {

		System.out.println(NameSpaceUtil.listNameSpace(conn));

	}

	@Test
	public void testIfExistsNSs() throws IOException {

		System.out.println(NameSpaceUtil.ifNSExists(conn, "default"));

	}

	@Test
	public void testCreateNS() throws IOException {

		System.out.println(NameSpaceUtil.creatNS(conn, "ns1"));

	}

	@Test
	public void testTablesInNS() throws IOException {

		System.out.println(NameSpaceUtil.getTablesInNameSpace(conn, "default"));

	}

	@Test
	public void testDeleteNS() throws IOException {

		System.out.println(NameSpaceUtil.deleteNS(conn, "ns1"));

	}

}
