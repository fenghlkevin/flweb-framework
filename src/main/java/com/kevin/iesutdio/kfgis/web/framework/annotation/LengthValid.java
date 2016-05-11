package com.kevin.iesutdio.kfgis.web.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LengthValid {
    /**
     * 忽略double值
     */
    public static final double IGNORE_VALUE=-1;
    
    /**
     * 数字等于
     * @return
     */
    double numberEquals() default IGNORE_VALUE;
    /**
     * 小于numberMaxExclusive
     * @return
     */
    double numberMaxExclusive() default IGNORE_VALUE;
    
    /**
     * 小于等于 numberMaxInclusive
     * @return
     */
    double numberMaxInclusive() default IGNORE_VALUE;
    
    /**
     * 大于 numberMinExclusive
     * @return
     */
    double numberMinExclusive() default IGNORE_VALUE;
    
    /**
     * 大于等于 numberMinInclusive
     * @return
     */
    double numberMinInclusive() default IGNORE_VALUE;
    
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
