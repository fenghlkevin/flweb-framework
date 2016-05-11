package com.kevin.iesutdio.kfgis.app.framework.zookeeper.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kevin.iesutdio.kfgis.app.framework.contant.LoggerContent;

public class SimpleLeaderLatchListener implements LeaderLatchListener {
	
	private Logger logger=LoggerFactory.getLogger(LoggerContent.ZOOKEEPER_LEADER_LOG);
	
	private CuratorFramework client;
	
	private String leaderPath;
	
	private String name;
	
	public SimpleLeaderLatchListener(CuratorFramework client,String leaderPath,String name){
		this.client=client;
		this.leaderPath=leaderPath;
		this.name=name;
	}

	@Override
	public void isLeader() {
		try {
			client.setData().forPath(this.getLeaderPath(), this.getName().getBytes());
		} catch (Exception e) {
			logger.error("upload leader flag error！", e);
		}
	}

	@Override
	public void notLeader() {
		
	}

	public CuratorFramework getClient() {
		return client;
	}

	public String getLeaderPath() {
		return leaderPath;
	}

	public String getName() {
		return name;
	}

}
