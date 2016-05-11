package com.kevin.iesutdio.kfgis.web.framework.springproxy;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringBeanInitServlet  extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3268927464647978188L;

	public void init(ServletConfig arg0) throws ServletException {
        SpringBeanProxy.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext()));
    }
}