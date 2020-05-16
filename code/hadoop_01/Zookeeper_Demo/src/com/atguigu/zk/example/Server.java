package com.atguigu.zk.example;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/*
 * 	1.每次启动后，在执行自己的核心业务之前，先向zk集群注册一个临时节点且向临时节点中保存一些关键信息
 */
public class Server {

	private String connectString = "hadoop102:2181,hadoop103:2181";
	private int sessionTimeout = 6000;   //  session timeout in milliseconds
	private ZooKeeper zooKeeper;

	private String basePath = "/Servers";

	// 初始化客户端对象
	public void init() throws Exception {
		zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
			}
		});
	}

	// 使用zk客户端注册临时节点
	public void regist(String info) throws KeeperException, InterruptedException {
		// 节点必须是临时待序号的节点
		zooKeeper.create(basePath + "/server", info.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
	}

	// 其他的业务功能
	public void doOtherBusiness() throws InterruptedException {
		System.out.println(".......server 正在工作......");
		// 持续工作
		while (true) {
			Thread.sleep(5000);
			System.out.println(".....server working......");
		}
	}

	// 还是得先启动
	public static void main(String[] args) throws Exception {
		Server server = new Server();
		// 初始化zk客户端对象
		server.init();
		// 注册节点
		server.regist(args[0]);
		// 执行自己其他的业务功能
		server.doOtherBusiness();
	}
}
