//package cn.com.cennavi.kfgis.framework.view;
//
//import java.io.ByteArrayOutputStream;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.util.FileCopyUtils;
//import org.springframework.web.servlet.view.AbstractView;
//
//import cn.com.cennavi.kfgis.framework.contant.FrameworkGISContants;
//import cn.com.cennavi.kfgis.util.ObjectUtil;
//import cn.com.cennavi.kfgis.util.SBase64;
//
///**
// * Created on 2012-11-28
// * <p>
// * Title: WEB-T GIS核心系统_XX服务_对象view模块
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
// * @author fengheliang fengheliang@cennavi.com.cn
// * @version 1.0
// * @update 修改日期 修改描述
// */
//public class ObjectView extends AbstractView {
//
//	public static final String DEFAULT_CONTENT_TYPE = "text/xml";
//
//	private boolean disableCaching = true;
//
//	private String encoding = "UTF-8";
//
//	public ObjectView() {
//		setContentType(DEFAULT_CONTENT_TYPE);
//	}
//
//	@Override
//	protected void prepareResponse(HttpServletRequest request,
//			HttpServletResponse response) {
//		response.setContentType(getContentType());
//		response.setCharacterEncoding(encoding);
//		if (disableCaching) {
//			response.addHeader("Pragma", "no-cache");
//			response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
//			response.addDateHeader("Expires", 1L);
//		}
//	}
//
//	@Override
//	protected void renderMergedOutputModel(Map<String, Object> model,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		ByteArrayOutputStream byteo = null;
//		try {
//			/**
//			 * 在应答的结果外层包一层callback
//			 */
//
//			byteo = new ByteArrayOutputStream(10240);
//			Map<String, Object> str = model;
//			Object temp = str.get(FrameworkGISContants.SERVICE_KEY_RESPONSE);
//			if (temp != null) {
//				if (temp instanceof String) {
//					byteo.write(((String) temp).getBytes());
//				} else {
//					byte[] tempRes = ObjectUtil.getObjectBytes(temp);
//					String temp64 = SBase64.encode(tempRes);
//					byteo.write(temp64.getBytes());
//				}
//			}
//
//			response.setContentType(getContentType());
//			response.setContentLength(byteo.size());
//
//			byte[] re = byteo.toByteArray();
//			FileCopyUtils.copy(re, response.getOutputStream());
//			response.flushBuffer();
//
//			request.getSession().setAttribute(
//					FrameworkGISContants.CONTANT_RESONSE_ATTRIBUTE, re);
//		} finally {
//			if (byteo != null) {
//				byteo.close();
//			}
//		}
//
//	}
//
//	public boolean isDisableCaching() {
//		return disableCaching;
//	}
//
//	public void setDisableCaching(boolean disableCaching) {
//		this.disableCaching = disableCaching;
//	}
//
//	public String getEncoding() {
//		return encoding;
//	}
//
//	public void setEncoding(String encoding) {
//		this.encoding = encoding;
//	}
//
//}
