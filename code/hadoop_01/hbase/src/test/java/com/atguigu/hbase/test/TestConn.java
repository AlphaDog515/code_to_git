package com.atguigu.hbase.test;

import java.io.IOException;

import org.junit.Test;

import com.atguigu.hbase.tools.ConnectionUtil;

public class TestConn {

	@Test
	public void test() throws IOException {

		System.out.println(ConnectionUtil.getConn());

	}

}
