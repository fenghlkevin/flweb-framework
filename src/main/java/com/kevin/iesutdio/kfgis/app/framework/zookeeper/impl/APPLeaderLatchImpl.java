//package com.kevin.iesutdio.kfgis.app.framework.zookeeper.impl;
//
//import java.io.Closeable;
//import java.io.IOException;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//import com.kevin.iesutdio.kfgis.app.framework.zookeeper.IAPPLeaderLatch;
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.recipes.leader.LeaderLatch;
//import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
//
///**
// * leader选举
// *
// * @author shencl
// */
//public class APPLeaderLatchImpl implements IAPPLeaderLatch, Closeable {
//
//	private final LeaderLatch leaderLatch;
//
//	private static Executor executor = Executors.newCachedThreadPool();
//
//	private String name;
//
//	public APPLeaderLatchImpl(CuratorFramework client, String name,String leaderPath, LeaderLatchListener... latchListeners) {
//		this.name = name;
//		leaderLatch = new LeaderLatch(client, leaderPath);
//
//		if (latchListeners != null && latchListeners.length > 0) {
//			for (LeaderLatchListener listener : latchListeners) {
//				leaderLatch.addListener(listener, executor);
//			}
//		}
//	}
//
//	@Override
//	public void start() throws Exception {
//		leaderLatch.start();
//	}
//
//	@Override
//	public void close() throws IOException {
//		leaderLatch.close();
//	}
//
//	@Override
//	public boolean isLeader() {
//		return leaderLatch.hasLeadership();
//	}
//
//	@Override
//	public String getName() {
//		return name;
//	}
//
//}