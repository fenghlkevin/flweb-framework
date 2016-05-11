package cn.com.cennavi.kfgis.framework.contant;

/**
 * 错误描述
 * @author Kevin
 *
 */
public class FrameworkErrorMsgContants {

    /**
     * 字段缺失
     */
    public static String ERROR_MISSINGPARAM = "字段缺失";

    /**
     * 字段不允许为空
     */
    public static String ERROR_NULLPARAM = "字段不允许为空";

    /**
     * 字段不允许为空
     */
    public static String ERROR_CUSTOMPARAM = "未通过自定义校验";

    /**
     * 未通过字符长度校验
     */
    public static String ERROR_OUTOFLENGTH = "未通过字符长度校验";

    /**
     * 未通过正则表达式校验
     */
    public static String ERROR_REGEX = "未通过正则表达式校验";

    /**
     * 未通过数字范围校验
     */
    public static String ERROR_NUMBERLIMIT = "未通过数字范围校验";

    /**
     * 未通过枚举型校验
     */
    public static String ERROR_ENUMHIT = "未通过枚举型校验";

    /**
     * 数据类型必须为
     */
    public static String ERROR_TYPE = "类型必须为";


    public static String getERROR_MISSINGPARAM() {
        return ERROR_MISSINGPARAM;
    }


    public static void setERROR_MISSINGPARAM(String eRROR_MISSINGPARAM) {
        ERROR_MISSINGPARAM = eRROR_MISSINGPARAM;
    }


    public static String getERROR_NULLPARAM() {
        return ERROR_NULLPARAM;
    }


    public static void setERROR_NULLPARAM(String eRROR_NULLPARAM) {
        ERROR_NULLPARAM = eRROR_NULLPARAM;
    }


    public static String getERROR_CUSTOMPARAM() {
        return ERROR_CUSTOMPARAM;
    }


    public static void setERROR_CUSTOMPARAM(String eRROR_CUSTOMPARAM) {
        ERROR_CUSTOMPARAM = eRROR_CUSTOMPARAM;
    }


    public static String getERROR_OUTOFLENGTH() {
        return ERROR_OUTOFLENGTH;
    }


    public static void setERROR_OUTOFLENGTH(String eRROR_OUTOFLENGTH) {
        ERROR_OUTOFLENGTH = eRROR_OUTOFLENGTH;
    }


    public static String getERROR_REGEX() {
        return ERROR_REGEX;
    }


    public static void setERROR_REGEX(String eRROR_REGEX) {
        ERROR_REGEX = eRROR_REGEX;
    }


    public static String getERROR_NUMBERLIMIT() {
        return ERROR_NUMBERLIMIT;
    }


    public static void setERROR_NUMBERLIMIT(String eRROR_NUMBERLIMIT) {
        ERROR_NUMBERLIMIT = eRROR_NUMBERLIMIT;
    }


    public static String getERROR_ENUMHIT() {
        return ERROR_ENUMHIT;
    }


    public static void setERROR_ENUMHIT(String eRROR_ENUMHIT) {
        ERROR_ENUMHIT = eRROR_ENUMHIT;
    }


    public static String getERROR_TYPE() {
        return ERROR_TYPE;
    }


    public static void setERROR_TYPE(String eRROR_TYPE) {
        ERROR_TYPE = eRROR_TYPE;
    }


    public static class Locale {
        public static final String LANG_ZH_CN = java.util.Locale.SIMPLIFIED_CHINESE.toString();

        public static final String LANG_EN_US = java.util.Locale.US.toString();

        private String locale;

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }
    }

}
