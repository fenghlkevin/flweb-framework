package com.kevin.iesutdio.kfgis.app.framework.dataloader.configfile;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.kevin.iesutdio.kfgis.app.framework.util.MD5;
import com.kevin.iesutdio.kfgis.web.framework.file.impl.AbstractFileDataParser;

public class ConfigFolderDataParser extends AbstractFileDataParser<Map<String, ConfigFile>> {

	public ConfigFolderDataParser(String sysKey, String dataFlag) {
		super(sysKey, dataFlag);
	}

	/* (non-Javadoc)
	 * @see AbstractFileDataParser#isNewFile(java.lang.String)
	 * 当判断新文件时，需要进行解析判断。putData2Container不用再解析一次了
	 */
	@Override
	public boolean isNewFile(String filePath) {
		Map<String, ConfigFile> table=new HashMap<String, ConfigFile>();
		String tp=this.getFilePath(filePath, this.getSysKey(), this.getDataFlag());
		this.getAllFolderValues(table, new File(tp));
		if(lastValues==null||(!lastValues.equals(table)||!table.equals(lastValues))){
			lastValues=table;
			return true;
		}
		return false;
	}

	@Override
	protected Map<String, ConfigFile> createContainer() {
		return new HashMap<String, ConfigFile>();
	}
	
	private Map<String, ConfigFile> lastValues=null;
	
	private void getAllFolderValues(Map<String, ConfigFile> table,File f){
		File[] fs = f.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().toLowerCase().endsWith(".xml") || pathname.getName().toLowerCase().endsWith(".properties")
						|| pathname.getName().toLowerCase().endsWith(".csv") || pathname.getName().toLowerCase().endsWith(".json")
						|| pathname.getName().toLowerCase().endsWith(".txt") || pathname.getName().toLowerCase().endsWith(".dtd");
			}
		});

		for (File ofs : fs) {
			ConfigFile cf = new ConfigFile();
			FileInputStream fstream = null;
			try {
				fstream = new FileInputStream(ofs);
				Long lastTime = ofs.lastModified();

				byte[] bs = new byte[fstream.available()];
				fstream.read(bs);

				String md5 = MD5.getMD5Code(bs);

				cf.setFileName(ofs.getName());
				cf.setMd5(md5);
				cf.setConfig(bs);
				cf.setLastTimestamp(lastTime);
				table.put(ofs.getName(), cf);
			} catch (Exception e) {
				logger.error("Loading File [" + ofs.getName() + " ] was ERROR!", e);
			}finally{
				try {
					fstream.close();
				} catch (IOException e) {
					logger.error("Closing File [" + ofs.getName() + " ] was ERROR!", e);
				}
			}
		}
	}

	@Override
	protected void putData2Container(Map<String, ConfigFile> table, FileInputStream fis, File f) throws Exception {
		table.putAll(lastValues);
//		lastValues=table;
	}

}
