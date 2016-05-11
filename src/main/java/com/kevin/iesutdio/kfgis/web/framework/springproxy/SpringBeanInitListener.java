package com.kevin.iesutdio.kfgis.web.framework.springproxy;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Created on 2011-12-19
 * <p>Title: WEB-T GIS核心系统_加载数据库中的参数配置信息</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author Kevin Feng fengheliang@cennavi.com.cn
 * @version 1.0
 * @update 修改日期 修改描述
 */
public class SpringBeanInitListener implements ServletContextListener {

	private static Logger logger=LoggerFactory.getLogger(SpringBeanInitListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	logger.info("load spring bean proxy");
    	SpringBeanProxy.setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()));
    	
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
