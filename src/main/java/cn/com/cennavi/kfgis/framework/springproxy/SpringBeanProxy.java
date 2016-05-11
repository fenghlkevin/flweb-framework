package cn.com.cennavi.kfgis.framework.springproxy;

import org.springframework.context.ApplicationContext;



public class SpringBeanProxy {
	
    private static ApplicationContext applicationContext;
    
    public synchronized static void setApplicationContext(ApplicationContext arg0) {
        applicationContext = arg0;
    }
    
    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }
}