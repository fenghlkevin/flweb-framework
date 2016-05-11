package com.kevin.iesutdio.kfgis.web.framework.file;

import java.util.HashMap;
import java.util.Map;

public class CommonFileMapContainer {

	private CommonFileMapContainer() {
		filesMap = new HashMap<String, Object>();
	}

	private static CommonFileMapContainer container;

	
	public static CommonFileMapContainer getInstance() {
		if (container == null) {
			container = new CommonFileMapContainer();
		}
		return container;
	}
	
	private Map<String, Object> filesMap;

	private boolean useCouchbase;
	
	public Object getFileMap(String fieldName) {
		return filesMap.get(fieldName);
	}

	public void setFileMap(String fieldName, Object fileMap) {
		if (fileMap != null) {
			filesMap.put(fieldName, fileMap);
		}
	}

}
