package com.kevin.iesutdio.kfgis.web.framework.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.kevin.iesutdio.kfgis.web.framework.configure.ErrorConfigureFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kevin.iesutdio.kfgis.web.framework.configure.ConfigureFactory;
import com.kevin.iesutdio.kfgis.web.framework.contant.FrameworkErrorMsgContants;
import com.kevin.iesutdio.kfgis.web.framework.contant.FrameworkErrorMsgContants.Locale;
import com.kevin.iesutdio.kfgis.web.framework.control.RootController;
import com.kevin.iesutdio.kfgis.web.framework.dao.IAuthDAO;
//import cn.com.cennavi.kfgis.util.CouchBaseClient2;
//import cn.com.cennavi.kfgis.util.CouchBaseClient2.CouchBaseParams;

/**
 * Created on 2011-12-19
 * <p>Title: WEB-T GIS核心系统_加载数据库中的参数配置信息</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author Kevin Feng fenghlkevin@gmail.com
 * @version 1.0
 * @update 修改日期 修改描述
 */
public class ConfigureContextListener implements ServletContextListener {

    //private Logger logger = LoggerFactory.getLogger(ConfigureContextListener.class);

    private static final String ROOTCONFIG_INITROOTCONTROLLER = "initRootController";

    private static final String ROOTCONFIG_INITLOCALE = "initLocale";
//
//    private static final String ROOTCONFIG_INITDBCONFIG = "initDBConfig";
//
//    private static final String ROOTCONFIG_INITDBERROR = "initDBError";

//    private static final String ROOTCONFIG_INITCOUCHBASE = "initCouchBase";

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        if ("true".equalsIgnoreCase(System.getProperty(ROOTCONFIG_INITROOTCONTROLLER))) {
           System.out.println("初始化RootController");
            ClassLoader cl = SystemContextListener.class.getClassLoader();
            RootController.loadRootControllers(cl);
        }

        if("true".equalsIgnoreCase(System.getProperty(ROOTCONFIG_INITLOCALE))){
            this.initLocaleStr((Locale) ConfigureFactory.getInstance().getConfigure(Locale.class), sce);
        }

//        boolean initDBConfig = "true".equalsIgnoreCase(System.getProperty(ROOTCONFIG_INITDBCONFIG));
//        boolean initDBError = "true".equalsIgnoreCase(System.getProperty(ROOTCONFIG_INITDBERROR));
//        if (initDBConfig && initDBError) {
//            ApplicationContext ac2 = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
//            IAuthDAO authDao = (IAuthDAO) ac2.getBean("authDAOImpl");
//            readConfigureFromDao(authDao);
//            this.initLocaleStr((Locale) ConfigureFactory.getInstance().getConfigure(Locale.class), sce);
//        }
    }

//    public void readConfigureFromDao(IAuthDAO authDao) {
//        Map<String, String> keys = new HashMap<String, String>();
//        Map<String, String> groups = new HashMap<String, String>();
//       System.out.println("初始化DynamicConfigure");
//        authDao.queryDynamicConfigure(keys, groups);
//
//        String[] homeKeys = System.getProperties().keySet().toArray(new String[0]);
//
//        String[] tempkeys = keys.keySet().toArray(new String[0]);
//        for (String tempKey : tempkeys) {
//            String temp = keys.get(tempKey);
//            for (String varName : homeKeys) {
//                String varValue = System.getProperty(varName);
//                temp = temp.replace("${" + varName + "}", varValue);
//            }
//            keys.put(tempKey, temp);
//        }
//
//        //       System.out.println("初始化StaticConfigure");
//        //        authDao.queryStaticConfigure(keys, groups);
//        //       System.out.println("初始化ErrorConfigure");
//        //        authDao.queryErrorConfigure(keys, groups);
//
//       System.out.println("初始化ConfigureFactory");
//        ConfigureFactory cf = ConfigureFactory.getInstance();
//        cf.setGroups(groups);
//        cf.setKeys(keys);
//        ErrorConfigureFactory.setConfigure(null);
//       System.out.println("初始化ErrorConfigureFactory");
//        ErrorConfigureFactory.getInstance();
//
////        if ("true".equalsIgnoreCase(System.getProperty(ROOTCONFIG_INITCOUCHBASE))) {
////           System.out.println("初始化CouchBase服务器");
////            CouchBaseParams cbp = (CouchBaseParams) cf.getConfigure(CouchBaseParams.class);
////            CouchBaseClient2.newInstance(cbp);
////        }
//    }

    private void initLocaleStr(Locale locale, ServletContextEvent sce) {
       System.out.println("加载国际化描述信息");
        try {
            WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());

            if (locale.getLocale() != null) {
                java.util.Locale loc = java.util.Locale.US.toString().equalsIgnoreCase(locale.getLocale()) ? java.util.Locale.US
                        : java.util.Locale.SIMPLIFIED_CHINESE;

                FrameworkErrorMsgContants.setERROR_CUSTOMPARAM(context.getMessage("ERROR_CUSTOMPARAM", null, loc));
                FrameworkErrorMsgContants.setERROR_ENUMHIT(context.getMessage("ERROR_ENUMHIT", null, loc));
                FrameworkErrorMsgContants.setERROR_MISSINGPARAM(context.getMessage("ERROR_MISSINGPARAM", null, loc));
                FrameworkErrorMsgContants.setERROR_NULLPARAM(context.getMessage("ERROR_NULLPARAM", null, loc));
                FrameworkErrorMsgContants.setERROR_NUMBERLIMIT(context.getMessage("ERROR_NUMBERLIMIT", null, loc));
                FrameworkErrorMsgContants.setERROR_OUTOFLENGTH(context.getMessage("ERROR_OUTOFLENGTH", null, loc));
                FrameworkErrorMsgContants.setERROR_REGEX(context.getMessage("ERROR_REGEX", null, loc));
                FrameworkErrorMsgContants.setERROR_TYPE(context.getMessage("ERROR_TYPE", null, loc));
            } else {
                System.out.println("未配置国际化描述信息，使用默认描述");
            }
        } catch (Exception e) {
        	 System.out.println("读取国际化配置错误");
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
