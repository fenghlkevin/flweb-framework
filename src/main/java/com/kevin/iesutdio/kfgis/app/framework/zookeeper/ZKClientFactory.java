package com.kevin.iesutdio.kfgis.app.framework.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.kevin.iesutdio.kfgis.app.framework.zookeeper.impl.APPLeaderLatchImpl;

public class ZKClientFactory {

	private static ZKClientFactory instance;

	public static ZKClientFactory getInstance() {
		if (instance == null) {
			instance = new ZKClientFactory();
		}
		return instance;
	}

	private ZKClientFactory() {
	}

	private CuratorFramework curator;

	private IAPPLeaderLatch _leaderLatch;

	public ZKClientFactory createCurator(String zookeepers, String namespace) {
		RetryPolicy rp = new ExponentialBackoffRetry(1000, 3);// 重试机制
		Builder builder = CuratorFrameworkFactory.builder().connectString(zookeepers).connectionTimeoutMs(50000).sessionTimeoutMs(50000).retryPolicy(rp);
		builder.namespace(namespace);
		curator = builder.build();
		// curator = CuratorFrameworkFactory.newClient(zookeepers, retryPolicy);
		curator.start();
		return this;
	}

	public ZKClientFactory createLeaderLatch(String serverName, String leaderPath, LeaderLatchListener... latchListeners) throws Exception {
		_leaderLatch = new APPLeaderLatchImpl(curator, serverName, leaderPath, latchListeners);
		_leaderLatch.start();
		return this;
	}

	public CuratorFramework getCurator() {
		return curator;
	}

	public IAPPLeaderLatch getLeaderLatch() {
		return _leaderLatch;
	}

}
