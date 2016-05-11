package com.kevin.iesutdio.kfgis.app.framework.listener.task.abs;

import java.util.HashMap;
import java.util.Map;

public class TaskTemplate {

		private Map<String, String> values = new HashMap<String, String>();

		public Map<String, String> getValues() {
			return values;
		}

		public void setValues(Map<String, String> values) {
			this.values = values;
		}
	}