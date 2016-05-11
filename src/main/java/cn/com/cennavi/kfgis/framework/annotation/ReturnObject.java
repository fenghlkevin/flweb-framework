package cn.com.cennavi.kfgis.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.Mapping;

/**
 * 应答返回注解，主要用于特殊的结果应答处理
 * @author fengheliang
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ReturnObject {
    
    Class<?> success() default Object.class;
    
    Class<?> error() default Object.class;

}
