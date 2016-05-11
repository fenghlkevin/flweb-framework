package cn.com.cennavi.kfgis.framework.annotation;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义验证器
 * @author fengheliang
 *
 */

public interface ICustomParamProcessor {
    
	/**
    * 实际处理方法
    * @param obj
    * @param inbytes
    * @return
    */
   public abstract byte[] process(Object obj,byte[] inbytes,HttpServletRequest request);
    
}
