package cn.com.cennavi.kfgis.framework.contant;

/**
 * 系统启动时使用的sql
 * @author Kevin
 *
 */
public class SQLContants {

    //public static final String SQL_PARAM_STATIC_CONFIGURE="SELECT PARAM_NAME,PARAM_VALUE,GROUP_ID FROM PARAM_STATIC_CONFIGURE WHERE STATUS = '1' AND SERVER_ID = ?";

    //public static final String SQL_PARAM_STATIC_CONFIGURE_NOSERVER="SELECT PARAM_NAME,PARAM_VALUE,GROUP_ID FROM PARAM_STATIC_CONFIGURE WHERE STATUS = '1'";

    public static final String SQL_PARAM_DYNAMIC_CONFIGURE = "SELECT PARAM_NAME as a,PARAM_VALUE as b,GROUP_ID as c FROM param_dynamic_configure WHERE STATUS = '1' AND SERVER_ID = ?";

    public static final String SQL_PARAM_DYNAMIC_CONFIGURE_NOSERVER = "SELECT PARAM_NAME as a,PARAM_VALUE as b,GROUP_ID as c FROM param_dynamic_configure WHERE STATUS = '1'";

    public static final String SQL_PARAM_ERROR_CONFIGURE = "SELECT ERROR_CODE as a,ERROR_DESC as b,'errorsMsg' as c FROM param_error_configure WHERE STATUS = '1'";

    //public static final String SQL_PARAM_AUTH_USER="SELECT BZCODE,USERID,'authUser' FROM AUTHENTICATION WHERE STATUS = '1'";
}
