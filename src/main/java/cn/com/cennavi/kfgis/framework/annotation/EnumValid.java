package cn.com.cennavi.kfgis.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举型参数
 * @author Kevin
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnumValid {
    /**
     * 枚举型数组
     * @return
     */
    String[] value() default {};
    
    /**
     * 可以识别一个字段传入的多个值。如果填入了值，该值则为切分的正则表达式
     * @return
     */
    String split() default "";
    
    
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
