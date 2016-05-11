package com.kevin.iesutdio.kfgis.app.framework.demo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

public class ZKTest {

	static final String zookeeperConnectionString = "192.168.59.173:2181,192.168.59.163:2181,192.168.59.164:2181";
	//static final String zookeeperConnectionString = "172.19.11.141:2181";

	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		final CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
		
		// 启动 上面的namespace会作为一个最根的节点在使用时自动创建
		client.start();
		client.delete().deletingChildrenIfNeeded().forPath("/taskplatform");
		//client.delete().deletingChildrenIfNeeded().forPath("/TaskPlat");
//		client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
		List<String> children=client.getChildren().forPath("/");
		output(children);
		
		children=client.getChildren().forPath("/brokers");
		output(children);
		
		children=client.getChildren().forPath("/brokers/topics/LOGBMWREQLOG");
		output(children);
		
		children=client.getChildren().forPath("/brokers/topics/LOGBMWREQLOG/partitions");
		output(children);
//		children=client.getChildren().forPath("/taskplatform");
//		output(children);
//		
//		children=client.getChildren().forPath("/taskplatform/leader");
//		output(children);
//		
//		children=client.getChildren().forPath("/taskplatform/server");
//		output(children);
//		
//		children=client.getChildren().forPath("/taskplatform/regserver");
//		output(children);
//		
//		children=client.getChildren().forPath("/taskplatform/tasks");
//		output(children);
		
//		client2.create().forPath(path+"/1");
		
//		children=client2.getChildren().forPath(path);
//		output(children);
//		client.close();
//		children=client2.getChildren().forPath(path);
//		output(children);
		
//		client2.create().forPath(path+"/1");
//		
//		 children=client.getChildren().forPath(path);
//		for(String s:children){
//			System.out.println(s);
//		}
//		client.close();
//		
//		children=client2.getChildren().forPath(path);
//		for(String s:children){
//			System.out.println(s);
//		}
	}
	
	private static void output(List<String> children){
		for(String s:children){
			System.out.println(s);
		}
		System.out.println();
	}

}
