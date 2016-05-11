package com.kevin.iesutdio.kfgis.app.framework.listener.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.kevin.iesutdio.kfgis.app.framework.zookeeper.ZKClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kevin.iesutdio.kfgis.app.framework.contant.LoggerContent;
import com.kevin.iesutdio.kfgis.app.framework.listener.task.abs.APPTask;
import com.kevin.iesutdio.kfgis.app.framework.util.NamedThreadFactory;
import com.kevin.iesutdio.kfgis.util.ObjectUtil;

/**
 * 任务监控线程
 * @author fengheliang
 *
 */
public class TaskMonitorListener  implements Runnable{

	private String taskPath;
	
	private String serverName;
	
	private List<APPTask> myTasks;
	
	/**
	 * 执行task的 线程池
	 */
	private ScheduledExecutorService executor;
	//private ThreadPoolExecutor executor;

	{
		myTasks=new ArrayList<APPTask>();
	}
	List<Runnable> curTasks=new ArrayList<Runnable>();
	private Logger logger=LoggerFactory.getLogger(LoggerContent.ZOOKEEPER_TASK_LOG);
	
	public void execute() {
		try {
			if(ZKClientFactory.getInstance().getCurator().checkExists().forPath(taskPath)==null){
				logger.error("Check Task List Path ["+taskPath+"] is NULL");
				return;
			}
		} catch (Exception e) {
			logger.error("Check Task List Path ["+taskPath+"] ERROR",e);
			return;
		}
		List<String> tasks;
		try {
			tasks=ZKClientFactory.getInstance().getCurator().getChildren().forPath(taskPath);
		} catch (Exception e) {
			logger.error("Get Zookeeper Task List ERROR",e);
			return ;
		}
		
		List<APPTask> tempTasks=new ArrayList<APPTask>();
		for(String taskStr:tasks){
			if(taskStr.indexOf(this.getServerName())>=0||taskStr.indexOf(APPTask.MUSTDO_TASKPATTERN)>=0){
				byte[] bs=null;
				try {
					bs=ZKClientFactory.getInstance().getCurator().getData().forPath(taskPath+"/"+taskStr);
				} catch (Exception e) {
					logger.error("Get My Task Config ERROR. taskName is ["+taskPath+"/"+taskStr+"]",e);
					continue;
				}
				if(bs==null){
					logger.error("Get My Task Config is NULL ERROR. taskName is ["+taskPath+"/"+taskStr+"]");
					continue;
				}
			
				APPTask cmd=(APPTask) ObjectUtil.getObjectBytes(bs);
				tempTasks.add(cmd);
			}
		}
		boolean needReStar=false;
		if(tempTasks.size()==myTasks.size()){
			for(APPTask c:tempTasks){
				if(!myTasks.contains(c)){
					needReStar=true;
					break;
				}
			}
		}else{
			needReStar=true;
		}
		
		if(needReStar){
			myTasks=tempTasks;
			if(executor!=null){
				if(curTasks != null && curTasks.size() > 0){
					for(Runnable c : curTasks){
						APPTask t = (APPTask)c;
						t.closeTask();
					}
				}
				executor.shutdownNow();
				curTasks = new ArrayList<Runnable>();
				logger.info("shutdown executor.");
				try{
					executor.awaitTermination(10L, TimeUnit.SECONDS);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			executor = Executors.newScheduledThreadPool(myTasks.size(), new NamedThreadFactory(this.getClass().getName()));
			/*executor = new ThreadPoolExecutor(myTasks.size(),myTasks.size()+1,600,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(1),  
					new ThreadPoolExecutor.DiscardOldestPolicy());*/
			for(APPTask c:tempTasks){
				Runnable cmd=(Runnable)c;
				curTasks.add(cmd);
				//executor.execute(cmd);
				//ScheduledFuture<?> sf=executor.scheduleWithFixedDelay(cmd, 2L, 10L, TimeUnit.SECONDS);
				ScheduledFuture<?> sf=executor.schedule(cmd, 0L, TimeUnit.SECONDS);
			}
		}
		
		//logger.info("MyTasks count is ["+myTasks.size()+"]");
		
		System.out.println("SimpleTaskWorkListener");
	}
	
	@Override
	public void run() {
		 execute();
	}

	public String getTaskPath() {
		return taskPath;
	}

	public TaskMonitorListener setTaskPath(String taskPath) {
		this.taskPath = taskPath;
		return this;
	}

	public String getServerName() {
		return serverName;
	}

	public TaskMonitorListener setServerName(String serverName) {
		this.serverName = serverName;
		return this;
	}
}
