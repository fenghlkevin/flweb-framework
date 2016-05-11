package com.kevin.iesutdio.kfgis.web.framework.file.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author fengheliang
 *
 * @param <T>
 */
public abstract class CSVDataAbstractParser<T> extends NAbstractFileDataParser<T> {

	public CSVDataAbstractParser(String sysKey, String dataFlag) {
		super(sysKey, dataFlag);
	}

	@Override
	protected void putData2Container(T table, FileInputStream fis,File f) throws Exception {
		BufferedReader reader = null;
		String line;

		try {
			reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
			int count=0;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				String[] tokens = line.split(",");
				if (this.ignore(tokens)) {
					line = null;
					continue;
				}
				this.putValue2Container(table, tokens);
				this.putValue2Container(table, tokens,f,count++);

			}
		} finally {
			reader.close();
		}
	}
	
	protected abstract boolean ignore(String[] tokens);

	/**
	 * 
	 * @param table
	 * @param tokens
	 * @deprecated
	 */
	protected  void putValue2Container(T table, String[] tokens){
		this.putValue2Container(table, tokens, null,0);
	}
	
	protected void putValue2Container(T table, String[] tokens,File f,int count){
		
	}
}
