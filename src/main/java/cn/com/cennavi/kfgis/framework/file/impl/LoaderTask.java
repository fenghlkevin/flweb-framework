package cn.com.cennavi.kfgis.framework.file.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.cennavi.kfgis.framework.contant.LoggerContant;
import cn.com.cennavi.kfgis.framework.file.IFileParse;

//@Component(CompentsIDsContant.DATALOADERTASK_NAME)
public class LoaderTask extends AbstractLoaderTask implements Runnable{

	protected static Logger logger = LoggerFactory.getLogger(LoggerContant.DATALOAD_LOG_NAME);
	
	
	 {
		parsers=new ArrayList<IFileParse<?>>();
		parsersSysKey=new ArrayList<String>();
	}
	
	private  List<IFileParse<?>> parsers;
	
	private  List<String> parsersSysKey;
	
	public LoaderTask  addParser(IFileParse<?> parser,String parserSysKey){
		this.parsers.add(parser);
		this.parsersSysKey.add(parserSysKey);
		return this;
	}

	/**
	 * <p>
	 * Discription:[方法功能中文描述]
	 * </p>
	 *  
	 * @author:fengheliang
	 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
	 */
	public void load() {
		if (logger.isDebugEnabled()) {
			logger.debug("开始检测新数据 dataLoaderTask");
		}
		for(int i=0;i<parsers.size();i++){
			this.read(parsersSysKey.get(i), null,parsers.get(i), false);
		}
	}

	public List<String> getParsersSysKey() {
		return parsersSysKey;
	}

	@Override
	public void run() {
		this.load();
	}
	
}
