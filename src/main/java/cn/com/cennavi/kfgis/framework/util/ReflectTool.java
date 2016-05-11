package cn.com.cennavi.kfgis.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectTool {
	
	public static Field getFieldByName(Object object, String fieldName) {
		Field[] fields = object.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equalsIgnoreCase(fieldName)) {
				return fields[i];
			}
		}
		return null;
	}
	
	public static Method getGetMethodByName(Object object, String fieldName) {
		return getMethodByName(object,"get"+fieldName,true);
	}
	
	public static Method getMethodByName(Object object, String fieldName,boolean declared) {
		Method[] methodes = null;
		if (declared) {
			methodes = object.getClass().getDeclaredMethods();
		} else {
			methodes = object.getClass().getMethods();
		}
		for (int i = 0; i < methodes.length; i++) {
			if (methodes[i].getName().equalsIgnoreCase(fieldName)) {
				return methodes[i];
			}
		}
		return null;
	}

	
	public static Method getSetMethodByName(Object object, String fieldName) {
		return getMethodByName(object,"set"+fieldName,true);
	}

	public static Object getValueByFieldName(Object object, String fieldName) {
		Method method = getGetMethodByName(object, fieldName);
		Object value = null;
		if (method != null) {
			try {
				value = method.invoke(object);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public static boolean setValueByFieldName(Object object, String fieldName, Object value) {
		Method method = getSetMethodByName(object, fieldName);
		Object[] params = { value };
		if (method != null) {
			try {
				method.invoke(object, params);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return false;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return false;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	
	/**
	 * 获取所有以fieldNameStr开头的方法
	 * @param object
	 * @param fieldNameStr
	 * @param declared
	 * @return
	 */
	public static Method[] getMethodsByNameStr(Class<?> clazz, String fieldNameStr,boolean declared) {
		List<Method> ms=new ArrayList<Method>();
		Method[] methodes = null;
		if (declared) {
			methodes = clazz.getDeclaredMethods();
		} else {
			methodes = clazz.getMethods();
		}
		for (int i = 0; i < methodes.length; i++) {
			if (methodes[i].getName().toLowerCase().startsWith(fieldNameStr)&&methodes[i].getParameterTypes().length==1) {
				ms.add(methodes[i]);
			}
		}
		return ms.toArray(new Method[ms.size()]);
	}
}
