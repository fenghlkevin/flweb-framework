package cn.com.cennavi.kfgis.framework.listener;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.com.cennavi.kfgis.framework.exception.NServiceInternalException;

/**
 * Created on 2011-12-19
 * <p>
 * Title: WEB-T GIS核心系统_加载系统参数
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 沈阳世纪高通科技有限公司
 * </p>
 * <p>
 * Department: 技术开发部
 * </p>
 * 
 * @author Kevin Feng fengheliang@cennavi.com.cn
 * @version 1.0
 * @update 修改日期 修改描述
 */
public class SystemContextListener implements ServletContextListener {

	// private Logger logger =
	// LoggerFactory.getLogger(SystemContextListener.class);

	// /**
	// * root配置的文件名配置
	// */
	// private static final String PARAM_ROOTCONFIG = "ROOTCONFIG";

	/**
	 * 最基础的配置路径
	 */
	private static final String BASE_HOME = "BASE_HOME";

	public static final String PARAM_ROOTCONFIG = "rootConfig";

	public static final String CONTEXTCONFIGLOCATION = "contextConfigLocation";

	private static final String PARAM_ROOTCONFIG_DEAFULTNAME = "RootConfig.properties";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("读取RootConfig配置文件");
		String path = null;

		if (sce != null) {
			path = sce.getServletContext().getInitParameter(SystemContextListener.PARAM_ROOTCONFIG);
		}
		/**
		 * 如果从servlet中读取配置失败，转为从System.Property中读取
		 */
		if (!pathRight(path)) {
			path = System.getProperty(SystemContextListener.PARAM_ROOTCONFIG);
		}

		/**
		 * 如果系统中也不存在配置，则取得WEB-INF 环境变量下的RootConfig
		 */
		if (!pathRight(path) && sce != null) {
			path = sce.getServletContext().getRealPath("/") + File.separator + "WEB-INF" + File.separator + "conf" + File.separator
					+ SystemContextListener.PARAM_ROOTCONFIG_DEAFULTNAME;
		}
		if (!pathRight(path)) {
			throw new NServiceInternalException("未合适的RootConfig");
		}

		initSystemProperties(path, sce);
	}

	private boolean pathRight(String path) {
		if (path == null || "".equalsIgnoreCase(path)) {
			return false;
		}
		File f = new File(path);
		if (!f.exists() || !f.isFile()) {
			return false;
		}
		return true;
	}

	private void initSystemProperties(String path, ServletContextEvent sce) {
		File file = new File(path);
		if (!pathRight(path)) {
			System.err.println("RootConfig 不存在或不是文件 [" + path + "]");
			// logger.error("RootConfig 不存在或不是文件. [{}]", new Object[] { path });
			return;
		}
		String rfPath = file.getParent();
		File rfPathF = new File(rfPath);
		File[] allRootConFig = rfPathF.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				String pa = pathname.getName().toLowerCase();
				return pa.startsWith("rootconfig");
			}
		});

		Arrays.sort(allRootConFig, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				String s1 = o1.getName().toLowerCase();
				String s2 = o2.getName().toLowerCase();
				return s1.compareToIgnoreCase(s2);
			}
		});
		Properties pps = new Properties();
		this.addGlobalParams(pps, sce);
		this.parseFile(pps, file);
		for (File f : allRootConFig) {
			if (f.getName().equalsIgnoreCase(file.getName())) {
				continue;
			}
			this.parseFile(pps, f);
		}
		this.addBaseHome(pps, file);
		this.transParams(pps);
		System.getProperties().putAll(pps);
	}

	private void addGlobalParams(Properties pps, ServletContextEvent sce) {
		// 加载全局变量
		if (sce != null) {
			@SuppressWarnings("unchecked")
			Enumeration<String> e = sce.getServletContext().getInitParameterNames();
			while (e.hasMoreElements()) {
				String s = e.nextElement();
				pps.put(s, sce.getServletContext().getInitParameter(s));
			}
		}
	}

	private void transParams(Properties pps) {
		// 先把所有home变量进行加载，然后进行赋值
		String[] keys = pps.keySet().toArray(new String[0]);

		while (true) {
			String tempValue = null;
			for (String key : keys) {
				tempValue = pps.getProperty(key);

				if (tempValue != null && !"".equalsIgnoreCase(tempValue) && tempValue.contains("${")) {
					System.out.println("replacing property value. [" + key + "]=[" + tempValue + "]");
					for (String varName : keys) {
						if (varName.equalsIgnoreCase(key)) {
							continue;
						}

						String varValue = pps.getProperty(varName);
						// tempValue = tempValue.replace("${" + varName + "}",
						// varValue + File.separator);
						tempValue = tempValue.replace("${" + varName + "}", varValue);
					}
					pps.setProperty(key, tempValue);

				}
				tempValue = null;
			}
			boolean hasVar = false;
			for (String key : keys) {
				tempValue = pps.getProperty(key);
				if (tempValue.indexOf("${") >= 0) {
					hasVar = true;
					break;
				}
			}
			if (!hasVar) {
				break;
			}
		}
	}

	private void addBaseHome(Properties pps, File ownFile) {
		// 先把所有home变量进行加载，然后进行赋值
		String[] keys = pps.keySet().toArray(new String[0]);
		boolean needBaseHome = true;
		for (String key : keys) {
			String tempValue = pps.getProperty(key);
			if (SystemContextListener.BASE_HOME.equalsIgnoreCase(key) && tempValue != null && !"".equalsIgnoreCase(tempValue.trim())) {
				needBaseHome = false;
			}
		}
		if (needBaseHome) {
			String basehome = ownFile.getParentFile().getParentFile().getAbsolutePath();
			pps.setProperty(SystemContextListener.BASE_HOME, basehome);
		}
	}

	private void parseFile(Properties apps, File file) {

		Properties pps = new Properties();
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			System.out.println("RootConfig 不存在.");
		}
		try {
			pps.load(fs);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("读取RootConfig 时出现i/o异常.");
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("关闭RootConfig 时出现i/o异常.");
				}
			}
		}
		apps.putAll(pps);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
