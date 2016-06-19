//package com.kevin.iesutdio.kfgis.app.framework.demo;
//
//import java.io.Serializable;
//
//import com.kevin.iesutdio.kfgis.app.framework.listener.task.abs.APPTask;
//import com.kevin.iesutdio.kfgis.app.framework.listener.task.abs.AbstractAPPTask;
//
//public class TaskCommondDemo extends AbstractAPPTask implements Serializable, APPTask, Runnable {
//
//	private String type;
//
//	private String name;
//
//	private Integer level;
//
//	private String adcode;
//
//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 9136222397296749722L;
//
//	@Override
//	public void run() {
//		System.out.println(adcode);
//
//	}
//
//	@Override
//	public void setName(String name) {
//		this.name=name;
//
//	}
//
//	@Override
//	public String getName() {
//		return name;
//	}
//
//	@Override
//	public void setType(String type) {
//		this.type=type;
//	}
//
//	@Override
//	public String getType() {
//		return this.type;
//	}
//
//	@Override
//	public void setLevel(Integer level) {
//		this.level=level;
//	}
//
//	@Override
//	public Integer getLevel() {
//		return this.level;
//	}
//
//	public String getAdcode() {
//		return adcode;
//	}
//
//	public void setAdcode(String adcode) {
//		this.adcode = adcode;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if(obj==null||!(obj instanceof TaskCommondDemo)){
//			return false;
//		}
//		TaskCommondDemo tc=(TaskCommondDemo)obj;
//		return this.getName().equalsIgnoreCase(tc.getName())&&this.getType().equalsIgnoreCase(tc.getType());
//	}
//
//	@Override
//	public void closeTask(){
//
//	}
//
//
//}
