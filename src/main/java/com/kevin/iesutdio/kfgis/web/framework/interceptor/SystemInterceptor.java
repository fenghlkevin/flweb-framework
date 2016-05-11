//package cn.com.cennavi.kfgis.framework.interceptor;
//
//import java.io.Serializable;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.servlet.HandlerMapping;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import com.iestudio.framework.logwriter.logitem.impl.AssemblyLogItemImpl;
//
//import Response;
//import IImageResult;
//import IResult;
//import FrameworkErrorCodeContants;
//import FrameworkGISContants;
//
///**
// * Created on 2011-12-20
// * <p>Title: WEB-T GIS核心系统_拦截器_请求与应答结果的拦截器，用于记录请求内容，应答内容，服务日志</p>
// * <p>Copyright: Copyright (c) 2011</p>
// * <p>Company: 沈阳世纪高通科技有限公司</p>
// * <p>Department: 技术开发部</p>
// * 
// * @author Kevin Feng fenghlkevin@gmail.com
// * @version 1.0
// * @update 修改日期 修改描述
// */
//public class SystemInterceptor extends HandlerInterceptorAdapter {
//
//    private Logger logger = LoggerFactory.getLogger(SystemInterceptor.class);
//
//    private org.apache.log4j.Logger requestlogger = org.apache.log4j.Logger.getLogger("cn.com.cennavi.kfgis.requestlog");
//
//    private org.apache.log4j.Logger serviceFlowlogger = org.apache.log4j.Logger.getLogger("cn.com.cennavi.kfgis.serviceFlowlog");
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //构建系统的request id
//        Long timestamp = System.currentTimeMillis();
//        
//        Map<String, String> uriTemplateVariables =null;
//        String token=null;
//        if(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)!=null){
//            uriTemplateVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
//            token = uriTemplateVariables.get(FrameworkGISContants.SERVICE_KEY_TOKEN);
//        }
//        if (token == null || "".equalsIgnoreCase(token.trim())) {
//            token="default";
//            //throw new IllegalArgumentException("Param [token] can not be null");
//        }
//        StringBuffer responseID = new StringBuffer();
//        responseID.append(token).append("_").append(request.getSession().getId()).append("_").append(timestamp).append("_").append(System.nanoTime());
//        if (logger.isDebugEnabled()) {
//            logger.debug("获取ResponseID值 [{}]", responseID.toString());
//        }
//
//        // 对用户权限进行判断 TODO 去掉token校验
////        if (authUser.getAuthUser().get(token) == null) {
////            throw new IllegalArgumentException("[bzcode] " + token + " can not use in service");
////        }
//        
//        
////      request.setAttribute(GISContants.SERVICE_KEY_TOKEN_TEMP, authUser.getAuthUser().get(token));
//        request.setAttribute(FrameworkGISContants.SERVICE_KEY_TOKEN_TEMP, token);
//
//        if (requestlogger.isInfoEnabled()) {
//            //记录请求日志
//            if (logger.isDebugEnabled()) {
//                logger.debug("记录请求与应答内容");
//            }
//            StringBuffer uri = new StringBuffer();
//            uri.append(request.getServletPath());
////            /**
////             * 如果请求至智能提示接口，则不记录日志
////             */
////            if (uri.toString().contains("smarttips")) {
////                return true;
////            }
//
//            String[] paramkeys = (String[]) request.getParameterMap().keySet().toArray(new String[0]);
//            if (paramkeys.length > 0) {
//                uri.append("?1=").append(timestamp);
//                for (String key : paramkeys) {
//                    uri.append("&").append(key).append("=").append(request.getParameter(key));
//                }
//            }
//            RequestBean rb = new RequestBean();
//            rb.setTime(timestamp);
//            rb.setRequestID(responseID.toString());
//            rb.setOthers(null);
//            rb.setUri(uri.toString());
//
//            rb.setType("REQUEST");
//            requestlogger.info(rb);
//        }
//
//        request.setAttribute(FrameworkGISContants.SERVICE_KEY_RESPONSEID, responseID.toString());
//
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        //根据应答信息构建应答对象
//        long statusCode = new Long(FrameworkErrorCodeContants.SUCCESS_CODE_120000);
//        Response rs = new Response();
//        /**
//         * IResult 为 modelandview中自己存储的，该对象为controller中 方法的返回值类对象名
//         */
//        if (modelAndView.getModel().get(FrameworkGISContants.SERVICE_KEY_RESULT) == null) {
//            statusCode = new Long(FrameworkErrorCodeContants.ERROR_CODE_150001);
//        }
//        else {
//            IResult result = (IResult) modelAndView.getModel().get(FrameworkGISContants.SERVICE_KEY_RESULT);
//            rs.setResult(result);
//        }
//        rs.setStatusCode(statusCode);
//        rs.setResponseID((String) request.getAttribute(FrameworkGISContants.SERVICE_KEY_RESPONSEID));
//        modelAndView.getModel().remove(FrameworkGISContants.SERVICE_KEY_RESULT);
//
//        if (rs.getResult() instanceof IImageResult) {
//            IImageResult res = (IImageResult) rs.getResult();
//            modelAndView.getModel().put(FrameworkGISContants.SERVICE_KEY_RESPONSE, res);
//        }
//        else {
//            modelAndView.getModel().put(FrameworkGISContants.SERVICE_KEY_RESPONSE, rs);
//        }
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        if (requestlogger.isInfoEnabled()) {
//            //记录应答内容
//            byte[] bs = (byte[]) request.getAttribute(FrameworkGISContants.SERVICE_RESONSE_ATTRIBUTE);
//            RequestBean rb = new RequestBean();
//            rb.setTime(System.currentTimeMillis());
//            rb.setRequestID((String) request.getAttribute(FrameworkGISContants.SERVICE_KEY_RESPONSEID));
//            rb.setOthers(null);
//
//            rb.setUri(bs != null ? new String(bs, System.getProperty(FrameworkGISContants.SERVICE_KEY_LOGENCODING)) : "null response");
//
//            /**
//             * 如果请求至智能提示接口，则不记录日志
//             */
////            if (rb.getUri().contains("smarttips")) {
////                //return;
////            }
////            else {
//                rb.setType("RESPONSE");
//                rb.setUserid((String) request.getAttribute(FrameworkGISContants.SERVICE_KEY_TOKEN_TEMP));
//                requestlogger.info(rb);
////            }
//        }
//
//        if (serviceFlowlogger.isInfoEnabled()) {
//            //记录流量日志 TODO
//        }
//
//        request.removeAttribute(FrameworkGISContants.SERVICE_KEY_TOKEN);
//        request.removeAttribute(FrameworkGISContants.SERVICE_KEY_TOKEN_TEMP);
//        request.removeAttribute(FrameworkGISContants.SERVICE_KEY_RESPONSEID);
//        request.removeAttribute(FrameworkGISContants.SERVICE_RESONSE_ATTRIBUTE);
//    }
//
//    /**
//     * Created on 2011-12-25
//     * <p>Title: WEB-T GIS核心系统_系统服务_请求应答内容记录</p>
//     * <p>Copyright: Copyright (c) 2011</p>
//     * <p>Company: 沈阳世纪高通科技有限公司</p>
//     * <p>Department: 技术开发部</p>
//     * 
//     * @author Kevin Feng fenghlkevin@gmail.com
//     * @version 1.0
//     * @update 修改日期 修改描述
//     */
//    public static final class RequestBean extends AssemblyLogItemImpl implements Serializable {
//
//        /**
//         * <p>Description:[字段功能描述]</p>
//         */
//        private static final long serialVersionUID = 2752316944773054841L;
//
//        /**
//         * <p>Description:[请求id]</p>
//         */
//        private String requestID;
//
//        /**
//         * <p>Description:[当前时间]</p>
//         */
//        private Long time;
//
//        /**
//         * <p>Description:[请求uri]</p>
//         */
//        private String uri;
//
//        /**
//         * <p>Description:[其他描述字段]</p>
//         */
//        private String others;
//
//        /**
//         * <p>Description:[请求类型]</p>
//         */
//        private String type;
//
//        /**
//         * <p>Description:[请求用户id]</p>
//         */
//        private String userid;
//
//        public String getUserid() {
//            return userid;
//        }
//
//        public void setUserid(String userid) {
//            this.userid = userid;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public String getRequestID() {
//            return requestID;
//        }
//
//        public void setRequestID(String requestID) {
//            this.requestID = requestID;
//        }
//
//        public Long getTime() {
//            return time;
//        }
//
//        public void setTime(Long time) {
//            this.time = time;
//        }
//
//        public String getUri() {
//            return uri;
//        }
//
//        public void setUri(String uri) {
//            this.uri = uri;
//        }
//
//        public String getOthers() {
//            return others;
//        }
//
//        public void setOthers(String others) {
//            this.others = others;
//        }
//    }
//
//    /**
//     * Created on 2011-12-25
//     * <p>Title: WEB-T GIS核心系统_系统服务_服务流量bean</p>
//     * <p>Copyright: Copyright (c) 2011</p>
//     * <p>Company: 沈阳世纪高通科技有限公司</p>
//     * <p>Department: 技术开发部</p>
//     * 
//     * @author Kevin Feng fenghlkevin@gmail.com
//     * @version 1.0
//     * @update 修改日期 修改描述
//     */
//    @SuppressWarnings("unused")
//    private static final class ServiceFlowBean extends AssemblyLogItemImpl implements Serializable {
//
//        /**
//         * <p>Description:[字段功能描述]</p>
//         */
//        private static final long serialVersionUID = 8858582242987237484L;
//
//        private String requestID;
//
//        private String requestType;
//
//        private String userID;
//
//        private String serviceID;
//
//        private long requestTime;
//
//        private long responseTime;
//
//        private String responseType;
//
//        private String responseLen;
//
//        private String responseCode;
//
//        private boolean isSucces;
//
//        public String getRequestID() {
//            return requestID;
//        }
//
//        public void setRequestID(String requestID) {
//            this.requestID = requestID;
//        }
//
//        public String getRequestType() {
//            return requestType;
//        }
//
//        public void setRequestType(String requestType) {
//            this.requestType = requestType;
//        }
//
//        public String getUserID() {
//            return userID;
//        }
//
//        public void setUserID(String userID) {
//            this.userID = userID;
//        }
//
//        public String getServiceID() {
//            return serviceID;
//        }
//
//        public void setServiceID(String serviceID) {
//            this.serviceID = serviceID;
//        }
//
//        public long getRequestTime() {
//            return requestTime;
//        }
//
//        public void setRequestTime(long requestTime) {
//            this.requestTime = requestTime;
//        }
//
//        public long getResponseTime() {
//            return responseTime;
//        }
//
//        public void setResponseTime(long responseTime) {
//            this.responseTime = responseTime;
//        }
//
//        public String getResponseType() {
//            return responseType;
//        }
//
//        public void setResponseType(String responseType) {
//            this.responseType = responseType;
//        }
//
//        public String getResponseLen() {
//            return responseLen;
//        }
//
//        public void setResponseLen(String responseLen) {
//            this.responseLen = responseLen;
//        }
//
//        public String getResponseCode() {
//            return responseCode;
//        }
//
//        public void setResponseCode(String responseCode) {
//            this.responseCode = responseCode;
//        }
//
//        public boolean isSucces() {
//            return isSucces;
//        }
//
//        public void setSucces(boolean isSucces) {
//            this.isSucces = isSucces;
//        }
//
//    }
//
////    public static void initAuthUser() {
////        authUser = (AuthBean) ConfigureFactory.getInstance().getConfigure(AuthBean.class);
////    }
////
////    private static AuthBean authUser;
//
//    /**
//     * 
//     * Created on 2012-10-26
//     * <p>Title: WEB-T GIS核心系统_auth服务_用户bzcode存储</p>
//     * <p>Copyright: Copyright (c) 2011</p>
//     * <p>Company: 沈阳世纪高通科技有限公司</p>
//     * <p>Department: 技术开发部</p>
//     * 
//     * @author Kevin Kevin@cennavi.com.cn
//     * @version 1.0
//     * @update 修改日期 修改描述
//     */
////    public static class AuthBean {
////
////        private Map<String, String> authUser;
////
////        public Map<String, String> getAuthUser() {
////            return authUser;
////        }
////
////        public void setAuthUser(Map<String, String> authUser) {
////            this.authUser = authUser;
////        }
////
////    }
//
//}
