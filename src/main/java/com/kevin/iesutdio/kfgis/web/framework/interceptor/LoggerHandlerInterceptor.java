package com.kevin.iesutdio.kfgis.web.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kevin.iesutdio.kfgis.web.framework.contant.FrameworkGISContants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kevin.iesutdio.kfgis.bean.RequestBean;

/**
 * Created on 2012-11-28
 * <p>Title: WEB-T GIS核心系统_服务_日志参数距离模块_A</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author fengheliang fenghlkevin@gmail.com
 * @version 1.0
 * @update 修改日期 修改描述
 */
public class LoggerHandlerInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(LoggerHandlerInterceptor.class);
    /**
     * <p>Discription:[记录日志信息]</p>
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     * @author:fengheliang
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        RequestBean rb = (RequestBean) session.getAttribute(FrameworkGISContants.KEY_LOGGER_REQUESTBEAN);
        Long timestamp = System.currentTimeMillis();
        
        //在frameworkServlet类中对日志时间进行记录
        //rb.setRequestTime(System.currentTimeMillis());
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
                
                /**
                 * 当存在adcode存在时需要增加到日志当中
                 */
                if("adcode".equals(key)){
                    rb.setAcode(request.getParameter(key));
                }
            }
        }
        
        rb.setUri(uri + params);
        /**
         * 获取enType开始位置
         */
        int enTypeStart = uri.indexOf("gis/") + 4;
        
        String str = uri.substring(enTypeStart);
        int enTypeEnd = str.indexOf("/");
        if(enTypeEnd == -1){
            enTypeEnd = str.indexOf(".");
        }
        
        /**
         * 获取第一部分为entype值
         */
        String enType = "";
        
        /**
         * 如果找不到合适的结尾符号，则将整个字符串作为entype
         */
        if(enTypeEnd == -1){
            enType = str;
        }else{
            enType = str.substring(0,enTypeEnd);
        }
        
        /**
         * 获取encodeid
         */
        int endFlag = str.indexOf(".");
        String enCodeId = "";
        if(endFlag == -1){
            enCodeId = str;
        }else{
            enCodeId = str.substring(0, endFlag);
        }
        
        rb.setEntype(enType);
        rb.setEnCodeId(enCodeId);
        
        /**
         * 构造responseID
         */
        StringBuffer responseID = new StringBuffer(200);
        responseID.append(request.getSession().getId());
        if (logger.isDebugEnabled()) {
            logger.debug("获取ResponseID值 [{}]", responseID.toString());
        }
        
        rb.setRequestID(responseID.toString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        HttpSession session = request.getSession();
//        RequestBean rb = (RequestBean) session.getAttribute(FrameworkGISContants.KEY_LOGGER_REQUESTBEAN);
//        rb.setEnResponseTime(System.currentTimeMillis());
//        rb.setEnRtTime(rb.getEnResponseTime() - rb.getEnRequestTime());
    }

    /**
     * 
     * <p>Discription:[记录应答信息]</p>
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     * @author:fengheliang
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        
        /*HttpSession session = request.getSession();
        RequestBean rb = (RequestBean) session.getAttribute(FrameworkGISContants.KEY_LOGGER_REQUESTBEAN);
        rb.setResponseTime(System.currentTimeMillis());*/
        //记录应答内容
        /*byte[] bs = (byte[]) request.getSession().getAttribute(FrameworkGISContants.CONTANT_RESONSE_ATTRIBUTE);
        bs = (byte[])request.getAttribute(FrameworkGISContants.CONTANT_RESONSE_ATTRIBUTE);
        if(bs != null){
            rb.setExceptionCo(new String(bs, System.getProperty(FrameworkGISContants.SERVICE_KEY_LOGENCODING)));
            rb.setSuccessFlag("0");
        }else{
            rb.setSuccessFlag("1");
        }*/
    }

}
