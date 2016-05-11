package com.kevin.iesutdio.kfgis.web.framework.resolver;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.kevin.iesutdio.kfgis.bean.RequestBean;
import com.kevin.iesutdio.kfgis.web.framework.contant.FrameworkGISContants;
import com.kevin.iesutdio.kfgis.web.framework.exception.NInvalidParamException;
import com.kevin.iesutdio.kfgis.web.framework.exception.NNullParamException;
import com.kevin.iesutdio.kfgis.web.framework.exception.NServiceInternalException;

/**
 * Created on 2012-11-29
 * <p>Title: WEB-T GIS核心系统_异常处理服务_公共的异常处理模块</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author fengheliang fengheliang@cennavi.com.cn
 * @version 1.0
 * @update 修改日期 修改描述
 */
public abstract class BaseServerCodeExceptionResolver extends AbstractHandlerExceptionResolver {

    /**
     * <p>Discription:[执行异常处理]</p>
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     * @author:fengheliang
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = new ModelAndView();
        int statusCode = -1;
        String errorMsg = ex.getMessage();
        if ((errorMsg == null || "".equalsIgnoreCase(errorMsg)) && ex instanceof NServiceInternalException) {
            NServiceInternalException exx = (NServiceInternalException) ex;
            errorMsg=exx.getStatusMsg();
        }

        boolean isDebug = "DEBUG".equalsIgnoreCase(System.getProperty("root_outputlevel"));

        Head header = this.userHeadException(request, response, handler, ex, mv,errorMsg);
        if (header == null) {
            header = this.headException(request, response, handler, ex, mv,errorMsg);
        }

        Object obj = this.userBodyException(mv, request, response, handler, ex, header, isDebug,errorMsg);
        /**
         * 如果都不为空时，则直接使用obj对象
         */
        if (obj != null && header != null) {
            mv.getModel().put(FrameworkGISContants.SERVICE_KEY_RESPONSE, obj);
        }
        else if (obj != null && header == null) {
            mv.getModel().put(FrameworkGISContants.SERVICE_KEY_RESPONSE, obj);
        }
        else if (obj == null && header != null && header.getCode() != -1) {
            statusCode = header.getCode();
            errorMsg = header.getMsg() == null || "".equalsIgnoreCase(header.getMsg()) ? errorMsg : header.getMsg();
        }
        else {
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        
        if(statusCode!=-1){
            if (isDebug) {
                errorMsg += getStackTrace(ex);
            }
            RequestBean rb=(RequestBean) request.getSession().getAttribute(FrameworkGISContants.KEY_LOGGER_REQUESTBEAN);
            rb.setExceptionCo(errorMsg);
            rb.setSuccessFlag("0");
            try {
                response.sendError(statusCode, errorMsg);
            }
            catch (IOException e) {
                logger.error("edit Response statusCode error", e);
                rb.setExceptionCo("edit Response statusCode error");
            }
            logger.error(String.valueOf(statusCode), ex);
        }

        return mv;
    }

    protected String getStackTrace(Exception ex) {
        StringWriter b = new StringWriter();
        PrintWriter pw = new PrintWriter(b);
        ex.printStackTrace(pw);
        String errorMsg = "\n\t " + b.getBuffer().toString();
        pw.close();
        return errorMsg;
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @param mv
     * @param isDebug
     * @return
     * @author:fengheliang
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    protected abstract Head userHeadException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex, ModelAndView mv,String errorMsg);

    /**
     * <p>Discription:[通用的头错误码]</p>
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @param mv
     * @param isDebug
     * @return
     * @author:fengheliang
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    protected Head headException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex, ModelAndView mv,String errorMsg) {
        Head head = null;

        int statusCode = -1;
        String tempMsg = null;

        if (ex instanceof NNullParamException) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            tempMsg = "Missing mandatory parameters." + errorMsg;
        }
        else if (ex instanceof NInvalidParamException) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            tempMsg = "Invalid parameters" + errorMsg;
        }
        else if (ex instanceof java.lang.IllegalArgumentException) {
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        else if (ex instanceof NServiceInternalException) {
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }

        if (statusCode != -1) {
            head = new Head();
            head.setCode(statusCode);
            head.setMsg(tempMsg);
        }

        return head;
    }

    /**
     * <p>Discription:[实现程序本身的拦截规则]</p>
     * @param mv
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     * @author:fengheliang
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    protected abstract Object userBodyException(ModelAndView mv, HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex,
            Head header, boolean isDebug,String errorMsg);

    /**
     * Created on 2012-11-29
     * <p>Title: WEB-T GIS核心系统_XX服务_头校验返回结果</p>
     * <p>Copyright: Copyright (c) 2011</p>
     * <p>Company: 沈阳世纪高通科技有限公司</p>
     * <p>Department: 技术开发部</p>
     * 
     * @author fengheliang fengheliang@cennavi.com.cn
     * @version 1.0
     * @update 修改日期 修改描述
     */
    protected static class Head {

        public Head() {
        }

        /**
         * <p>Description:[应答头错误码]</p>
         */
        private int code;

        /**
         * <p>Description:[应答头描述]</p>
         */
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
