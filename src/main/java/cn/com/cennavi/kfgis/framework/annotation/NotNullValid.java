package cn.com.cennavi.kfgis.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 非空验证
 * @author Kevin
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotNullValid {
    /**
     * 如果为true则 必须不能为空，否则可以为空
     * @return
     */
    boolean value() default true;
    
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
