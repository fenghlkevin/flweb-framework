//package com.kevin.iesutdio.kfgis.app.framework.listener.leader;
//
//import com.kevin.iesutdio.kfgis.app.framework.zookeeper.ZKClientFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.kevin.iesutdio.kfgis.app.framework.contant.LoggerContent;
//import com.kevin.iesutdio.kfgis.app.framework.listener.leader.inf.IExecute;
//import com.kevin.iesutdio.kfgis.app.framework.zookeeper.IAPPLeaderLatch;
//
///**
// *
// * @author fengheliang
// *
// */
//public class ExecutorMonitorListener  implements Runnable{
//
//	private Logger logger = LoggerFactory.getLogger(LoggerContent.ZOOKEEPER_LEADER_DO_LOG);
//
//	protected void execute(){
//		IAPPLeaderLatch leaderLatch= ZKClientFactory.getInstance().getLeaderLatch();
//
//		if(leaderLatch.isLeader()){
//			try {
//				this.leaderExecute.execute();
//			} catch (Exception e) {
//				logger.error("Leader Do ERROR!",e);
//			}
//			System.out.println("leader do");
//		}else{
//			try {
//				this.followerExecute.execute();
//			} catch (Exception e) {
//				logger.error("Follower Do ERROR!",e);
//			}
//			System.out.println("follower do");
//		}
//	}
//
//	private IExecute leaderExecute;
//
//	private IExecute followerExecute;
//
//	public ExecutorMonitorListener setLeaderExecute(IExecute leaderExecute){
//		this.leaderExecute= leaderExecute;
//		return this;
//	}
//
//	public ExecutorMonitorListener setFollowerExecute(IExecute followerExecute){
//		this.followerExecute= followerExecute;
//		return this;
//	}
//
//	@Override
//	public void run() {
//		execute();
//	}
//
//}
