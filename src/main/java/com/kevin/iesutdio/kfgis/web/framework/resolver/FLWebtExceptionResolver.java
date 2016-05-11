package com.kevin.iesutdio.kfgis.web.framework.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kevin.iesutdio.kfgis.bean.Response;
import com.kevin.iesutdio.kfgis.web.framework.contant.FrameworkGISContants;
import com.kevin.iesutdio.kfgis.web.framework.exception.NBaseServiceException;
import org.springframework.web.servlet.ModelAndView;

import com.kevin.iesutdio.kfgis.bean.RequestBean;

/**
 * Created on 2012-11-29
 * <p>Title: WEB-T GIS核心系统_公共服务_错误信息拦截器</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author fengheliang fenghlkevin@gmail.com
 * @version 1.0
 * @update 修改日期 修改描述
 */
public class FLWebtExceptionResolver extends BaseServerCodeExceptionResolver {

    /**
     * <p>Discription:[实现http错误]</p>
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @param mv
     * @return
     * @author:fengheliang
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    @Override
    protected Head userHeadException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex, ModelAndView mv,String errorMsg) {
//        Head header=null;
//        if(ex instanceof AuthServiceInternalException){
//            int statusCode=HttpServletResponse.SC_BAD_REQUEST;
//            header=new Head();
//            header.setCode(statusCode);
//        }
//        return header;
        return null;
    }

    /**
     * <p>Discription:[实现程序错误]</p>
     * @param mv
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @param header
     * @param isDebug
     * @param errorMsg
     * @return
     * @author:fengheliang
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    @Override
    protected Object userBodyException(ModelAndView mv, HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex, Head header,
            boolean isDebug,String errorMsg) {
        Response rs=null;
        long statusCode=-1;
        if(ex instanceof NBaseServiceException){
            NBaseServiceException exx=(NBaseServiceException)ex;
            statusCode=exx.getStatusCode();
        }else{
            statusCode=150000;
        }
        
        if(statusCode!=-1){
            rs = new Response();
            RequestBean rb=(RequestBean) request.getSession().getAttribute(FrameworkGISContants.KEY_LOGGER_REQUESTBEAN);
            rs.setResponseID(rb.getRequestID());
            rs.setStatusCode(statusCode);
            
            rb.setSuccessFlag("0");
            rb.setExceptionCo(errorMsg);
            String tempMsg=errorMsg;
            if(isDebug){
                tempMsg+=this.getStackTrace(ex);
            }
            rs.setErrorMsg(tempMsg);
        }
        
        return rs;
    }

    @Override
    protected Head headException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex, ModelAndView mv, String errorMsg) {
        return null;
    }

}
