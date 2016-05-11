package com.kevin.iesutdio.kfgis.bean;

import java.io.Serializable;

import com.kevin.iesutdio.kfgis.bean.param.IResult;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("response")
public class Response implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long statusCode=200L;
	
	@XStreamAlias(value="responseID")
	private String responseID;
	
    @JsonSerialize(include=Inclusion.NON_NULL)
	private String errorMsg;
	
	@JsonSerialize(include=Inclusion.NON_NULL)
	private String errorStackTrace;
	
	@XStreamAlias(value="result")
	@JsonSerialize(include=Inclusion.NON_NULL)
	private IResult result;

	public IResult getResult() {
		return result;
	}

	public void setResult(IResult result) {
		this.result = result;
	}

	public String getErrorStackTrace() {
		return errorStackTrace;
	}

	public void setErrorStackTrace(String errorStackTrace) {
		this.errorStackTrace = errorStackTrace;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Long getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Long statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getResponseID() {
        return responseID;
    }

    public void setResponseID(String responseID) {
        this.responseID = responseID;
    }
	
}
