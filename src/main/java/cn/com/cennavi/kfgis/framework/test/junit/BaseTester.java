package cn.com.cennavi.kfgis.framework.test.junit;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.util.Log4jWebConfigurer;

import cn.com.cennavi.kfgis.framework.dao.IAuthDAO;
import cn.com.cennavi.kfgis.framework.listener.ConfigureContextListener;
import cn.com.cennavi.kfgis.framework.listener.SystemContextListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration
public class BaseTester {
    
    @Autowired
    private IAuthDAO authDao = null;

    static {
        SystemContextListener sc = new SystemContextListener();
        sc.contextInitialized(null);

        MockServletContext msc = new MockServletContext();
        msc.setAttribute("log4jExposeWebAppRoot", false);
        Log4jWebConfigurer.initLogging(msc);

    }

    @Before
    public void startup() {
        ConfigureContextListener ccl = new ConfigureContextListener();
        ccl.readConfigureFromDao(authDao);
    }
}
