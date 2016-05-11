package com.kevin.iesutdio.kfgis.bean.param;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import com.kevin.iesutdio.kfgis.web.framework.annotation.CustomParamProcessor;
import com.kevin.iesutdio.kfgis.web.framework.annotation.ICustomParamProcessor;
import com.kevin.iesutdio.kfgis.web.framework.annotation.Ignore;
import com.kevin.iesutdio.kfgis.web.framework.exception.NInvalidParamException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.thoughtworks.xstream.XStream;

public abstract class AbstractParams implements IParams {

	/**
     * 
     */
	private static final long serialVersionUID = -473377805991288672L;

	/**
	 * jackson parser
	 */
	private static ObjectMapper mapper;
	static {
		mapper = new ObjectMapper();
	}

	// private Logger logger=LoggerFactory.getLogger(AbstractParams.class);

	@Ignore
	private String uri;

	@Ignore
	private byte[] postValue;

	@Ignore
	private String xmlStr;

	@Ignore
	private IXmlParams xmlParams;

	/*
	 * (non-Javadoc)
	 * 
	 * @see IParams#setPostValues(byte[])
	 */
	@Override
	public void setPostValues(byte[] postValue,HttpServletRequest httprequest) {
		this.postValue = postValue;
		if (postValue != null && postValue.length > 0 && this.getXmlClass() != null) {
			/**
			 * 处理xml
			 */
			try {
				Method method = this.getClass().getMethod("getXmlClass");
				if (method != null) {
					CustomParamProcessor cp = method.getAnnotation(CustomParamProcessor.class);
					if (cp != null) {
						ICustomParamProcessor processor = cp.processor().newInstance();
						postValue = processor.process(this, postValue,httprequest);
						this.postValue = postValue;
					}
				}
			} catch (SecurityException e) {
				throw new NInvalidParamException("NewInstance Processor Error", e);
			} catch (NoSuchMethodException e) {
				throw new NInvalidParamException("NewInstance Processor Error", e);
			} catch (InstantiationException e) {
				throw new NInvalidParamException("NewInstance Processor Error", e);
			} catch (IllegalAccessException e) {
				throw new NInvalidParamException("NewInstance Processor Error", e);
			}
			// CustomParamProcessor cp =
			// this.getXmlClass().getAnnotation(CustomParamProcessor.class);

			try {
				xmlStr = new String(postValue, "UTF-8");
				xmlStr = xmlStr.trim();

				/**
				 * 如果是xml就构建xml，如果不是，则根据xml的class构建一个对应bean for bmw -cns
				 * 
				 * edit use CustomParamProcessor to do it
				 */

				// if (!xmlStr.startsWith("<")&&!xmlStr.startsWith("{")) {
				// xmlStr = getBeanByString(xmlStr);
				// }
				Class<? extends IXmlParams> clazz = getXmlClass();

				if (xmlStr.startsWith("<")) {
					XStream stream = new XStream();
					stream.processAnnotations(clazz);
					xmlParams = (IXmlParams) stream.fromXML(xmlStr);
				} else if (xmlStr.startsWith("{")) {
					xmlParams = mapper.readValue(xmlStr, clazz);
				} else {
					throw new NInvalidParamException("can not parse the request body. not xml or json");
				}
			} catch (UnsupportedEncodingException e) {
				throw new NInvalidParamException("requestBody-xml:transform xml error", e);
			} catch (RuntimeException e) {
				throw new NInvalidParamException("requestBody-xml:parse xml error", e);
			} catch (JsonParseException e) {
				throw new NInvalidParamException("requestBody-json:transform json error", e);
			} catch (JsonMappingException e) {
				throw new NInvalidParamException("requestBody-json:transform json error", e);
			} catch (IOException e) {
				throw new NInvalidParamException("requestBody-json:transform json error", e);
			}
		}
	}

	// /**
	// * 根据post传入的string 构建xmlbean
	// *
	// * @param args
	// * @return
	// * @throws InstantiationException
	// * @throws IllegalAccessException
	// * @throws SecurityException
	// */
	// private String getBeanByString(String args) {
	// StringBuffer str = new StringBuffer();
	// String[] params = args.split("&");
	// str.append("<GIMRequest>");
	// for (String param : params) {
	// String[] temp = param.split("=");
	// if (temp.length != 2) {
	// continue;
	// }
	// str.append("<").append(temp[0]).append(">").append(temp[1]).append("</").append(temp[0]).append(">");
	// }
	//
	// str.append("</GIMRequest>");
	// return str.toString();
	// }

	public IXmlParams getXmlParams() {
		return xmlParams;
	}

	public void setXmlParams(IXmlParams xmlParams) {
		this.xmlParams = xmlParams;
	}

	public byte[] getPostValue() {
		return postValue;
	}

	public String getXmlStr() {
		return xmlStr;
	}

	public abstract Class<? extends IXmlParams> getXmlClass();

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri.replaceAll(" ", "%20");
		// try {
		// this.uri= URLEncoder.encode(uri.replaceAll(" ", "%20"),"UTF-8");
		// }
		// catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
	}
}
