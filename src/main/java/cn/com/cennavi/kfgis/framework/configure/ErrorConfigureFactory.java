package cn.com.cennavi.kfgis.framework.configure;

import java.util.Map;

import org.springframework.validation.ObjectError;

import cn.com.cennavi.kfgis.framework.contant.FrameworkErrorCodeContants;

/**
 * Created on 2011-12-20
 * <p>Title: WEB-T GIS核心系统_错误工厂类_错误对象</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author Kevin Feng Kevin Feng@cennavi.com.cn
 * @version 1.0
 * @update 修改日期 修改描述
 */
public class ErrorConfigureFactory {

    //    private Log logger=LogFactory.getLog(ErrorConfigureFactory.class);

    private static ErrorConfigureFactory configure;

    public static ErrorConfigureFactory getInstance() {
        if (configure == null) {
            configure = new ErrorConfigureFactory();
        }
        return configure;
    }

    private ErrorConfig errorConfig;

    private ErrorConfigureFactory() {
        /**
         * 初始化异常代码并写入内存中
         */
        this.errorConfig = (ErrorConfig) ConfigureFactory.getInstance().getConfigure(ErrorConfig.class);
    }

    public ObjectError createObjectError(String code, String objectName, Object[] args) {
        String msg = errorConfig.getErrorsMsg().get(code);

        objectName = objectName == null || "".equalsIgnoreCase(objectName) ? "GISError" : objectName;
        if (msg == null) {
            return new ObjectError(objectName, new String[] { FrameworkErrorCodeContants.ERROR_CODE_150001 }, new Object[] {}, "服务内部错误");
        }

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                msg = msg.replaceFirst("[?]", args[i].toString());
            }
        }
        return new ObjectError(objectName, new String[] { code }, new Object[] {}, msg);
    }

    public String getObjectErrorDesc(String code, Object[] args) {
        return errorConfig.getErrorsMsg().get(code);
    }

    /**
     * <p>Discription:[通过错误代码获取错误描述]</p>
     * @param code  错误代码
     * @return
     * @author liuchao
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public String getObjectErrorDesc(String code) {
        return errorConfig.getErrorsMsg().get(code);
    }

    /**
     * Created on 2011-12-20
     * <p>Title: WEB-T GIS核心系统_异常处理_异常读取类</p>
     * <p>Copyright: Copyright (c) 2011</p>
     * <p>Company: 沈阳世纪高通科技有限公司</p>
     * <p>Department: 技术开发部</p>
     * 
     * @author Kevin Feng Kevin Feng@cennavi.com.cn
     * @version 1.0
     * @update 修改日期 修改描述
     */
    public static class ErrorConfig {

        private Map<String, String> errorsMsg;

        public Map<String, String> getErrorsMsg() {
            return errorsMsg;
        }

        public void setErrorsMsg(Map<String, String> errorsMsg) {
            this.errorsMsg = errorsMsg;
        }
    }

    public static void setConfigure(ErrorConfigureFactory configure) {
        ErrorConfigureFactory.configure = configure;
    }

}
