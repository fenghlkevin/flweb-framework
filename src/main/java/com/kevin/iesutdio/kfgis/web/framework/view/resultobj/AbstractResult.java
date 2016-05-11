package com.kevin.iesutdio.kfgis.web.framework.view.resultobj;

import com.kevin.iesutdio.kfgis.bean.param.IResult;

public class AbstractResult implements IResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6306011848641255455L;

	private String content_type;
	
	private String encoding="utf-8";

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	

}
