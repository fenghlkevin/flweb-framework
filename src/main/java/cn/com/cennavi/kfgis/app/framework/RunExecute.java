package cn.com.cennavi.kfgis.app.framework;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.util.Log4jConfigListener;
import org.springframework.web.util.Log4jWebConfigurer;

import cn.com.cennavi.kfgis.app.framework.contant.SystemPropertiesContant;
import cn.com.cennavi.kfgis.app.framework.dataloader.configfile.ConfigFolderDataParser;
import cn.com.cennavi.kfgis.app.framework.listener.leader.SimpleFollowerExecuteImpl;
import cn.com.cennavi.kfgis.app.framework.listener.leader.SimpleLeaderExecuteImpl;
import cn.com.cennavi.kfgis.app.framework.listener.leader.ExecutorMonitorListener;
import cn.com.cennavi.kfgis.app.framework.listener.leader.inf.IExecute;
import cn.com.cennavi.kfgis.app.framework.listener.task.TaskMonitorListener;
import cn.com.cennavi.kfgis.app.framework.util.NamedThreadFactory;
import cn.com.cennavi.kfgis.app.framework.util.ServerAnalysisUtil;
import cn.com.cennavi.kfgis.app.framework.util.ServerAnalysisUtil.ServerConfigure;
import cn.com.cennavi.kfgis.app.framework.zookeeper.ZKClientFactory;
import cn.com.cennavi.kfgis.app.framework.zookeeper.impl.SimpleLeaderLatchListener;
import cn.com.cennavi.kfgis.framework.exception.NServiceInternalException;
import cn.com.cennavi.kfgis.framework.file.impl.LoaderTask;
import cn.com.cennavi.kfgis.framework.listener.SystemContextListener;
import cn.com.cennavi.kfgis.framework.springproxy.SpringBeanInitListener;
import cn.com.cennavi.kfgis.framework.util.ObjUtil;

public class RunExecute {

	/**
	 * 执行task的 线程池
	 */
	private ScheduledExecutorService executor;

	{
		executor = Executors.newScheduledThreadPool(3, new NamedThreadFactory(this.getClass().getName()));
	}

	protected void vilidateEmpty(String value, String key) {
		if (ObjUtil.isEmpty(value, true)) {
			throw new NServiceInternalException("can not find [" + key + "] value");
		}
	}

	protected void validateProp() {
		String projectName = System.getProperty(SystemPropertiesContant.SYSTEM_PROJECT_NAME);
		this.vilidateEmpty(projectName, SystemPropertiesContant.SYSTEM_PROJECT_NAME);

		String regPath = System.getProperty(SystemPropertiesContant.ZOOKEEPER_REG_PATH);
		this.vilidateEmpty(regPath, SystemPropertiesContant.ZOOKEEPER_REG_PATH);

		String leaderPath = System.getProperty(SystemPropertiesContant.ZOOKEEPER_LEADER_PATH);
		this.vilidateEmpty(leaderPath, SystemPropertiesContant.ZOOKEEPER_LEADER_PATH);

		String serverName = System.getProperty(SystemPropertiesContant.SYSTEM_SERVICE_NAME);
		this.vilidateEmpty(serverName, SystemPropertiesContant.SYSTEM_SERVICE_NAME);

		String zookeepers = System.getProperty(SystemPropertiesContant.ZOOKEEPER_CLUSTERS_PATH);
		this.vilidateEmpty(zookeepers, SystemPropertiesContant.ZOOKEEPER_CLUSTERS_PATH);

		String properties_folder = System.getProperty(SystemPropertiesContant.SYSTEM_PROPERTIES_FOLDER);
		this.vilidateEmpty(properties_folder, SystemPropertiesContant.SYSTEM_PROPERTIES_FOLDER);

		String configPath = System.getProperty(SystemPropertiesContant.ZOOKEEPER_CONFIGURE_PATH);
		this.vilidateEmpty(configPath, SystemPropertiesContant.ZOOKEEPER_CONFIGURE_PATH);
		
		String taskPath = System.getProperty(SystemPropertiesContant.ZOOKEEPER_TASK_PATH);
		this.vilidateEmpty(taskPath, SystemPropertiesContant.ZOOKEEPER_TASK_PATH);
	}

