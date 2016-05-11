package com.kevin.iesutdio.kfgis.web.framework.control;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kevin.iesutdio.kfgis.bean.param.IResult;
import com.kevin.iesutdio.kfgis.web.framework.annotation.FieldTypeValid;
import com.kevin.iesutdio.kfgis.web.framework.annotation.RestBeanVariable;
import com.kevin.iesutdio.kfgis.web.framework.contant.FrameworkErrorMsgContants;
import com.kevin.iesutdio.kfgis.web.framework.contant.FrameworkGISContants;
import com.kevin.iesutdio.kfgis.web.framework.exception.NInvalidParamException;
import com.kevin.iesutdio.kfgis.web.framework.exception.NNullParamException;
import com.kevin.iesutdio.kfgis.web.framework.util.RequestUtil;
import com.kevin.iesutdio.kfgis.web.framework.validator.AnnotationValidator;
import com.kevin.iesutdio.kfgis.web.framework.annotation.NotNullValid;
import com.kevin.iesutdio.kfgis.web.framework.exception.NBaseServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.kevin.iesutdio.kfgis.bean.RequestBean;
import com.kevin.iesutdio.kfgis.bean.param.AbstractParams;
import com.kevin.iesutdio.kfgis.bean.param.IParams;
import com.kevin.iesutdio.kfgis.web.framework.annotation.ReturnObject;
import com.kevin.iesutdio.kfgis.web.framework.exception.NServiceInternalException;

import com.iestudio.file.FileUtil;

/**
 * Created on 2012-9-13
 * <p>
 * Title: BMW CNS系统_Root Controller
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 沈阳世纪高通科技有限公司
 * </p>
 * <p>
 * Department: 技术开发部
 * </p>
 * 
 * @author Kevin Kevin@cennavi.com.cn
 * @version 1.0
 * @update 修改日期 修改描述
 */
public abstract class RootController {

    private static final Logger logger = LoggerFactory.getLogger(RootController.class);

    /**
     * <p>
     * Description:[RootHandler]
     * </p>
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @author:Kevin
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
    public IResult rootHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object[] controller = this.getController(request, response);

        if (controller == null) {
            throw new NServiceInternalException("未找到合适的RootController");
        }

        Class<?> control = (Class<?>) controller[0];
        Method method = (Method) controller[1];
        RequestMapping rm = (RequestMapping) controller[2];
        boolean passed = false;
        for (RequestMethod requestMethod : rm.method()) {
            if (requestMethod.toString().equalsIgnoreCase(request.getMethod())) {
                passed = true;
            }
        }
        if (!passed) {
            throw new NServiceInternalException("类 " + control.getName() + " 执行 Method [" + method.getName() + "] 不支持 [" + request.getMethod() + "] 方法.");
        }

        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();

        /**
         * method 先获取ReturnObject的注解，注解无值时候使用returnTYpe
         */
        Class<?> errorClazz = null;
        ReturnObject ro = method.getAnnotation(ReturnObject.class);
        if (ro != null) {
            Class<?> error = ro.error();
            if (!error.getName().equalsIgnoreCase(Object.class.getName())) {
                errorClazz = error;
            }
        }
        if (errorClazz == null) {
            errorClazz = method.getReturnType();
        }

        if (!errorClazz.isInterface()) {
            try {
                request.getSession().setAttribute(FrameworkGISContants.SERVICE_KEY_RESPONSE, errorClazz.newInstance());
            } catch (InstantiationException e) {
                throw new NServiceInternalException("类 " + control.getName() + " 实例化应答对象时异常.", e);
            } catch (IllegalAccessException e) {
                throw new NServiceInternalException("类 " + control.getName() + " 实例化应答对象时异常.", e);
            }
        }

        Object conTrollerObj;

        String controlKey = control.getSimpleName();
        StringBuffer tempControlKey = new StringBuffer();
        tempControlKey.append(controlKey.substring(0, 1).toLowerCase()).append(controlKey.substring(1));
        conTrollerObj = wac.getBean(tempControlKey.toString());
        // conTrollerObj = control.newInstance();
        if (conTrollerObj == null) {
            throw new NServiceInternalException("类 " + control.getName() + " newInstance 异常.");
        }

