package com.kevin.iesutdio.kfgis.app.framework.listener.task.abs;

import java.io.Serializable;
import java.util.Map;

public abstract class AbstractAPPTask implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5336061127137875269L;

	private String name;
	
	private String type;
	
	private Integer level;
	
	private boolean iswitch;
	
	private Map<String,String> values;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}

	public boolean getSwitch() {
		return iswitch;
	}

	public void setSwitch(boolean iswitch) {
		this.iswitch = iswitch;
	}

}