	/**
	 * 注册本机到服务器
	 * 
	 * @param zkFactory
	 * @param sc
	 */
	private void regServer(ServerConfigure sc) {
		String serverName = System.getProperty(SystemPropertiesContant.SYSTEM_SERVICE_NAME);
		String regPath = System.getProperty(SystemPropertiesContant.ZOOKEEPER_REG_PATH) + "/" + serverName + "@" + sc.getIp() + "@" + sc.getHostname();
		CuratorFramework curator = ZKClientFactory.getInstance().getCurator();
		try {
			curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(regPath, sc.getServerInfo().getBytes());
		} catch (Exception e) {
			throw new NServiceInternalException("Register Server Information Error .", e);
		}
	}

	public void execute(String[] initArgs) {
		if(initArgs.length<2){
			throw new NServiceInternalException("Main Args Value is not ENOUTH .");
		}
		
		System.setProperty(SystemPropertiesContant.SYSTEM_SERVICE_NAME,initArgs[0]);
		String serverName = System.getProperty(SystemPropertiesContant.SYSTEM_SERVICE_NAME);
		// 加载所有配置信息
		// 加载Rootonfig文件，并对变量赋值
		/*System.setProperty(SystemContextListener.PARAM_ROOTCONFIG, initArgs[1]);
		SystemContextListener scl = new SystemContextListener();
		scl.contextInitialized(null);*/

		MockServletContext mc = new MockServletContext();
		mc.addInitParameter(Log4jWebConfigurer.EXPOSE_WEB_APP_ROOT_PARAM, serverName);
		ServletContextEvent sce = new ServletContextEvent(mc);
		// 加载log4j配置文件
		Log4jConfigListener log4j = new Log4jConfigListener();
		log4j.contextInitialized(sce);

		// 加载spring 配置文件
		ContextLoaderListener springListener = new ContextLoaderListener();
		springListener.contextInitialized(sce);

		// 加载springproxy
		SpringBeanInitListener sbi = new SpringBeanInitListener();
		sbi.contextInitialized(sce);
		// 检查关键项目
		validateProp();

		// 加载文件变化检测listener
		LoaderTask loaderFile = new LoaderTask();
		// 增加文件解析器
		loaderFile.addParser(new ConfigFolderDataParser(SystemPropertiesContant.SYSTEM_PROPERTIES_FOLDER, null), SystemPropertiesContant.SYSTEM_PROPERTIES_FOLDER);
		executor.scheduleWithFixedDelay(loaderFile, 2L, 10L, TimeUnit.SECONDS);

		// 启动curator客户端
		String leaderPath = System.getProperty(SystemPropertiesContant.ZOOKEEPER_LEADER_PATH);
		String zookeepers = System.getProperty(SystemPropertiesContant.ZOOKEEPER_CLUSTERS_PATH);
		String projectName = System.getProperty(SystemPropertiesContant.SYSTEM_PROJECT_NAME);

		ZKClientFactory clientFactory = ZKClientFactory.getInstance();
		clientFactory.createCurator(zookeepers, projectName);

		SimpleLeaderLatchListener latchListener = new SimpleLeaderLatchListener(clientFactory.getCurator(), leaderPath, serverName);
		try {
			clientFactory.createLeaderLatch(serverName, leaderPath, latchListener);
		} catch (Exception e) {
			throw new NServiceInternalException("初始化Latchistener异常", e);
		}

		// 得到本机cpu和jvm设置
		ServerConfigure sc = ServerAnalysisUtil.execute();

		// 注册本机到zookeeper
		this.regServer(sc);

		String configPath = System.getProperty(SystemPropertiesContant.ZOOKEEPER_CONFIGURE_PATH);
		String taskPath = System.getProperty(SystemPropertiesContant.ZOOKEEPER_TASK_PATH);
		// 加载leader监控进程
		String regPath=System.getProperty(SystemPropertiesContant.ZOOKEEPER_REG_PATH);
		IExecute leaderExecute = new SimpleLeaderExecuteImpl(configPath, taskPath, regPath);
		
		//加载follower监控进程
		
		IExecute followerExecute = new SimpleFollowerExecuteImpl(configPath);

		ExecutorMonitorListener lt = new ExecutorMonitorListener();
		lt.setLeaderExecute(leaderExecute).setFollowerExecute(followerExecute);
		executor.scheduleWithFixedDelay(lt, 2L, 10L, TimeUnit.SECONDS);

		// 启动任务监控进程
		TaskMonitorListener wl = new TaskMonitorListener();
		wl.setTaskPath(taskPath).setServerName(serverName);
		executor.scheduleWithFixedDelay(wl, 20L, 10L, TimeUnit.SECONDS);

	}

}
