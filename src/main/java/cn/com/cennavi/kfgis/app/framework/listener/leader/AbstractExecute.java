package cn.com.cennavi.kfgis.app.framework.listener.leader;

import cn.com.cennavi.kfgis.app.framework.zookeeper.ZKClientFactory;

/**
 * @author fengheliang
 *
 */
public abstract class AbstractExecute {

	public boolean nodeExist(String path){
		try {
			return ZKClientFactory.getInstance().getCurator().checkExists().forPath(path)!=null;
		} catch (Exception e) {
			return true;
		}
	}

}
