package com.kevin.iesutdio.kfgis.web.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kevin.iesutdio.kfgis.bean.Response;
import com.kevin.iesutdio.kfgis.bean.param.IResult;
import com.kevin.iesutdio.kfgis.web.framework.contant.FrameworkGISContants;
import com.kevin.iesutdio.kfgis.web.framework.contant.FrameworkErrorCodeContants;
import org.codehaus.jettison.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kevin.iesutdio.kfgis.bean.RequestBean;

/**
 * Created on 2012-11-28
 * <p>Title: WEB-T GIS核心系统_XX服务_应答结构构件模块</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author fengheliang fenghlkevin@gmail.com
 * @version 1.0
 * @update 修改日期 修改描述
 */
public class ResponseHandlerInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //根据应答信息构建应答对象
        long statusCode = new Long(FrameworkErrorCodeContants.SUCCESS_CODE_120000);
        
        RequestBean rb=(RequestBean) request.getSession().getAttribute(FrameworkGISContants.KEY_LOGGER_REQUESTBEAN);
        
//        Response rs =new Response();
        
        /**
         * 是返回的string字符串
         */
        if(modelAndView.getModel().get(FrameworkGISContants.SERVICE_KEY_RESULT) == null){
            /**
             * 拼接外层字符串 并返回
             */
           String temp=modelAndView.getViewName();
           if(temp!=null&&temp.indexOf("{")==0){
               JSONObject json=new JSONObject(temp);
               JSONObject res=(JSONObject) json.get("response");
               res.put("responseID", rb.getRequestID());
               modelAndView.getModel().put(FrameworkGISContants.SERVICE_KEY_RESPONSE, json.toString());
           }else if(temp!=null&&temp.indexOf("<?")==0){
               Document doc = DocumentHelper.parseText(temp);
               Element obj=(Element) doc.selectObject("//response//responseID");
               obj.setText(rb.getRequestID());
               modelAndView.getModel().put(FrameworkGISContants.SERVICE_KEY_RESPONSE, doc.asXML());
           }else{
               Response rs =new Response();
               statusCode = new Long(FrameworkErrorCodeContants.ERROR_CODE_150001);
               rs.setStatusCode(statusCode);
               rs.setResponseID(rb.getRequestID());
               modelAndView.getModel().put(FrameworkGISContants.SERVICE_KEY_RESPONSE, rs);
           }
        }else{
            Response rs =new Response();
            IResult result = (IResult) modelAndView.getModel().get(FrameworkGISContants.SERVICE_KEY_RESULT);
            rs.setResult(result);
            rs.setStatusCode(statusCode);
            rs.setResponseID(rb.getRequestID());
            modelAndView.getModel().put(FrameworkGISContants.SERVICE_KEY_RESPONSE, rs);
        }
        
        modelAndView.getModel().remove(FrameworkGISContants.SERVICE_KEY_RESULT);
    }
}
