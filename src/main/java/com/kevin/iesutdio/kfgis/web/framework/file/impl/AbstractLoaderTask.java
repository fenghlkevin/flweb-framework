package com.kevin.iesutdio.kfgis.web.framework.file.impl;

import com.kevin.iesutdio.kfgis.web.framework.contant.LoggerContant;
import com.kevin.iesutdio.kfgis.web.framework.file.IFileParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kevin.iesutdio.kfgis.web.framework.file.CommonFileReader;

public abstract class AbstractLoaderTask {

	private static Logger logger = LoggerFactory.getLogger(LoggerContant.DATALOAD_LOG_NAME);

	protected static CommonFileReader reader;

	{
		reader = new CommonFileReader();
	}

	/**
	 * <p>
	 * Discription:[方法功能中文描述]
	 * </p>
	 * 
	 * @author:fengheliang
	 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
	 */
	public abstract void load();

	protected void read(String key, String path, IFileParse<?> parse, boolean immediate) {
		String log = key;
		int isRead = reader.execute(key, path, parse, immediate);
		if (isRead == 1) {
			logger.debug(log.concat("成功"));
		} else if (isRead == 0) {
			logger.debug(log.concat("未发生变化"));
		} else {
			logger.error(log.concat("失败"));
		}
	}

}
