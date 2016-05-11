package cn.com.cennavi.kfgis.framework.view;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import cn.com.cennavi.kfgis.framework.contant.FrameworkGISContants;
import cn.com.cennavi.kfgis.framework.view.resultobj.ByteResult;
import cn.com.cennavi.kfgis.framework.view.resultobj.JsonResult;
import cn.com.cennavi.kfgis.framework.view.resultobj.StrResult;

/**
 * Created on 2012-11-28
 * <p>
 * Title: WEB-T GIS核心系统_XX服务_对象view模块
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 沈阳世纪高通科技有限公司
 * </p>
 * <p>
 * Department: 技术开发部
 * </p>
 * 
 * @author fengheliang fengheliang@cennavi.com.cn
 * @version 1.0
 * @update 修改日期 修改描述
 */
public class StrView extends AbstractView {

	public static final String DEFAULT_CONTENT_TYPE = "text/xml";

	private boolean disableCaching = true;

	private String encoding = "UTF-8";

	public StrView() {
		setContentType(DEFAULT_CONTENT_TYPE);
	}

	@Override
	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
//		if (response.getContentType() == null || "".equalsIgnoreCase(response.getContentType().trim())) {
//			response.setContentType(getContentType());
//		}
//		if (response.getCharacterEncoding() == null || "".equalsIgnoreCase(response.getCharacterEncoding().trim())) {
//			response.setCharacterEncoding(encoding);
//		}

		if (disableCaching) {
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.addDateHeader("Expires", 1L);
		}
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ByteArrayOutputStream byteo = null;
		try {

			byteo = new ByteArrayOutputStream(10240);
			Map<String, Object> str = model;
			/**
			 * controller返回的IResult对象
			 */
			Object temp = str.get(FrameworkGISContants.SERVICE_KEY_RESULT);
			String content_type=DEFAULT_CONTENT_TYPE;
			String encoding=this.encoding;
			if (temp != null) {
				byte[] reValue = new byte[0];
				if (temp instanceof ByteResult) {
					ByteResult br = (ByteResult) temp;
					content_type=br.getContent_type();
					encoding=br.getEncoding();
					reValue = br.getBytes();
				} else if (temp instanceof StrResult) {
					StrResult br = (StrResult) temp;
					content_type=br.getContent_type();
					encoding=br.getEncoding();
					reValue = br.getStrObj().getBytes();
				} else if (temp instanceof JsonResult) {
					JsonResult br = (JsonResult) temp;
					encoding=br.getEncoding();
					content_type=br.getContent_type();
					reValue = br.getJsonObj();
				}
				byteo.write(reValue);
			}
			
			response.setContentType(content_type);
			response.setCharacterEncoding(encoding);
			response.setContentLength(byteo.size());

			byte[] re = byteo.toByteArray();
			FileCopyUtils.copy(re, response.getOutputStream());
			response.flushBuffer();

			request.setAttribute(FrameworkGISContants.CONTANT_RESONSE_ATTRIBUTE, re);
		} finally {
			if (byteo != null) {
				byteo.close();
			}
		}

	}

	public boolean isDisableCaching() {
		return disableCaching;
	}

	public void setDisableCaching(boolean disableCaching) {
		this.disableCaching = disableCaching;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
