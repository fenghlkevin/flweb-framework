//package com.kevin.iesutdio.kfgis.app.framework.listener.leader;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.Serializable;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.Set;
//
//import com.kevin.iesutdio.kfgis.app.framework.contant.LoggerContent;
//import com.kevin.iesutdio.kfgis.app.framework.contant.SystemPropertiesContant;
//import com.kevin.iesutdio.kfgis.app.framework.listener.task.abs.APPTask;
//import com.kevin.iesutdio.kfgis.app.framework.listener.task.abs.TaskTemplate;
//import com.kevin.iesutdio.kfgis.app.framework.zookeeper.ZKClientFactory;
//import com.kevin.iesutdio.kfgis.web.framework.file.CommonFileMapContainer;
//import com.kevin.iesutdio.tools.clazz.ObjUtil;
//import com.kevin.iesutdio.tools.clazz.ObjectUtil;
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.log4j.helpers.OptionConverter;
//import org.apache.zookeeper.CreateMode;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.kevin.iesutdio.kfgis.app.framework.dataloader.configfile.ConfigFile;
//import com.kevin.iesutdio.kfgis.app.framework.listener.leader.inf.IExecute;
//import com.kevin.iesutdio.kfgis.web.framework.exception.NServiceInternalException;
//
//public class SimpleLeaderExecuteImpl extends AbstractExecute implements IExecute {
//
//	private Logger logger = LoggerFactory.getLogger(LoggerContent.ZOOKEEPER_LEADER_DO_LOG);
//
//	private String configPath;
//
//	private String taskPath;
//
//	private String regPath;
//
//	private List<String> lastServers = null;
//
//	public SimpleLeaderExecuteImpl(String configPath, String taskPath, String regPath) {
//		this.configPath = configPath;
//		this.regPath = regPath;
//		this.taskPath = taskPath;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public void execute() throws Exception {
//		// 上传或下载最新的配置文件（比对通过最后修改时间戳、以及文件md5）（配置文件发生变化 ，也要重新分配任务）
//		// 收集注册server数据 （节点有变化，也要重新分配任务）
//		// 分配任务（如果任务有变化）
//		// 上传任务列表
//
//		boolean needReLoad = false;
//
//		CuratorFramework curator = ZKClientFactory.getInstance().getCurator();
//		Map<String, ConfigFile> localCfs = (Map<String, ConfigFile>) CommonFileMapContainer.getInstance().getFileMap(SystemPropertiesContant.SYSTEM_PROPERTIES_FOLDER);
//		if (!super.nodeExist(configPath)) {
//			logger.debug("Config Path [" + configPath + "] is not EXISTS. Need Load Local CONFIG.");
//			curator.create().creatingParentsIfNeeded().forPath(configPath);
//			needReLoad = true;
//		} else {
//			byte[] configBytes = curator.getData().forPath(configPath);
//			if (configBytes == null || configBytes.length <= 0) {
//				logger.debug("Config Path [" + configPath + "] Value is NULL. Need Load Local CONFIG.");
//				needReLoad = true;
//			} else {
//				Map<String, ConfigFile> zooCfs = (Map<String, ConfigFile>) ObjectUtil.getObjectBytes(configBytes);
//				if (!zooCfs.equals(localCfs) || !localCfs.equals(zooCfs)) {
//					needReLoad = true;
//				}
//			}
//		}
//
//	    if (needReLoad) {
//	        byte[] temp = ObjectUtil.getObjectBytes(localCfs);
//	        curator.setData().forPath(configPath, temp);
//	        logger.info("Upload New Configure Data to Zookeeper");
//	    }
//
//	    List<String> servers = curator.getChildren().forPath(regPath);
//	    boolean needReloadTask = needReLoad || lastServers == null || !lastServers.equals(servers);
//
//	    if (needReloadTask) {
//	        logger.info("Upload New Task to Zookeeper");
//	        ConfigFile tasklist = localCfs.get("tasklist.properties");
//	        logger.info("tasklist: " + tasklist.getFileName());
//	        // 获取注册的服务
//	        Map<String, APPTask> apps = this.createTasks(tasklist);
//	        logger.info("apps size: " + apps.size());
//	        try {
//	            curator.delete().deletingChildrenIfNeeded().forPath(taskPath);
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	        int s = 0;
//	        for (APPTask app : apps.values()) {
//	            if (!app.getSwitch()) {
//	                logger.info("APP [" + app.getName() + " Switch is off]");
//	                continue;
//	            }
//	            byte[] task = ObjectUtil.getObjectBytes(app);
//	            if (APPTask.MUSTDO_TASKPATTERN.equalsIgnoreCase(app.getType())) {
//	                curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(taskPath + "/" + APPTask.MUSTDO_TASKPATTERN + "_" + app.getName(), task);
//	            } else {
//	                if (s >= servers.size()) {
//	                    s = 0;
//	                }
//	                String serverName = servers.get(s).split("@")[0];
//	                curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(taskPath + "/" + serverName + "_" + app.getName(), task);
//	                s++;
//	            }
//	        }
//	        lastServers = servers;
//	    }
//		updateCurLeader();
//	}
//
//	private static final String TASK_TEMPLATE_PREFIX = "task.template.";
//
//	private static final String TASK_REAL_PREFIX = "task.real.";
//
//	private void updateCurLeader() throws Exception{
//        String leaderServerPath = System.getProperty(SystemPropertiesContant.LEADER_SERVER_PATH);
//        String serverName = System.getProperty(SystemPropertiesContant.SYSTEM_SERVICE_NAME);
//
//        CuratorFramework curator = ZKClientFactory.getInstance().getCurator();
//        boolean needReLoad = false;
//
//        if (!super.nodeExist(leaderServerPath)) {
//            logger.info("curLeader Path [" + leaderServerPath + "] is not EXISTS. Need Load.");
//            curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(leaderServerPath);
//            needReLoad = true;
//        } else {
//            byte[] leaderBytes = curator.getData().forPath(leaderServerPath);
//            if (leaderBytes == null || leaderBytes.length <= 0) {
//                logger.info("curLeader Path [" + leaderServerPath + "] Value is NULL. Need Load.");
//                needReLoad = true;
//            } else {
//                String curLeader = (String)ObjectUtil.getObjectBytes(leaderBytes);
//                if(!curLeader.equals(serverName)){
//                    needReLoad = true;
//                }
//            }
//        }
//        if (needReLoad) {
//            byte[] temp = ObjectUtil.getObjectBytes(serverName);
//            curator.setData().forPath(leaderServerPath, temp);
//            logger.info("Upload New LeaderName to Zookeeper");
//        }
//    }
//
//	private Map<String, APPTask> createTasks(ConfigFile tasklist) {
//		Properties taskpps = new Properties();
//		try {
//			taskpps.load(new ByteArrayInputStream(tasklist.getConfig()));
//		} catch (IOException e) {
//			logger.info("Parser TaskList ERROR", e);
//		}
//		// 取得所有的template
//		Map<String, TaskTemplate> templates = this.parseTaskTemplate(taskpps);
//		return this.parseTask(taskpps, templates);
//	}
//
//	private String getTValue(TaskTemplate tt, String prefix_real, Properties taskpps, String tname, Set<String> keys, Map<String, TaskTemplate> templates, String ttKey) {
//
//		String className = OptionConverter.findAndSubst(prefix_real, taskpps);
//		if (ObjUtil.isEmpty(className, true)) {
//			if (tt == null) {
//				throw new NServiceInternalException("Task  [" + tname + "] 's  Name [" + prefix_real + "] can not FIND!");
//			} else {
//				className = tt.getValues().get(ttKey);
//			}
//			if (ObjUtil.isEmpty(className, true)) {
//				throw new NServiceInternalException("Task  [" + tname + "] 's  Name [" + prefix_real + "] can not FIND!");
//			}
//		}
//		return className;
//	}
//
//	@SuppressWarnings("unchecked")
//	private Map<String, APPTask> parseTask(Properties taskpps, Map<String, TaskTemplate> templates) {
//		Map<String, APPTask> cmds = new HashMap<String, APPTask>();
//		// 根据task name prefix取得所有任务列表，并对列表中task进行赋值
//		Set<String> keys = taskpps.stringPropertyNames();
//		// 取得所有任务名称
//		Set<String> tnames = new HashSet<String>();
//		for (String key : keys) {
//			if (key.startsWith(TASK_REAL_PREFIX)) {
//				String tkeys[] = key.split("[.]");
//				if (tkeys.length > 2) {
//					tnames.add(tkeys[2]);
//				}
//			}
//		}
//		for (String tname : tnames) {
//			String prefix_template = TASK_REAL_PREFIX + tname + ".template";
//			String templateName = OptionConverter.findAndSubst(prefix_template, taskpps);
//			TaskTemplate tt = null;
//			if (templates.containsKey(templateName)) {
//				tt = templates.get(templateName);
//				if (tt == null) {
//					throw new NServiceInternalException("Task  [" + tname + "] 's Template [" + templateName + "] can not FIND!");
//				}
//			}
//
//			String prefix_real_class = TASK_REAL_PREFIX + tname + ".class";
//			String className = this.getTValue(tt, prefix_real_class, taskpps, tname, keys, templates, "class");
//
//			String prefix_real_type = TASK_REAL_PREFIX + tname + ".type";
//			String type = this.getTValue(tt, prefix_real_type, taskpps, tname, keys, templates, "type");
//
//			String prefix_real_level = TASK_REAL_PREFIX + tname + ".level";
//			String level = this.getTValue(tt, prefix_real_level, taskpps, tname, keys, templates, "level");
//
//			String prefix_real_switch = TASK_REAL_PREFIX + tname + ".switch";
//			String iswitch = this.getTValue(tt, prefix_real_switch, taskpps, tname, keys, templates, "switch");
//
//			Class<?> taskClazz = null;
//			try {
//				taskClazz = Class.forName(className);
//			} catch (ClassNotFoundException e) {
//				throw new NServiceInternalException("Task Class Name [" + className + "] can not FIND!", e);
//			}
//			if (!Runnable.class.isAssignableFrom(taskClazz) || !Serializable.class.isAssignableFrom(taskClazz) || !APPTask.class.isAssignableFrom(taskClazz)) {
//				throw new NServiceInternalException("Task Class Name [" + className + "] is not Implements Class [" + Runnable.class + "] or [" + Serializable.class + "] or ["
//						+ APPTask.class + "]!");
//			}
//			Method em = ObjectUtil.getMethodByName(taskClazz, "equals", true);
//			if (em == null) {
//				throw new NServiceInternalException("Task Class Name [" + className + "] is not Implements Method [public boolean equals(Object obj)]!");
//			}
//
//			APPTask at;
//			try {
//				at = (APPTask) taskClazz.newInstance();
//			} catch (InstantiationException e) {
//				throw new NServiceInternalException("Task Class Name [" + className + "] New Instance ERROR!", e);
//			} catch (IllegalAccessException e) {
//				throw new NServiceInternalException("Task Class Name [" + className + "] New Instance ERROR!", e);
//			}
//
//			at.setType(type);
//			at.setLevel(Integer.valueOf(level));
//			at.setName(tname);
//			at.setSwitch("on".equalsIgnoreCase(iswitch) ? true : false);
////			if (tt != null) {
////				at.setValues(tt.getValues());
////			}
//			Map<String, String> tempValues =  (tt == null ? new HashMap<String, String>() : (Map<String, String>)ObjectUtil.clone(tt.getValues()));
//			at.setValues(tempValues);
//
//			String startPrefix = TASK_REAL_PREFIX + tname + ".properties";
//			for (String key : keys) {
//				if (key.startsWith(startPrefix)) {
//					String tempValue = OptionConverter.findAndSubst(key, taskpps);
//					String tempKey = key.substring(startPrefix.length() + 1);
//					if (!ObjUtil.isEmpty(tempValue, true)) {
//						tempValues.put(tempKey, tempValue);
//					}
//				}
//			}
//			for (Map.Entry<String, String> entry : tempValues.entrySet()) {
//				String key = entry.getKey();
//				String tempValue = entry.getValue();
//				try {
//					Method method = ObjectUtil.getMethodByName(taskClazz, "set" + key, false);
//					if (method == null) {
//						logger.debug("Can not Find Method [set" + key + "]");
//						continue;
//					}
//					Class<?>[] types = method.getParameterTypes();
//					if (types == null || types.length > 1) {
//						logger.debug("Method [set" + key + "]'s Parameter is not 1 PARAM");
//						continue;
//					}
//					Class<?> fieldType = types[0];
//					Object tv = tempValue;
//					if (Integer.class.isAssignableFrom(fieldType)) {
//						tv = Integer.valueOf(tempValue);
//					} else if (Double.class.isAssignableFrom(fieldType)) {
//						tv = Double.valueOf(tempValue);
//					} else if (Boolean.class.isAssignableFrom(fieldType)) {
//						tv = OptionConverter.toBoolean(tempValue, false);
//					}
//					method.setAccessible(true);
//					method.invoke(at, tv);
//				} catch (Exception e) {
//					logger.debug("Task Class Name [" + className + "] Set Value [" + key + "] ERROR!", e);
//				}
//			}
//
//			cmds.put(tname, at);
//		}
//		return cmds;
//	}
//
//	private Map<String, TaskTemplate> parseTaskTemplate(Properties taskpps) {
//		Set<String> keys = taskpps.stringPropertyNames();
//		// Enumeration enumeration = taskpps.propertyNames();
//		// 取得所有的template name
//		Set<String> tnames = new HashSet<String>();
//		for (String key : keys) {
//			if (key.startsWith(TASK_TEMPLATE_PREFIX)) {
//				String tkeys[] = key.split("[.]");
//				if (tkeys.length > 2) {
//					tnames.add(tkeys[2]);
//				}
//			}
//		}
//		// 根据template name取得所有template 对象
//		Map<String, TaskTemplate> taskTemplateMap = new HashMap<String, TaskTemplate>();
//		for (String tname : tnames) {
//			// String prefix_class = TASK_TEMPLATE_PREFIX + tname + ".class";
//			// String prefix_type = TASK_TEMPLATE_PREFIX + tname + ".type";
//			// String className = OptionConverter.findAndSubst(prefix_class,
//			// taskpps);
//			// String taskType = OptionConverter.findAndSubst(prefix_type,
//			// taskpps);
//			// if (className == null || "".equalsIgnoreCase(className)) {
//			// throw new NServiceInternalException("Template [" + tname +
//			// "] Class Name is EMPTY!");
//			// }
//			// Class<?> clazz = null;
//			// try {
//			// clazz = Class.forName(className);
//			// } catch (ClassNotFoundException e) {
//			// throw new NServiceInternalException("Template Class Name [" +
//			// className + "] can not FIND!", e);
//			// }
//			// if (!TaskTemplate.class.isAssignableFrom(clazz)) {
//			// throw new NServiceInternalException("Template Class Name [" +
//			// className + "] is not extends Class [" + TaskTemplate.class +
//			// "]!");
//			// }
//			//
//			// TaskTemplate tt;
//			// try {
//			// tt = (TaskTemplate) clazz.newInstance();
//			// } catch (InstantiationException e) {
//			// throw new NServiceInternalException("Template Class Name [" +
//			// className + "] New Instance ERROR!", e);
//			// } catch (IllegalAccessException e) {
//			// throw new NServiceInternalException("Template Class Name [" +
//			// className + "] New Instance ERROR!", e);
//			// }
//			// tt.setTaskClass(className);
//			// tt.setTaskType(taskType);
//			TaskTemplate tt = new TaskTemplate();
//			String startPrefix = TASK_TEMPLATE_PREFIX + tname;
//			for (String key : keys) {
//				if (key.startsWith(startPrefix)) {// &&!key.equalsIgnoreCase(prefix_class)&&!key.equalsIgnoreCase(prefix_type)
//					String tempValue = OptionConverter.findAndSubst(key, taskpps);
//					String tempKey = key.substring(startPrefix.length() + 1);
//					tt.getValues().put(tempKey, tempValue);
//				}
//			}
//			taskTemplateMap.put(tname, tt);
//		}
//		return taskTemplateMap;
//	}
//
//}
