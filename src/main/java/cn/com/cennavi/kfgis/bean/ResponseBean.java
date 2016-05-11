package cn.com.cennavi.kfgis.bean;

import java.io.Serializable;

import com.iestudio.framework.logwriter.logitem.impl.AssemblyLogItemImpl;

/**
 * 通用应答结构体
 * @author fengheliang
 *
 */
public class ResponseBean extends AssemblyLogItemImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8854485661112872114L;
	
	private String time;
	
	private String date;

	private String requestID;
	
	private String responseHeader;
	
	private String response;
	
	private Long requestTime;
	
	private String requestUrl;

	
	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public Long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}
	
	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(String responseHeader) {
		this.responseHeader = responseHeader;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(date).append(requestID).append(time).append("#");
		builder.append(time);
		builder.append("$");
		builder.append(requestID);
		builder.append("$");
		builder.append(responseHeader);
		builder.append("$");
		builder.append(response);
		builder.append("$");
		builder.append(requestTime);
		builder.append("$");
		builder.append(requestUrl);
		return builder.toString();
	}
}