        Type[] paramTypes = method.getGenericParameterTypes();
        Object result = null;
        try {
            if (paramTypes == null) {
                this.deBeforeMethod(request, response, null);
                result = method.invoke(conTrollerObj, request, response);
            } else {
                Object args = resolveHandlerArguments(method, conTrollerObj, request);
                this.deBeforeMethod(request, response, args);
                result = method.invoke(conTrollerObj, args, request, response);
            }
            this.deAfterMethod(request, response, conTrollerObj);
        } catch (IllegalArgumentException e) {
            throw new NServiceInternalException("类 " + control.getName() + " 执行 Method [" + method.getName() + "] 异常.", e);
        } catch (IllegalAccessException e) {
            throw new NServiceInternalException("类 " + control.getName() + " 执行 Method [" + method.getName() + "] 异常.", e);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() == null) {
                throw new NServiceInternalException("类 " + control.getName() + " 执行 Method [" + method.getName() + "] 异常.", e);
            } else {
                if (e.getTargetException() instanceof NBaseServiceException) {
                    throw (NBaseServiceException) e.getTargetException();
                } else {
                    throw new NServiceInternalException("类 " + control.getName() + " 执行 Method [" + method.getName() + "] 异常.", e);
                }
            }
        } catch (Exception e) {
            if (e instanceof NNullParamException) {
                throw (NNullParamException) e;
            } else if (e instanceof NInvalidParamException) {
                throw (NInvalidParamException) e;
            } else if(e instanceof RuntimeException){
                throw (RuntimeException) e;
            } else {
                throw new NServiceInternalException("类 " + control.getName() + " 执行 Method [" + method.getName() + "] 异常.", e);
            }
        }

        if (result == null) {
            throw new NServiceInternalException("类" + this.getClass().getName() + " 执行 Method [" + method.getName() + "] 结果为空.");
        }
        if (!(result instanceof IResult)) {
            throw new NServiceInternalException("类" + this.getClass().getName() + " 执行 Method [" + method.getName() + "] 结果不是IResult对象.");
        }
        return (IResult) result;
    }

    /**
     * <p>
     * Discription:[获取controller 的Class，Method]
     * </p>
     * 
     * @param params
     * @param request
     * @param response
     * @return new Object[]{Class,Method}
     * @author:Kevin
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    protected abstract Object[] getController(HttpServletRequest request, HttpServletResponse response);

    /**
     * 在方法前执行
     * 
     * @param request
     * @param response
     */
    protected void deBeforeMethod(HttpServletRequest request, HttpServletResponse response, Object param) {

    }

    /**
     * 在方法后执行
     * 
     * @param request
     * @param response
     */
    protected void deAfterMethod(HttpServletRequest request, HttpServletResponse response, Object param) {

    }

    /**
     * <p>
     * Description:[<Param.Param,Class>]
     * </p>
     */
    private static Map<String, Class<?>> classMap;

    /**
     * <p>
     * Description:[<Class.Param.Param. ... ,Method>]
     * </p>
     */
    private static Map<String, Method> methodMap;

    /**
     * <p>
     * Description:[<Class.method.Param.Param. ... ,RequestMapping>]
     * </p>
     */
    private static Map<String, RequestMapping> requestMappingMap;

    /**
     * <p>
     * Discription:[loadRootControllers]
     * </p>
     * 
     * @author:Kevin
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static void loadRootControllers(ClassLoader sysloader) {

        if (methodMap == null || classMap == null || requestMappingMap == null) {
            classMap = new HashMap<String, Class<?>>();
            methodMap = new HashMap<String, Method>();
            requestMappingMap = new HashMap<String, RequestMapping>();
        }
        URL url = sysloader.getResource("");
        File file = new File(url.getFile());
        List<File> files = new ArrayList<File>(10);
        FileUtil.recurAllFolders(file, files, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (!pathname.isFile()) {
                    return false;
                }
                return pathname.getName().toLowerCase().endsWith(".class");
            }
        });

        try {
            for (File f : files) {
                String className = f.toURI().getPath().replaceAll(url.getPath(), "").replaceAll(".class", "").replaceAll("/", ".");
                Class<?> clazz = sysloader.loadClass(className);
                com.kevin.iesutdio.kfgis.web.framework.annotation.RootController rootC = clazz
                        .getAnnotation(com.kevin.iesutdio.kfgis.web.framework.annotation.RootController.class);
                if (rootC == null) {
                    continue;
                }

                Method[] methods = clazz.getDeclaredMethods();
                if (methods == null || methods.length <= 0) {
                    throw new NServiceInternalException("类" + clazz.getName() + " 没有可用的Method");
                }

                boolean hasMapping = false;
                for (Method method : methods) {
                    RequestMapping rm = method.getAnnotation(RequestMapping.class);
                    if (rm == null) {
                        continue;
                    }
                    hasMapping = true;
                    String baseKey = makeBaseKey(rm);

                    classMap.put(baseKey, clazz);
                    methodMap.put(clazz.getName() + "." + baseKey, method);
                    requestMappingMap.put(clazz.getName() + "." + method.getName() + "." + baseKey, rm);

                    logger.info("注册RootController [" + clazz.getName() + "] Method [" + method.getName() + "]");
                }

                if (!hasMapping) {
                    logger.error("类" + clazz.getName() + " 没有任何Method配置了@RootRequestMapping");
                }

            }
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException("loadRootControllers异常", e);
            }

        }
    }

    /**
     * <p>
     * Discription:[获取Controller 类]
     * </p>
     * 
     * @param key
     * @return
     * @author:Kevin
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    protected Class<?> getControllerClass(String key) {
        return classMap.get(key);
    }

    /**
     * <p>
     * Discription:[获取Method 对象]
     * </p>
     * 
     * @param key
     * @return
     * @author:Kevin
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    protected Method getControllerMethod(String key) {
        return methodMap.get(key);
    }

    /**
     * <p>
     * Discription:[获取 RequestMapping 对象]
     * </p>
     * 
     * @param key
     * @return
     * @author:Kevin
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    protected RequestMapping getRequestMapping(String key) {
        return requestMappingMap.get(key);
    }

    protected static String makeBaseKey(RequestMapping rm) {
        StringBuffer baseKey = new StringBuffer();
        // RequestMethod[] rmethods = rm.method();
        // for (RequestMethod rmethod : rmethods) {
        // baseKey.append(rmethod.name()).append(".");
        // }

        String[] rParams = rm.params();
        for (String rParam : rParams) {
            baseKey.append(rParam).append(".");
        }
        return baseKey.toString();
    }

    @SuppressWarnings("unchecked")
    private Object resolveRestBeanVariable(MethodParameter methodParam, HttpServletRequest servletRequest) throws Exception {
        Class<?> paramType = methodParam.getParameterType();
        Object obj = paramType.newInstance();
        // HttpServletRequest servletRequest =
        // webRequest.getNativeRequest(HttpServletRequest.class);
        // Map<String, String> uriTemplateVariables = (Map<String, String>)
        // servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Map<String, String[]> paramVariables = servletRequest.getParameterMap();
        /**
         * 存储参数名小写化后的MAP
         */
        Map<String, String> lowMap = new HashMap<String, String>();
        /**
         * 遍历原参数MAP，将参数名转为小写后加入到 lowMap 中
         */
        for (Iterator<String> iter = paramVariables.keySet().iterator(); iter.hasNext();) {
            String key = iter.next();
            lowMap.put(key.toLowerCase(), paramVariables.get(key)[0]);
        }

        AnnotationValidator pv = new AnnotationValidator();
        Class<?> thiz = paramType;
        List<String> tempFieldMap = new ArrayList<String>();
        for (; !thiz.equals(Object.class); thiz = thiz.getSuperclass()) {
            for (Field field : thiz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (tempFieldMap.contains(fieldName)) {
                    continue;
                } else {
                    tempFieldMap.add(fieldName);
                }

                String value = null;
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (pv.isIgnore(field)) {
                    continue;
                }
                if (lowMap != null && (lowMap.containsKey(fieldName.toLowerCase()) || lowMap.containsKey(fieldName))) {
                    /**
                     * 如果rest参数存在,则取得rest参数值
                     */
                    value = lowMap.get(fieldName.toLowerCase());
                    if (value == null) {
                        value = lowMap.get(fieldName);
                    }
                    value = value == null ? null : value.trim();

                } else if (lowMap.containsKey(fieldName.toLowerCase()) || lowMap.containsKey(fieldName)) {
                    /**
                     * 如果parameter中存在该值,则取得该值
                     */
                    value = lowMap.get(fieldName.toLowerCase());
                    if (value == null) {
                        value = lowMap.get(fieldName);
                    }
                    value = value == null ? null : value.trim();
                }

                /**
                 * 空验证
                 */
                NotNullValid notNull = field.getAnnotation(NotNullValid.class);
                // 如果是必填值，写法是 a=
                if (notNull == null || notNull.value()) {
                    if (value == null || ((value instanceof java.lang.String) ? "".equalsIgnoreCase((String) value) : false)) {
                        // ||((fieldValue instanceof java.lang.String) ? "".equalsIgnoreCase((String) fieldValue) : false) 
                        //去掉这句话是为了，当参数是可选参数时，如果输入的是字符串，并且字符串无验证的情况下，可以通过校验
                        if (notNull == null) {
                            throw new NNullParamException(field.getName() + " " + FrameworkErrorMsgContants.ERROR_NULLPARAM);
                        } else {
                            throw new NNullParamException(notNull.errCode(), notNull.errMsg());
                        }
                    }
                } else {
                    // 如果是选填值，写法是 a=
                    if (value == null || ((value instanceof java.lang.String) ? "".equalsIgnoreCase((String) value) : false)) {
                        // ||  ((fieldValue instanceof java.lang.String) ? "".equalsIgnoreCase((String) fieldValue) : false)
                        //去掉这句话是为了，当参数是可选参数时，如果输入的是字符串，并且字符串无验证的情况下，可以通过校验
                        continue;
                    }
                }

                FieldError fieldError = null;
                // /**
                // * 空验证
                // */
                // FieldError fieldError = pv.fieldEmpty(field, value);
                // if (fieldError != null) {
                // throw new NNullParamException(field.getName() + " " +
                // FrameworkErrorMsgContants.ERROR_NULLPARAM);
                // }

                WebDataBinder binder = createBinder(null, fieldName);
                // initBinder(handlerForInitBinderCall, fieldName, binder,
                // webRequest);
                Object revalue = null;
                try {
                    revalue = binder.convertIfNecessary(value, field.getType(), methodParam);
                } catch (Exception e) {
                    // fieldError = new FieldError(paramType.getName(),
                    // field.getName(), FrameworkErrorMsgContants.ERROR_TYPE +
                    // " [" + field.getType() + "]");
                    FieldTypeValid fieldTypeValid = field.getAnnotation(FieldTypeValid.class);
                    if (fieldTypeValid == null) {
                        throw new NInvalidParamException(field.getName() + " " + FrameworkErrorMsgContants.ERROR_TYPE + " [" + field.getType() + "]");
                    } else {
                        throw new NInvalidParamException(fieldTypeValid.errCode(), fieldTypeValid.errMsg());
                    }

                }

                /**
                 * 数字类型超长时报错
                 */
                if (revalue != null && "Infinity".equalsIgnoreCase(revalue.toString())
                        && (field.getType() == double.class || field.getType() == float.class || field.getType() == long.class || field.getType() == int.class)) {
                    //fieldError = new FieldError(paramType.getName(), field.getName(), FrameworkErrorMsgContants.ERROR_NUMBERLIMIT);
                    FieldTypeValid fieldTypeValid = field.getAnnotation(FieldTypeValid.class);
                    if (fieldTypeValid == null) {
                        throw new NInvalidParamException(field.getName() + " " + FrameworkErrorMsgContants.ERROR_NUMBERLIMIT);
                    } else {
                        throw new NNullParamException(fieldTypeValid.errCode(), fieldTypeValid.errMsg());
                    }

                }

                /**
                 * 字段值验证
                 */
                fieldError = pv.validateField(field, value);
                if (fieldError != null) {
                    if ("".equalsIgnoreCase(fieldError.getObjectName())) {
                        throw new NInvalidParamException(fieldError.getField() + " " + fieldError.getDefaultMessage());
                    } else {
                        throw new NInvalidParamException(fieldError.getObjectName(), fieldError.getDefaultMessage());
                    }

                }

                if (revalue != null) {
                    field.set(obj, revalue);
                }

            }
        }
        tempFieldMap.clear();
        return obj;
    }

    protected WebDataBinder createBinder(Object target, String objectName) throws Exception {
        return new WebRequestDataBinder(target, objectName);
    }

    @SuppressWarnings("unchecked")
    private Object resolveHandlerArguments(Method handlerMethod, Object handler, HttpServletRequest servletRequest) throws Exception {

        Object reValue = null;
        Class<?>[] paramTypes = handlerMethod.getParameterTypes();
        // Object[] args = new Object[paramTypes.length];
        
        RequestMapping rm = handlerMethod.getAnnotation(RequestMapping.class);
        boolean haspost = false;
        if (rm != null) {
            RequestMethod[] methods = rm.method();
            for (RequestMethod m : methods) {
                if (m == RequestMethod.POST) {
                    haspost = true;
                    break;
                }
            }
        }
        
        byte[] bytes = null; 
        if(haspost){
        	bytes= RequestUtil.getPostValue(servletRequest);
        	 RequestBean rb=   (RequestBean) servletRequest.getSession().getAttribute(FrameworkGISContants.KEY_LOGGER_REQUESTBEAN); 
             if(rb!=null&&bytes!=null&&bytes.length>0){
          	   rb.setRequestBody(new String(bytes,"utf-8"));
             }
        }

        for (int i = 0; i < paramTypes.length; i++) {
            MethodParameter methodParam = new MethodParameter(handlerMethod, i);
            methodParam.initParameterNameDiscovery(new LocalVariableTableParameterNameDiscoverer());
            GenericTypeResolver.resolveParameterType(methodParam, handler.getClass());

            Annotation[] paramAnns = methodParam.getParameterAnnotations();
            for (Annotation paramAnn : paramAnns) {
                if (RestBeanVariable.class.isInstance(paramAnn)) {

                    reValue = resolveRestBeanVariable(methodParam, servletRequest);
                    break;
                }
            }
        }

       
        IParams param = null;
        if (reValue != null) {
            if (reValue instanceof IParams) {
                param = (IParams) reValue;
            }
        }
        if (haspost && param != null) {
//            byte[] bytes = RequestUtil.getPostValue(servletRequest);
            param.setPostValues(bytes,servletRequest);
        }
        if (param != null) {
            validator(param.getValidator(), param);
            if (param instanceof AbstractParams) {
                AbstractParams aparmas = (AbstractParams) param;
                if (aparmas.getXmlParams() != null) {
                    aparmas.getXmlParams().validate();
                    validator(aparmas.getXmlParams().getValidator(), param);
                }

                StringBuffer uri = new StringBuffer();
                uri.append(servletRequest.getServletPath());

                String[] paramkeys = (String[]) servletRequest.getParameterMap().keySet().toArray(new String[0]);
                if (paramkeys.length > 0) {
                    uri.append("?1=").append(System.currentTimeMillis());
                    for (String key : paramkeys) {
                        String temp = servletRequest.getParameter(key);
                        uri.append("&").append(key).append("=")
                                .append(temp == null || "".equalsIgnoreCase(temp) ? "" : URLEncoder.encode(servletRequest.getParameter(key), "UTF-8"));

                    }
                }
                aparmas.setUri(uri.toString());
            }
        }
        servletRequest.getSession().setAttribute(FrameworkGISContants.KEY_REQUEST_PARAM, reValue);
        // MARKSOURCE fenghl add
        return reValue;
    }

    private void validator(Validator validator, IParams param) {
        if (validator != null) {
            Errors errors = new BindException(param, param.toString());
            validator.validate(param, errors);
            if (errors.hasErrors()) {
                throw new NInvalidParamException(errors.getFieldError().getField() + " " + errors.getFieldError().getDefaultMessage());
            }
        }
    }

}
