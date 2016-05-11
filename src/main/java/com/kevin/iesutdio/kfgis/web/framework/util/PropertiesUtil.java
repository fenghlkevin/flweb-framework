package com.kevin.iesutdio.kfgis.web.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.helpers.OptionConverter;

import com.kevin.iesutdio.kfgis.util.ObjectUtil;

public class PropertiesUtil<T> {

	@SuppressWarnings("unchecked")
	public T getProperties(Class<?> t, Map<String, String> values) {
		T rvalue = null;
		try {
			rvalue = (T) t.newInstance();

			// Field[] fields = t.getDeclaredFields();

			// Set<Entry<String, String>> sets=values.entrySet();
			Iterator<Entry<String, String>> iterator = values.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				String key = entry.getKey();
				String value = entry.getValue();

				Method m = ObjectUtil.getMethodByName(t, "set" + key, false);
				if (m == null) {
					continue;
				}
				Class<?> types[] = m.getParameterTypes();
				if (types == null || types.length > 1) {
					continue;
				}
				Class<?> cls = types[0];
				Object tv = value;
				if (Integer.class.isAssignableFrom(cls)) {
					tv = Integer.valueOf(value);
				} else if (Double.class.isAssignableFrom(cls)) {
					tv = Double.valueOf(value);
				} else if (Long.class.isAssignableFrom(cls)) {
					tv = Long.valueOf(value);
				} else if (Boolean.class.isAssignableFrom(cls)) {
					tv = OptionConverter.toBoolean(value, false);
				}

				m.setAccessible(true);
				m.invoke(rvalue, tv);
			}

			// for (Field field : fields) {
			// Class<?> cls = field.getType();
			// String fieldName = field.getName();
			// String tempValue = values.get(fieldName);
			// Object tv = null;
			// if (Integer.class.isAssignableFrom(cls)) {
			// tv = Integer.valueOf(tempValue);
			// } else if (Double.class.isAssignableFrom(cls)) {
			// tv = Double.valueOf(tempValue);
			// } else if (Boolean.class.isAssignableFrom(cls)) {
			// tv = OptionConverter.toBoolean(tempValue, false);
			// }
			//
			// field.setAccessible(true);
			// field.set(rvalue, tv);
			// }
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
		return rvalue;

	}

}
