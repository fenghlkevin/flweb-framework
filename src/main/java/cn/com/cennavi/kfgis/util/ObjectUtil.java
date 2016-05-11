package cn.com.cennavi.kfgis.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.cennavi.kfgis.bean.RequestBean;
import cn.com.cennavi.kfgis.bean.ResponseBean;

/**
 * @author Kevin Feng
 * 
 */
public class ObjectUtil {

	private static Log logger = LogFactory.getLog(ObjectUtil.class);

	/**
	 * 转换二进制流道对象
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object getObjectBytes(byte[] bytes) {
		ByteArrayInputStream bo = null;
		ObjectInputStream oo = null;
		try {
			bo = new ByteArrayInputStream(bytes);
			oo = new ObjectInputStream(bo);
			return oo.readObject();
		} catch (IOException e) {
			logger.error("读取对象二进制流时失败", e);
			// e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			logger.error("读取TFPMessage对象时,类型转换错误", e);
			// e.printStackTrace();
			return null;
		} finally {
			if (bo != null) {
				try {
					bo.close();
				} catch (IOException e) {
					logger.error("读取对象二进制流时失败时，关闭流失败", e);
				}
			}
			if (oo != null) {
				try {
					oo.close();
				} catch (IOException e) {
					logger.error("读取对象二进制流时失败，关闭流失败", e);
				}
			}

		}
	}

	
	/**
	 * @param
	 * @return
	 */
	public static byte[] getObjectBytesByTopic(Object obj) {
		byte[] bs = null;
				
		if(obj instanceof RequestBean){
			RequestBean bean = (RequestBean)obj;
			bs = bean.toString().getBytes();
		}else if(obj instanceof ResponseBean){
			ResponseBean bean = (ResponseBean)obj;
			bs = bean.toString().getBytes();
		}
		return bs;
		
//		ByteArrayOutputStream bo = null;
//		ObjectOutputStream oo = null;
//		try {
//			bo = new ByteArrayOutputStream();
//			oo = new ObjectOutputStream(bo);
//			oo.writeObject(obj);// 源对象
//			byte[] bs = bo.toByteArray();
//			return bs;
//		} catch (IOException e) {
//			logger.error("读取对象二进制流时失败", e);
//			return null;
//		} finally {
//			if (bo != null) {
//				try {
//					bo.close();
//				} catch (IOException e) {
//					logger.error("读取对象二进制流时失败时，关闭流失败", e);
//				}
//			}
//			if (oo != null) {
//				try {
//					oo.close();
//				} catch (IOException e) {
//					logger.error("读取对象二进制流时失败，关闭流失败", e);
//				}
//			}
//		}
	}
	
	
	/**
	 * @param
	 * @return
	 */
	public static byte[] getObjectBytes(Object obj) {
		ByteArrayOutputStream bo = null;
		ObjectOutputStream oo = null;
		try {
			bo = new ByteArrayOutputStream();
			oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);// 源对象
			byte[] bs = bo.toByteArray();
			return bs;
		} catch (IOException e) {
			logger.error("读取对象二进制流时失败", e);
			return null;
		} finally {
			if (bo != null) {
				try {
					bo.close();
				} catch (IOException e) {
					logger.error("读取对象二进制流时失败时，关闭流失败", e);
				}
			}
			if (oo != null) {
				try {
					oo.close();
				} catch (IOException e) {
					logger.error("读取对象二进制流时失败，关闭流失败", e);
				}
			}

		}
	}

	public static boolean isRightValueInArray(int array[]) {
		if (array == null || array.length <= 0) {
			return false;
		}
		for (int v : array) {
			if (v <= 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Description: [是否为数字]
	 * <p>
	 * 
	 * @param args
	 * @return
	 * @author [冯贺亮]创建于[2007-11-22]
	 */
	public static boolean isNumber(String args) {
		boolean isnumber = true;
		if (args == null || args.trim().length() <= 0) {
			return false;
		}
		for (int i = 0; i < args.length(); i++) {
			if (Character.isDigit(args.charAt(i)) || args.charAt(i) == '.' || args.charAt(i) == '-') {
				continue;
			} else {
				isnumber = false;
			}
		}
		return isnumber;
	}

	/**
	 * 判断一个子类是否继承某个类 或实现某接口
	 * 
	 * @param child
	 * @param parent
	 * @return
	 */
	public static boolean isParentClass(Class<?> child, Class<?> parent) {
		Class<?> temp = child;
		for (; temp != null && !temp.equals(Object.class); temp = temp.getSuperclass()) {
			// if(temp==null){break;}
			if (parent.equals(temp)) {
				return true;
			}
			Class<?> faces[] = temp.getInterfaces();
			for (Class<?> face : faces) {
				if (face.equals(parent)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		ObjectUtil.isParentClass(ArrayList.class, List.class);
	}

	/**
	 * @param city
	 * @return
	 */
	public static Object clone(Object obj) {
		ByteArrayOutputStream bo = null;
		ObjectOutputStream oo = null;
		ByteArrayInputStream bi = null;
		ObjectInputStream oi = null;
		Object newObj = null;
		try {
			bo = new ByteArrayOutputStream();
			oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);// 源对象
			bi = new ByteArrayInputStream(bo.toByteArray());
			oi = new ObjectInputStream(bi);
			newObj = oi.readObject();// 目标对象
		} catch (IOException e) {
			logger.error("深度克隆City对象时失败", e);
			return null;
		} catch (ClassNotFoundException e) {
			logger.error("深度克隆City对象时失败", e);
			return null;
		} finally {
			if (bo != null) {
				try {
					bo.close();
				} catch (IOException e) {
					logger.error("深度克隆City对象时，关闭流失败", e);
				}
			}
			if (oo != null) {
				try {
					oo.close();
				} catch (IOException e) {
					logger.error("深度克隆City对象时，关闭流失败", e);
				}
			}
			if (bi != null) {
				try {
					bi.close();
				} catch (IOException e) {
					logger.error("深度克隆City对象时，关闭流失败", e);
				}
			}
			if (oi != null) {
				try {
					oi.close();
				} catch (IOException e) {
					logger.error("深度克隆City对象时，关闭流失败", e);
				}
			}
		}
		return newObj;
	}

	public static Method getSetMethodByName(Object object, String fieldName) {
		return getMethodByName(object, "set" + fieldName, false);
	}

	public static Method getSetMethodByName(Object object, String fieldName, boolean declared) {
		return getMethodByName(object, "set" + fieldName, declared);
	}

	public static Method getMethodByName(Object object, String fieldName, boolean declared) {
		return getMethodByName(object.getClass(), fieldName, declared);
	}

	public static Method getMethodByName(Class<?> clazz, String fieldName, boolean declared) {
		Method[] methodes = null;
		if (declared) {
			methodes = clazz.getDeclaredMethods();
		} else {
			methodes = clazz.getMethods();
		}
		for (int i = 0; i < methodes.length; i++) {
			if (methodes[i].getName().equalsIgnoreCase(fieldName)) {
				return methodes[i];
			}
		}
		return null;
	}

}
