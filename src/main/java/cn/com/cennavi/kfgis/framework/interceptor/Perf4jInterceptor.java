package cn.com.cennavi.kfgis.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.iestudio.framework.logwriter.realwriter.LogBuilder;

/**
 * @author fengheliang
 *@deprecated
 */
@Deprecated
public class Perf4jInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	    /**
	     * 初始化模块性能日志
	     */
        Logger perf4jLogger=LoggerFactory.getLogger(this.getClass().getName());
        if(perf4jLogger.isInfoEnabled()){
            /**
             * 如果是INfo模式，则加载混合性能日志
             */
            int i=handler.getClass().getName().lastIndexOf(".control");
            String service=handler.getClass().getName().substring(0, i);
            
            String moduleName=this.getClass().getName();
            if(perf4jLogger.isDebugEnabled()){
                /**
                 * 如果是debug模式，则加载各模块性能日志
                 * logger名称是服务包名
                 */
                LogBuilder builder=new LogBuilder();
                moduleName=this.getClass().getName()+"."+service;
                this.loadPerf4jLog(moduleName,builder);
                perf4jLogger=LoggerFactory.getLogger(moduleName);
            }
            StopWatch stopWatch = new Slf4JStopWatch(perf4jLogger);
            stopWatch.start(service,"preHandle");
            request.setAttribute("stopWatch", stopWatch);
            request.setAttribute("serviceName", moduleName);
        }
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	    StopWatch sw=(StopWatch) request.getAttribute("stopWatch");
        String serviceName=(String) request.getAttribute("serviceName");
        if(sw!=null&&serviceName!=null){
            sw.lap(serviceName,"postHandle");
        }
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	    StopWatch sw=(StopWatch) request.getAttribute("stopWatch");
	    String serviceName=(String) request.getAttribute("serviceName");
	    if(sw!=null&&serviceName!=null){
	        request.removeAttribute("stopWatch");
	        request.removeAttribute("serviceName");
	        sw.stop(serviceName,"afterCompletion");
	    }
	}
	
	public void loadPerf4jLog(String moduleName,LogBuilder builder) {
        builder.loadLog(moduleName, TEMPLATE_PERF4J_LOGGER, TEMPLATE_PERF4J_APPENDER, System.getProperty(PARAM_PERF4JLOG_PATH),
                System.getProperty(PARAM_PERF4JTARGET_PATH), builder.getStrLevel(System.getProperty(PARAM_PERF4J_LEVELRANGEMIN)),
                builder.getStrLevel(System.getProperty(PARAM_PERF4J_LEVELRANGEMAX)), moduleName);
    }
	
    private static final String TEMPLATE_PERF4J_LOGGER = "cn.com.cennavi.kfgis.framework.interceptor.Perf4jInterceptor";

    private static final String TEMPLATE_PERF4J_APPENDER = "org.apache.log4j.ConsoleAppender.file.perf4j";

    private static final String PARAM_PERF4JLOG_PATH = "perf4j_module_logpath";

    private static final String PARAM_PERF4JTARGET_PATH = "perf4j_module_targetpath";

    private static final String PARAM_PERF4J_LEVELRANGEMIN = "perf4j_levelRangeMin";

    private static final String PARAM_PERF4J_LEVELRANGEMAX = "perf4j_levelRangeMax";

}
