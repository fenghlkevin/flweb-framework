package com.kevin.iesutdio.kfgis.bean.param;

import java.io.Serializable;

import com.kevin.iesutdio.kfgis.web.framework.exception.NBaseServiceException;
import org.springframework.validation.Validator;

/**
 * XML对象的接口对象
 * @author Kevin
 *
 */
public interface IXmlParams extends Serializable{
    
    /**
     * 返回该实现类的所有 validator 数组
     * @return
     */
    public abstract Validator getValidator();
    
    /**
     * 执行自动校验
     * @return
     */
    public abstract void validate() throws NBaseServiceException;

}
