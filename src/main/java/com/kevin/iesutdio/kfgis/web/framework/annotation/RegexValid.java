package com.kevin.iesutdio.kfgis.web.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 正则表达式
 * @author Kevin
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegexValid {
    /**
     * 默认为空的正则表达式
     * @return
     */
    String value() default "";
    
    /**
     * 错误码，当使用错误码时，异常信息中会存储该内容
     * @return
     */
    String errCode() default "";
    
    /**
     * 错误信息，当存在错误信息时，异常信息中会存储该内容
     * @return
     */
    String errMsg() default "";
}
