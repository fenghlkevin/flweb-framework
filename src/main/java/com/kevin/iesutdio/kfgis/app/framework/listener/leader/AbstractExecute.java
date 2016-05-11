package com.kevin.iesutdio.kfgis.app.framework.listener.leader;

import com.kevin.iesutdio.kfgis.app.framework.zookeeper.ZKClientFactory;

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
