package com.kevin.iesutdio.kfgis.web.framework.file.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import com.kevin.iesutdio.kfgis.web.framework.file.IFileParse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kevin.iesutdio.kfgis.web.framework.contant.LoggerContant;
import com.kevin.iesutdio.kfgis.web.framework.util.ObjUtil;

public abstract class NAbstractFileDataParser<T> implements IFileParse<T> {

	protected Logger logger = LoggerFactory.getLogger(LoggerContant.DATALOAD_LOG_NAME);

	private String sysKey;

	private String dataFlag;

	public NAbstractFileDataParser(String sysKey, String dataFlag) {
		this.sysKey = sysKey;
		this.dataFlag = dataFlag;
	}

	protected String getFilePath(String filePath, String sysKey, String dataFlag) {
		String pathname = filePath;
		if (ObjUtil.isEmpty(pathname, true)) {
			// logger.debug("can not get ["+dataFlag+"]'s file path, now use system path ["
			// +sysKey + "]");
			pathname = System.getProperty(sysKey);
		}

		if (ObjUtil.isEmpty(pathname, true)) {
			logger.error("can not get [" + dataFlag + "]'s file path in system properties [" + sysKey + "],ERROR!!");
			return null;
		}
		return pathname;
	}

	protected long lastTimestamp = -1;

	private Map<String, Long> lastTimestampFiles = new HashMap<String, Long>();

	private static class MFileFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			return pathname.isFile() && !pathname.getName().toLowerCase().equalsIgnoreCase(".svn");
		}
	}

	protected FileFilter createFileFilter() {
		return null;
	}

	@Override
	public boolean isNewFile(String filePath) {
		filePath = this.getFilePath(filePath, sysKey, dataFlag);
		File f = new File(filePath);
		if (!f.exists()) {
			return false;
		}

		FileFilter ff = createFileFilter();
		if (ff == null) {
			ff = new MFileFilter();
		}

		if (f.isDirectory()) {
			File[] files = f.listFiles(ff);
			if (files.length > 0) {
				Arrays.sort(files, new Comparator<File>() {
					@Override
					public int compare(File o1, File o2) {
						return o1.lastModified() <= o2.lastModified() ? -1 : 1;
					}
				});
			}
			boolean reValue = false;
			if (lastTimestampFiles.isEmpty() || lastTimestampFiles.values().size() != files.length) {
				reValue = true;
			} else {
				for (File tempFile : files) {
					String key = tempFile.getName();
					reValue = lastTimestampFiles.containsKey(key) ? lastTimestampFiles.get(key) < tempFile.lastModified() : true;
					if (reValue)
						break;
				}
			}
			if (reValue) {
				lastTimestampFiles.clear();
				for (File tempFile : files) {
					lastTimestampFiles.put(tempFile.getName(), tempFile.lastModified());
				}
			}
			return reValue;
		} else if (f.isFile()) {
			long temp = f.lastModified();
			if (lastTimestamp < temp) {
				lastTimestamp = temp;
				return true;
			}
		}
		return false;
	}

	protected abstract T createContainer();

	protected abstract void putData2Container(T table, FileInputStream fis, File f) throws Exception;

	@Override
	public T execute(String filePath) {
		String pathname = this.getFilePath(filePath, sysKey, dataFlag);
		File path = new File(pathname);
		if (path == null || !path.exists()) {
			return null;
		}
		final T table = this.createContainer();
		FileInputStream reader = null;
		try {
			if (path.isFile()) {
				reader = new FileInputStream(path);
				this.putData2Container(table, reader, path);
				logger.info("Finish data Read ["+path.getPath()+"]");
			} else if (path.isDirectory()) {
				FileFilter ff = createFileFilter();
				if (ff == null) {
					ff = new MFileFilter();
				}
				File[] files = path.listFiles(ff);
//				if (files.length > 10) {
//					ThreadPool<Boolean> pool = new ThreadPool<Boolean>();
//					String poolcount=System.getProperty("DATA_TASK_THREAD_POOL");
//					int cnt=ObjUtil.isEmpty(poolcount,true)?2:new Integer(poolcount);
//					pool.startExecutors(cnt);
//					CountDownLatch cdl = new CountDownLatch(files.length);
//					for (File tempFile : files) {
//						Command c = new Command(table, tempFile, this, cdl);
//						pool.addCommand(c);
//					}
//					pool.shutDownExecutors(false);
//					while (true) {
//						if (cdl.getCount() > 0) {
//							System.out.println("waiting................");
//							Thread.sleep(2000);
//							continue;
//						}
//						break;
//					}
//				} else {
					for (File tempFile : files) {
						reader = new FileInputStream(tempFile);
						this.putData2Container(table, reader, tempFile);
						reader.close();
						logger.info("Finish data Read ["+tempFile.getPath()+"]");
					}
//				}
				logger.info("Finished Data Read, DataCount is [" + files.length + "]");
			}
			afterLoaded(table);
		} catch (Exception e) {
			logger.error("Parse " + this.dataFlag + " error!", e);
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("close " + this.dataFlag + " error!", e);
				}
			}
		}
		return table;
	}

	private class Command implements Callable<Boolean> {

		private T table;

		private File tf;

		private NAbstractFileDataParser<T> temp;

		private CountDownLatch cdl;

		private Command(T table, File tf, NAbstractFileDataParser<T> temp, CountDownLatch cdl) {
			this.table = table;
			this.tf = tf;
			this.temp = temp;
			this.cdl = cdl;
		}

		@Override
		public Boolean call() throws Exception {
			boolean ret = false;
			FileInputStream reader = null;
			try {
				reader = new FileInputStream(tf);
				temp.putData2Container(table, reader, tf);
				//reader.close();
				ret = true;
				logger.info("Finish data Read ["+tf.getPath()+"]");
			} catch (Exception e) {
				// return false;
				e.printStackTrace();
			} finally {
				cdl.countDown();
				if (reader != null) {
					reader.close();
				}
			}
			return ret;
		}

	}
	protected void afterLoaded(T table) {

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
