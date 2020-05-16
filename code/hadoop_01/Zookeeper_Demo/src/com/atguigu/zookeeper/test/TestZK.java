package com.atguigu.zookeeper.test;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestZK {

	private String connectString = "hadoop102:2181,hadoop103:2181";
	private int sessionTimeout = 6000;
	private ZooKeeper zooKeeper;

	// zkCli.sh -server xxx:2181
	@Before
	public void init() throws Exception {

		// 创建一个zk的客户端对象
		zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {

			// 回调方法，一旦watcher观察的path触发了指定的事件，服务端会通知客户端，客户端收到通知后
			// 会自动调用process()
			@Override
			public void process(WatchedEvent event) {
				// TODO Auto-generated method stub

			}
		});

		System.out.println(zooKeeper);

	}

	@After
	public void close() throws InterruptedException {

		if (zooKeeper != null) {
			zooKeeper.close();
		}

	}

	// ls
	@Test
	public void ls() throws Exception {

		Stat stat = new Stat();

		List<String> children = zooKeeper.getChildren("/", null, stat);

		System.out.println(children);

		System.out.println(stat);

	}

	// create [-s] [-e] path data
	// public String create(final String path, byte data[], List<ACL> acl,CreateMode
	// createMode)
	@Test
	public void create() throws Exception {

		zooKeeper.create("/eclipse", "hello".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

	}

	// get path public byte[] getData(final String path, Watcher watcher, Stat stat)
	@Test
	public void get() throws Exception {

		byte[] data = zooKeeper.getData("/eclipse", null, null);

		System.out.println(new String(data));

	}

	// set path data
	@Test
	public void set() throws Exception {

		zooKeeper.setData("/eclipse", "hi".getBytes(), -1);

	}

	// delete path
	@Test
	public void delete() throws Exception {

		zooKeeper.delete("/eclipse", -1);

	}

	// rmr path
	@Test
	public void rmr() throws Exception {

		String path = "/data";

		// 先获取当前路径中所有的子node
		List<String> children = zooKeeper.getChildren(path, false);

		// 删除所有的子节点
		for (String child : children) {

			zooKeeper.delete(path + "/" + child, -1);

		}

		zooKeeper.delete(path, -1);

	}

	// 判断当前节点是否存在
	@Test
	public void ifNodeExists() throws Exception {

		Stat stat = zooKeeper.exists("/data2", false);

		System.out.println(stat == null ? "不存在" : "存在");

	}

}
