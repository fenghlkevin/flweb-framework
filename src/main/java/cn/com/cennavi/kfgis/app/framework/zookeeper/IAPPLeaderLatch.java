package cn.com.cennavi.kfgis.app.framework.zookeeper;

import java.io.IOException;

/**
 * leader判断接口
 * @author fengheliang
 *
 */
public interface IAPPLeaderLatch {
	
	public void start() throws Exception ;

	public void close() throws IOException;

	public boolean isLeader() ;

	public String getName() ;
}
