package cn.com.cennavi.kfgis.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.iestudio.framework.logwriter.logitem.impl.AssemblyLogItemImpl;

/**
 * Created on 2011-12-25
 * <p>Title: WEB-T GIS核心系统_系统服务_请求应答内容记录</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author Kevin Feng fengheliang@cennavi.com.cn
 * @version 1.0
 * @update 修改日期 修改描述
 */
public class RequestBean extends AssemblyLogItemImpl implements Serializable {

    /**
     * <p>Description:[字段功能描述]</p>
     */
    private static final long serialVersionUID = 2752316944773054841L;

    {
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");//yyyyMMddHHmmss
    	SimpleDateFormat sdfDate=new SimpleDateFormat("yyyyMMdd");
    	time=sdf.format(new Date());
    	date=sdfDate.format(new Date());
    }
    /**
     * <p>Description:[请求id]</p>
     */
    private String requestID;
    
    private String requestBody;

    /**
     * <p>Description:[当前时间]</p>
     */
   private String time;
   
   private String date;



	/**
     * <p>Description:[请求uri]</p>
     */
    private String uri = "";
    /**
     * <p>Description:[请求用户id]</p>
     */
    private String userid = "";
    
    private Long requestTime;
    
    private Long responseTime;
    
    private String successFlag = "1";
    
    private String entype;
    
    private Long retime;
    
    private Long enRequestTime;
    
    private Long enResponseTime;
    
    private Long enRtTime;
    
    private String ipAddr;
    
    private String enCodeId;
    
    private String exceptionCo = null;
    
    private String bizcode;
    
    private String acode = null;
    
    private String location=null;
    
    private String destination=null;
    
    private Double l2distance=0D;
    
    private String version_1=null;
    
    private String version_2=null;
    
    private Integer responseSize=0;
    
    private String vehicleTrajectory=null;
    
    private String op;
    
    private Integer reset; 
    
    private String cityCode;
    
    private String cityName;
    /**
     * 状态码
     */
    private String statusCode="200";
    	
	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getCityCode() {
		return cityCode;
	}
	
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


	public Integer getReset() {
		return reset;
	}

	
	public void setReset(Integer reset) {
		this.reset = reset;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getAcode() {
        return acode;
    }

    public void setAcode(String acode) {
        this.acode = acode;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public String getSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(String successFlag) {
        this.successFlag = successFlag;
    }

    public String getEntype() {
        return entype;
    }

    public void setEntype(String entype) {
        this.entype = entype;
    }

    public Long getRetime() {
        return retime;
    }

    public void setRetime(Long retime) {
        this.retime = retime;
    }

    public Long getEnRequestTime() {
        return enRequestTime;
    }

    public void setEnRequestTime(Long enRequestTime) {
        this.enRequestTime = enRequestTime;
    }

    public Long getEnResponseTime() {
        return enResponseTime;
    }

    public void setEnResponseTime(Long enResponseTime) {
        this.enResponseTime = enResponseTime;
    }

    public Long getEnRtTime() {
        return enRtTime;
    }

    public void setEnRtTime(Long enRtTime) {
        this.enRtTime = enRtTime;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getEnCodeId() {
        return enCodeId;
    }

    public void setEnCodeId(String enCodeId) {
        this.enCodeId = enCodeId;
    }

    public String getExceptionCo() {
        return exceptionCo;
    }

    public void setExceptionCo(String exceptionCo) {
        this.exceptionCo = exceptionCo;
    }

    public String getBizcode() {
        return bizcode;
    }

    public void setBizcode(String bizcode) {
        this.bizcode = bizcode;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Double getL2distance() {
		return l2distance;
	}

	public void setL2distance(Double l2distance) {
		this.l2distance = l2distance;
	}

	public String getVersion_1() {
		return version_1;
	}

	public void setVersion_1(String version_1) {
		this.version_1 = version_1;
	}

	public String getVersion_2() {
		return version_2;
	}

	public void setVersion_2(String version_2) {
		this.version_2 = version_2;
	}

	public Integer getResponseSize() {
		return responseSize;
	}

	public void setResponseSize(Integer responseSize) {
		this.responseSize = responseSize;
	}
	
	public String getVehicleTrajectory() {
		return vehicleTrajectory;
	}

	public void setVehicleTrajectory(String vehicleTrajectory) {
		this.vehicleTrajectory = vehicleTrajectory;
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
		builder.append(requestBody);
		builder.append("$");
		builder.append(uri);
		builder.append("$");
		builder.append(userid);
		builder.append("$");
		builder.append(requestTime);
		builder.append("$");
		builder.append(responseTime);
		builder.append("$");
		builder.append(successFlag);
		builder.append("$");
		builder.append(entype);
		builder.append("$");
		builder.append(retime);
		builder.append("$");
		builder.append(enRequestTime);
		builder.append("$");
		builder.append(enResponseTime);
		builder.append("$");
		builder.append(enRtTime);
		builder.append("$");
		builder.append(ipAddr);
		builder.append("$");
		builder.append(enCodeId);
		builder.append("$");
		builder.append(exceptionCo);
		builder.append("$");
		builder.append(bizcode);
		builder.append("$");
		builder.append(acode);
		builder.append("$");
		builder.append(location);
		builder.append("$");
		builder.append(destination);
		builder.append("$");
		builder.append(l2distance);
		builder.append("$");
		builder.append(version_1);
		builder.append("$");
		builder.append(version_2);
		builder.append("$");
		builder.append(responseSize);
		builder.append("$");
		builder.append(vehicleTrajectory);
		builder.append("$");
		builder.append(op);
		builder.append("$");
		builder.append(reset);
		builder.append("$");
		builder.append(statusCode);
		
		return builder.toString();
	}

	
}