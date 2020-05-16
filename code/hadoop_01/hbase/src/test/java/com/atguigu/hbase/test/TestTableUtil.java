package com.atguigu.hbase.test;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Connection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.atguigu.hbase.tools.ConnectionUtil;
import com.atguigu.hbase.tools.TableUtil;

public class TestTableUtil {

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
	public void testTableExists() throws IOException {

		System.out.println(TableUtil.ifTableExists(conn, "t2", null));

	}

	@Test
	public void testCreateTable() throws IOException {

		System.out.println(TableUtil.createTable(conn, "t2", null, "cf1", "cf2"));

	}

	@Test
	public void testDropTable() throws IOException {

		System.out.println(TableUtil.dropTable(conn, "t2", null));

	}

}
