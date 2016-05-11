package com.kevin.iesutdio.kfgis.web.framework.annotation;

/**
 * 自定义验证器接口
 * @author fengheliang
 *
 */
public interface ICustomValidator{
    
    /**
     * 实际验证方法
     * @param obj
     * @return
     */
    public abstract boolean validate(Object obj);
    
}