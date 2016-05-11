package com.kevin.iesutdio.kfgis.web.framework.file.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.kevin.iesutdio.kfgis.web.framework.file.IFileParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kevin.iesutdio.kfgis.web.framework.contant.LoggerContant;
import com.kevin.iesutdio.kfgis.web.framework.util.ObjUtil;

/**
 * @author fengheliang
 *
 * @param <T>
 * @deprecated
 */
public abstract class AbstractFileDataParser<T> implements IFileParse<T> {
	
	protected Logger logger = LoggerFactory.getLogger(LoggerContant.DATALOAD_LOG_NAME);
	
	private String sysKey;
	
	private String dataFlag;
	
	public AbstractFileDataParser(String sysKey,String dataFlag){
		this.sysKey=sysKey;
		this.dataFlag=dataFlag;
	}
	
	protected String getFilePath(String filePath,String sysKey,String dataFlag) {
		String pathname = filePath;
		if (ObjUtil.isEmpty(pathname, true)) {
			//logger.debug("can not get ["+dataFlag+"]'s file path, now use system path [" +sysKey + "]");
			pathname = System.getProperty(sysKey);
		}

		if (ObjUtil.isEmpty(pathname, true)) {
			logger.error("can not get ["+dataFlag+"]'s file path in system properties [" +sysKey + "],ERROR!!");
			return null;
		}
		return pathname;
	}
	
	protected long lastTimestamp = -1;
	
	@Override
	public boolean isNewFile(String filePath) {
		filePath = this.getFilePath(filePath,sysKey,dataFlag);
		File file = new File(filePath);

		if (file.isDirectory() || !file.exists()) {
			return false;
		}
		long temp = file.lastModified();
		if (lastTimestamp < temp) {
			lastTimestamp = temp;
			return true;
		}
		return false;
	}
	
	protected abstract T createContainer();
	
	protected abstract void putData2Container(T table,FileInputStream fis,File f) throws Exception;
	
	@Override
	public T execute(String filePath) {
		String pathname = this.getFilePath(filePath,sysKey,dataFlag);

		File path = new File(pathname);

		if (path == null || !path.exists()) {
			return null;
		}
		//TMCTable tmcTable = new TMCTable();
		T table=this.createContainer();

		FileInputStream reader=null;
//		BufferedReader reader = null;
		try {
			if(path.isFile()){
				reader=new FileInputStream(path);
			}
			this.putData2Container(table, reader,path);
//			byte[] bs=new byte[fis.available()];
//			fis.read(bs);
//			String line;
//			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));

//			while ((line = reader.readLine()) != null) {
//				if (line.startsWith("#")) {
//					continue;
//				}
//				String[] tokens = line.split(",");
//				if (this.ignore(tokens)) {
//					line = null;
//					continue;
//				}
//				this.putValue2Container(table, tokens);
//
//			}
		} catch (Exception e) {
			logger.error("Parse "+this.dataFlag+" error!", e);
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("close "+this.dataFlag+" error!", e);
				}
			}
		}
		return table;
	}	

	public String getSysKey() {
		return sysKey;
	}

	public String getDataFlag() {
		return dataFlag;
	}

	public long getLastTimestamp() {
		return lastTimestamp;
	}
}
