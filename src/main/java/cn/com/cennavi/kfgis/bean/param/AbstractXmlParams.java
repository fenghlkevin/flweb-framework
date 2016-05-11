package cn.com.cennavi.kfgis.bean.param;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.springframework.validation.FieldError;

import cn.com.cennavi.kfgis.framework.exception.NBaseServiceException;
import cn.com.cennavi.kfgis.framework.exception.NNullParamException;
import cn.com.cennavi.kfgis.framework.validator.AnnotationValidator;

/**
 * xml对象抽象类，必须继承该类
 * @author Kevin
 *
 */
public abstract class AbstractXmlParams implements IXmlParams {

    /**
     * 
     */
    private static final long serialVersionUID = 8575902553593955280L;
    
    private void validate(Class<?> thiz,AnnotationValidator pv,Object object){
        for (Field field : thiz.getDeclaredFields()) {
            
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (pv.isIgnore(field)) {
                continue;
            }

            field.setAccessible(true);
            Object obj = null;
            try {
                obj = field.get(object);
                
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }

            /**
             * 空验证
             */
            FieldError fieldError = pv.fieldEmpty(field, obj);
            if (fieldError != null) {
                throw new NNullParamException(fieldError.getField() + " " + fieldError.getDefaultMessage());
//                throw new NullParamException(fieldError);
            }

            /**
             * 字段值验证
             */
            if (obj instanceof AbstractXmlParams) {
                AbstractXmlParams xmlParams = (AbstractXmlParams) obj;
                this.validate(xmlParams.getClass(), pv,xmlParams);
                xmlParams.validate();
            }else if(obj instanceof List<?>){
            	List<?> temp=(List<?>)obj;
            	for(Object t:temp){
            		 this.validate(t.getClass(), pv,t);
            	}
            } else {
                fieldError = pv.validateField(field, obj);
            }

            if (fieldError != null) {
//                throw new InvalidParamException(fieldError);
                throw new NNullParamException(fieldError.getField() + " " + fieldError.getDefaultMessage());
            }
        }
    }
    
    @Override
    public void validate() throws NBaseServiceException {
    	AnnotationValidator pv = new AnnotationValidator();
    	Class<?> thiz = this.getClass();
    	this.validate(thiz, pv,this);
    }

//    @Override
//    public void validate() throws NBaseServiceException {
//        Class<?> thiz = this.getClass();
//        AnnotationValidator pv = new AnnotationValidator();
//        for (Field field : thiz.getDeclaredFields()) {
//            
//            if (Modifier.isStatic(field.getModifiers())) {
//                continue;
//            }
//
//            if (pv.isIgnore(field)) {
//                continue;
//            }
//
//            field.setAccessible(true);
//            Object obj = null;
//            try {
//                obj = field.get(this);
//            } catch (IllegalArgumentException e) {
//                throw new IllegalArgumentException(e);
//            } catch (IllegalAccessException e) {
//                throw new IllegalArgumentException(e);
//            }
//
//            /**
//             * 空验证
//             */
//            FieldError fieldError = pv.fieldEmpty(field, obj);
//            if (fieldError != null) {
//                throw new NNullParamException(fieldError.getField() + " " + fieldError.getDefaultMessage());
////                throw new NullParamException(fieldError);
//            }
//
//            /**
//             * 字段值验证
//             */
//            if (obj instanceof AbstractXmlParams) {
//                AbstractXmlParams xmlParams = (AbstractXmlParams) obj;
//                xmlParams.validate();
//            } else {
//                fieldError = pv.validateField(field, obj);
//            }
//
//            if (fieldError != null) {
////                throw new InvalidParamException(fieldError);
//                throw new NNullParamException(fieldError.getField() + " " + fieldError.getDefaultMessage());
//            }
//        }
//    }

}
