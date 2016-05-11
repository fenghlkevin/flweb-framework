package com.kevin.iesutdio.kfgis.app.framework.listener.leader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import com.kevin.iesutdio.kfgis.app.framework.contant.LoggerContent;
import com.kevin.iesutdio.kfgis.app.framework.contant.SystemPropertiesContant;
import com.kevin.iesutdio.kfgis.app.framework.zookeeper.ZKClientFactory;
import com.kevin.iesutdio.kfgis.web.framework.file.CommonFileMapContainer;
import com.kevin.iesutdio.kfgis.util.ObjectUtil;
import org.apache.commons.io.FileUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kevin.iesutdio.kfgis.app.framework.dataloader.configfile.ConfigFile;
import com.kevin.iesutdio.kfgis.app.framework.listener.leader.inf.IExecute;

/**
 * 标准的follower任务
 * @author fengheliang
 *
 */
public class SimpleFollowerExecuteImpl extends AbstractExecute implements IExecute {

	private Logger logger = LoggerFactory.getLogger(LoggerContent.ZOOKEEPER_LEADER_DO_LOG);

	private String configPath;

	public SimpleFollowerExecuteImpl(String configPath) {
		this.configPath = configPath;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() throws Exception {
		// 对比并下载最新的配置文件（比对通过最后修改时间戳、以及文件md5）
		CuratorFramework curator = ZKClientFactory.getInstance().getCurator();
//		Stat stat = curator.checkExists().forPath(configPath);
		if (!super.nodeExist(configPath)) {
			logger.debug("Config Path [" + configPath + "] is not EXISTS");
			return;
		}
		byte[] configBytes = curator.getData().forPath(configPath);
		if (configBytes == null || configBytes.length <= 0) {
			logger.debug("Config Path [" + configPath + "] Value is NULL");
			return;
		}
		Map<String, ConfigFile> zooCfs = (Map<String, ConfigFile>) ObjectUtil.getObjectBytes(configBytes);
		Map<String, ConfigFile> localCfs = (Map<String, ConfigFile>) CommonFileMapContainer.getInstance().getFileMap(SystemPropertiesContant.SYSTEM_PROPERTIES_FOLDER);
		if (!zooCfs.equals(localCfs) || !localCfs.equals(zooCfs)) {
			for (ConfigFile cf : zooCfs.values()) {
				File f = new File(System.getProperty(SystemPropertiesContant.SYSTEM_PROPERTIES_FOLDER) + "/" + cf.getFileName());
				FileUtils.deleteQuietly(f);
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(f);
					fos.write(cf.getConfig());
					fos.flush();
				} catch (Exception e) {
					this.logger.error("Edit Local Config [" + cf.getFileName() + "] ERROR", e);
				} finally {
					if (fos != null) {
						fos.close();
					}
				}
			}
		}
	}

}
