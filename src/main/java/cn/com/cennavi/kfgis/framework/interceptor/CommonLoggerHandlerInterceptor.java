package cn.com.cennavi.kfgis.framework.interceptor;

import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.com.cennavi.kfgis.bean.RequestBean;
import cn.com.cennavi.kfgis.bean.ResponseBean;
import cn.com.cennavi.kfgis.framework.contant.FrameworkGISContants;
import cn.com.cennavi.kfgis.util.SBase64;

public class CommonLoggerHandlerInterceptor extends HandlerInterceptorAdapter {

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		//session.setMaxInactiveInterval(15);
		RequestBean rb = (RequestBean) session.getAttribute(FrameworkGISContants.KEY_LOGGER_REQUESTBEAN);
		Long timestamp = System.currentTimeMillis();

		/**
		 * 储存IP地址
		 */
		String uri = request.getRequestURI();
		rb.setIpAddr(request.getRemoteAddr());

		StringBuffer params = new StringBuffer(200);
		String[] paramkeys = (String[]) request.getParameterMap().keySet().toArray(new String[0]);
		if (paramkeys.length > 0) {
			params.append("?1=").append(timestamp);
			for (String key : paramkeys) {
				params.append("&").append(key).append("=").append(request.getParameter(key));
			}
		}

		rb.setUri(uri + params);
		String enType = "";
		int endTypeIndex = uri.lastIndexOf("/");
		if (endTypeIndex == -1 || endTypeIndex + 1 > uri.length()) {
			enType = uri;
		} else {
			int endXIndex = uri.lastIndexOf("?");
			if (endXIndex < 0 || endXIndex <= endTypeIndex) {
				enType = uri.substring(endTypeIndex + 1);
			} else {
				enType = uri.substring(endTypeIndex + 1, endXIndex);
			}
		}

		rb.setEntype(enType);
		rb.setEnCodeId(enType);

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		HttpSession session = request.getSession();
		Object o = request.getAttribute(FrameworkGISContants.CONTANT_RESONSE_ATTRIBUTE);
		ResponseBean rsb = (ResponseBean) session.getAttribute(FrameworkGISContants.KEY_LOGGER_RESPONSEBEAN);
		if (o != null) {
			if (o instanceof String) {
				rsb.setResponse((String) o);
			} else if (o instanceof byte[]) {
				//rsb.setResponse(new String((byte[]) o));
				rsb.setResponse(SBase64.encode((byte[]) o));
			}
			RequestBean rb = (RequestBean) session.getAttribute(FrameworkGISContants.KEY_LOGGER_REQUESTBEAN);
			rb.setResponseSize(rsb.getResponse().length());
		}
	}

}
