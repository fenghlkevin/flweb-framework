//package com.kevin.iesutdio.kfgis.app.framework.dataloader.configfile;
//
//import java.io.Serializable;
//
///**
// * 配置文件存储
// * @author fengheliang
// *
// */
//public class ConfigFile implements Serializable {
//
//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 5829078065207200983L;
//
//	private String fileName;
//
//	private Long lastTimestamp;
//
//	private String md5;
//
//	private byte[] config;
//
//	public byte[] getConfig() {
//		return config;
//	}
//
//	public void setConfig(byte[] config) {
//		this.config = config;
//	}
//
//	public String getMd5() {
//		return md5;
//	}
//
//	public void setMd5(String md5) {
//		this.md5 = md5;
//	}
//
//	public Long getLastTimestamp() {
//		return lastTimestamp;
//	}
//
//	public void setLastTimestamp(Long lastTimestamp) {
//		this.lastTimestamp = lastTimestamp;
//	}
//
//	public String getFileName() {
//		return fileName;
//	}
//
//	public void setFileName(String fileName) {
//		this.fileName = fileName;
//	}
//
//	@Override
//	public String toString() {
//		// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mi:ss");
//		// Calendar c=Calendar.getInstance();
//		// c.setTimeInMillis(lastTimestamp);
//		return "ConfigFile [fileName=" + fileName + ", lastTimestamp=" + lastTimestamp + ", md5=" + md5 + "]";
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (!(obj instanceof ConfigFile)) {
//			return super.equals(obj);
//		}
//		ConfigFile out = (ConfigFile) obj;
//		//return out.getFileName().equalsIgnoreCase(this.getFileName()) && out.getLastTimestamp().equals(this.getLastTimestamp()) && out.getMd5().equalsIgnoreCase(this.getMd5());
//		return out.getFileName().equalsIgnoreCase(this.getFileName()) && out.getMd5().equalsIgnoreCase(this.getMd5());
//
//	}
//
//}
