package com.kevin.iesutdio.kfgis.web.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.iestudio.framework.logwriter.realwriter.LogBuilder;

/**
 * 
 * @author fengheliang
 */
public class LogInterceptor extends HandlerInterceptorAdapter{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 初始化debug模块的日志信息
         */
        int i=handler.getClass().getName().lastIndexOf(".control");
        String te=handler.getClass().getName().substring(0, i);
        LogBuilder builder=new LogBuilder();
        loadDebugLog(te,builder);
        return true;
    }
    
    private static final String TEMPLATE_DEBUG_APPENDER = "org.apache.log4j.ConsoleAppender.file";

    private static final String PARAM_DEBUGLOG_PATH = "debug_logpath";

    private static final String PARAM_DEBUGTARGET_PATH = "debug_targetpath";

    private static final String PARAM_DEBUG_LEVELRANGEMIN = "debug_levelRangeMin";

    private static final String PARAM_DEBUG_LEVELRANGEMAX = "debug_levelRangeMax";

    private static final String FILTER_DEBUG = "cn.com.cennavi.kfgis.service";

    public void loadDebugLog(String moduleName,LogBuilder builder) {
        builder.loadLog(moduleName, "", TEMPLATE_DEBUG_APPENDER, System.getProperty(PARAM_DEBUGLOG_PATH), System.getProperty(PARAM_DEBUGTARGET_PATH),
                builder.getStrLevel(System.getProperty(PARAM_DEBUG_LEVELRANGEMAX)), builder.getStrLevel(System.getProperty(PARAM_DEBUG_LEVELRANGEMIN)), FILTER_DEBUG
                        + "." + moduleName);
    }
    
}
