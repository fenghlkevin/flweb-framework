package com.kevin.iesutdio.kfgis.bean.param;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.Validator;

public interface IParams extends Serializable{
	
	public abstract Validator getValidator();
	
	public abstract void setPostValues(byte[] postValue,HttpServletRequest httprequest);
	
}
