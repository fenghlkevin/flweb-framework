//package cn.com.cennavi.kfgis.framework.interceptor;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.codehaus.jackson.JsonEncoding;
//import org.codehaus.jackson.JsonGenerator;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import FrameworkGISContants;
//import cn.com.cennavi.kfgis.framework.interceptor.annotation.ResponseDataType;
//import cn.com.cennavi.kfgis.framework.interceptor.annotation.ResponseDataType.DataType;
//
///**
// * Created on 2012-11-28
// * <p>
// * Title: WEB-T GIS核心系统_XX服务_应答结构构件模块
// * </p>
// * <p>
// * Copyright: Copyright (c) 2011
// * </p>
// * <p>
// * Company: 沈阳世纪高通科技有限公司
// * </p>
// * <p>
// * Department: 技术开发部
// * </p>
// * 
// * @author fengheliang fenghlkevin@gmail.com
// * @version 1.0
// * @update 修改日期 修改描述
// */
//public class StrResponseHandlerInterceptor extends HandlerInterceptorAdapter {
//
//	private static Map<String, ResponseDataType> resTypeMap = null;
//	
//	  private static ObjectMapper objectMapper = new ObjectMapper();
//	{
//		if (resTypeMap == null) {
//			resTypeMap = new HashMap<String, ResponseDataType>();
//		}
//		if(objectMapper==null){
//			objectMapper = new ObjectMapper();
//		}
//	}
//
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//		String view = modelAndView.getViewName();
//		ResponseDataType resDataType = resTypeMap.get(view);
//		if (resDataType == null && handler != null) {
//			Method[] methods = handler.getClass().getMethods();
//			//
//			boolean has=false;
//			for (Method method : methods) {
//				RequestMapping rm = method.getAnnotation(RequestMapping.class);
//				if (rm == null) {
//					continue;
//				}
//				String[] values = rm.value();
//				for (String v : values) {
//					if (!v.equalsIgnoreCase(view)) {
//						continue;
//					}
//					resDataType = method.getAnnotation(ResponseDataType.class);
//					if (resDataType != null) {
//						resTypeMap.put(view, resDataType);
//						has=true;
//						break;
//					}
//				}
//				if(has){
//					break;
//				}
//			}
//		}
//
//		String value = null;
//		Object objValue=modelAndView.getModel().get(FrameworkGISContants.SERVICE_KEY_RESULT);
//		if (resDataType != null) {
//			if (resDataType.value() == DataType.STR) {
//				value = modelAndView.getViewName();// can not get in this if
//			} else if (resDataType.value() == DataType.XML) {
//
//			} else if (resDataType.value() == DataType.JSON&&objValue!=null) {
//				byte[] json=this.getJson(objValue);
//				value=new String(json,"UTF-8");
//			}
//		}else{
//			//print string-->viewname
//			value=view;
//		}
//		modelAndView.getModel().put(FrameworkGISContants.SERVICE_KEY_RESPONSE, value == null ? "" : value);
//
//		// /**
//		// * 是返回的string字符串
//		// */
//		// if
//		// (modelAndView.getModel().get(FrameworkGISContants.SERVICE_KEY_RESULT)
//		// == null) {
//		//
//		// String temp = modelAndView.getViewName();
//		// modelAndView.getModel().put(FrameworkGISContants.SERVICE_KEY_RESPONSE,
//		// temp);
//		//
//		// }
//	}
//
//	private byte[] getJson(Object value) throws IOException {
//		ByteArrayOutputStream byteo = null;
//		try {
//			byteo = new ByteArrayOutputStream();
//			 JsonGenerator generator = objectMapper.getJsonFactory().createJsonGenerator(byteo, JsonEncoding.UTF8);
//             objectMapper.writeValue(generator, value);
//			  byte[] bs = byteo.toByteArray();
//			  return bs;
//		} finally {
//			if (byteo != null) {
//				byteo.close();
//			}
//		}
//	}
//}
