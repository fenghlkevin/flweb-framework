package cn.com.cennavi.kfgis.framework.configure;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.cennavi.kfgis.framework.exception.NServiceInternalException;

/**
 * Created on 2012-9-22
 * <p>Title: configure读取器</p>
 * <p>Copyright: Copyright (c) 2012</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author Kevin Kevin@cennavi.com.cn
 * @version 1.0
 * @update 修改日期 修改描述
 */
public final class ConfigureFactory {

    /**
     * <p>Description:[单例]</p>
     */
    private static ConfigureFactory configure;

    /**
     * <p>Discription:[单例调用方法]</p>
     * @return
     * @author:Kevin
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static ConfigureFactory getInstance() {
        if (configure == null) {
            configure = new ConfigureFactory();
        }
        return configure;
    }

    private Map<String, String> keys;

    private Map<String, String> groups;

    private Map<Class<?>, Object> configureMap = new HashMap<Class<?>, Object>();

    public Map<String, String> getKeys() {
        return keys;
    }

    public void setKeys(Map<String, String> keys) {
        this.keys = keys;
    }

    public Map<String, String> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, String> groups) {
        this.groups = groups;
    }

    public Object getConfigure(Class<?> clazz) {
        if (!configureMap.containsKey(clazz)) {
            Object obj = resolveVariable(clazz);
            configureMap.put(clazz, obj);
        }
        return configureMap.get(clazz);
    }

    /**
     * <p>Discription:[方法功能中文描述]</p>
     * @param clazz
     * @return
     * @throws Exception
     * @author:Kevin Feng
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    private Object resolveVariable(Class<?> clazz) {
        Object obj;
        try {
            obj = clazz.newInstance();
        }
        catch (InstantiationException e) {
            throw new NServiceInternalException("实例化对象时:实例化错误",e); 
        }
        catch (IllegalAccessException e) {
            throw new NServiceInternalException("实例化对象时:权限不足",e); 
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String groupID = field.getName();
            if (this.getGroups().containsKey(groupID)) {
                /**
                 * 如果group中存在 则建立group并把数据加入group中
                 */

                Object groupObj;
                try {
                    groupObj = field.get(obj);
                }
                catch (IllegalArgumentException e) {
                    throw new NServiceInternalException("参数不全错误",e); 
                }
                catch (IllegalAccessException e) {
                    throw new NServiceInternalException("设置值时，权限不足",e); 
                }
                boolean reNew = groupObj == null ? true : false;
                Method initMethod = null;
                Object[] initValue = null;
                if (field.getType().equals(Map.class)) {
                    try {
                        initMethod = Map.class.getDeclaredMethod("put", Object.class, Object.class);
                    }
                    catch (SecurityException e) {
                        throw new NServiceInternalException("Map对象 put值错误",e); 
                    }
                    catch (NoSuchMethodException e) {
                        throw new NServiceInternalException("Map对象 put值错误",e); 
                    }
                    initValue = new Object[2];
                    groupObj = reNew ? groupObj = new HashMap<Object, Object>() : groupObj;
                }
                else if (field.getType().equals(List.class)) {
                    try {
                        initMethod = List.class.getDeclaredMethod("add", Object.class);
                    }
                    catch (SecurityException e) {
                        throw new NServiceInternalException("List对象 add值错误",e); 
                    }
                    catch (NoSuchMethodException e) {
                        throw new NServiceInternalException("List对象 add值错误",e); 
                    }
                    initValue = new Object[1];
                    groupObj = reNew ? groupObj = new ArrayList<Object>() : groupObj;
                }
                if (reNew) {
                    try {
                        field.set(obj, groupObj);
                    }
                    catch (IllegalArgumentException e) {
                        throw new NServiceInternalException("Object Set值错误",e); 
                    }
                    catch (IllegalAccessException e) {
                        throw new NServiceInternalException("Object Set值权限不足",e); 
                    }
                }
                String[] keys = this.getGroups().get(groupID).split(",");

                Class<?> valueClass = null;
                String typeStr = field.getGenericType().toString();
                if (typeStr.lastIndexOf(Long.class.getName()) == typeStr.length() - 1 - Long.class.getName().toString().length()) {
                    valueClass = Long.class;
                }
                else if (typeStr.lastIndexOf(Integer.class.getName()) == typeStr.length() - 1 - Integer.class.getName().toString().length()) {
                    valueClass = Integer.class;
                }
                else if (typeStr.lastIndexOf(Double.class.getName()) == typeStr.length() - 1 - Double.class.getName().toString().length()) {
                    valueClass = Double.class;
                }
                else if (typeStr.lastIndexOf(String.class.getName()) == typeStr.length() - 1 - String.class.getName().toString().length()) {
                    valueClass = String.class;
                }
                else {
                    throw new NServiceInternalException("传入数据不符合数据类型"); 
                }
                /**
                 * 转换group中所有的value值
                 */
                Constructor<?> constructor;
                try {
                    constructor = valueClass.getDeclaredConstructor(String.class);
                }
                catch (SecurityException e) {
                    throw new NServiceInternalException("查询构造函数时错误",e); 
                }
                catch (NoSuchMethodException e) {
                    throw new NServiceInternalException("查询构造函数时错误",e); 
                }
                if (constructor == null) {
                    throw new NServiceInternalException("构造函数为空"); 
                }
                for (int i = 0; i < keys.length; i++) {
                    Object valueObj;
                    try {
                        valueObj = constructor.newInstance(this.getKeys().get(keys[i]));
                    }
                    catch (IllegalArgumentException e) {
                        throw new NServiceInternalException("实例化构造函数时异常",e); 
                    }
                    catch (InstantiationException e) {
                        throw new NServiceInternalException("实例化构造函数时异常",e); 
                    }
                    catch (IllegalAccessException e) {
                        throw new NServiceInternalException("实例化构造函数时异常",e); 
                    }
                    catch (InvocationTargetException e) {
                        throw new NServiceInternalException("实例化构造函数时异常",e); 
                    }
                    // changedValues[i]=valueObj;
                    if (initValue.length == 1) {
                        initValue[0] = valueObj;
                    }
                    else if (initValue.length == 2) {
                        initValue[0] = keys[i];
                        initValue[1] = valueObj;
                    }
                    try {
                        initMethod.invoke(groupObj, initValue);
                    }
                    catch (IllegalArgumentException e) {
                        throw new NServiceInternalException("invoke对象时异常",e); 
                    }
                    catch (IllegalAccessException e) {
                        throw new NServiceInternalException("invoke对象时异常",e); 
                    }
                    catch (InvocationTargetException e) {
                        throw new NServiceInternalException("invoke对象时异常",e); 
                    }
                    initValue = new Object[initValue.length];
                }
            }
            else if (this.getKeys().containsKey(field.getName())) {
                /**
                 * 如果group中没有，则在keys中寻找
                 */
                Class<?> valueClass = null;
                if (field.getType().equals(Long.class)) {
                    valueClass = Long.class;
                }
                else if (field.getType().equals(Integer.class)) {
                    valueClass = Integer.class;
                }
                else if (field.getType().equals(Double.class)) {
                    valueClass = Double.class;
                }
                else if (field.getType().equals(String.class)) {
                    valueClass = String.class;
                } else if (field.getType().equals(Boolean.class)) {
                    valueClass = String.class;
                }
                Constructor<?> constructor;
                try {
                    constructor = valueClass.getDeclaredConstructor(String.class);
                }
                catch (SecurityException e) {
                    throw new NServiceInternalException("查询构造函数时异常",e); 
                }
                catch (NoSuchMethodException e) {
                    throw new NServiceInternalException("查询构造函数时异常",e); 
                }
                if (constructor == null) {
                    throw new NServiceInternalException("构造函数为空"); 
                }
                Object valueObj;
                try {
                    valueObj = constructor.newInstance(this.getKeys().get(field.getName()));
                }
                catch (IllegalArgumentException e) {
                    throw new NServiceInternalException("实例化构造函数时异常",e);  
                }
                catch (InstantiationException e) {
                    throw new NServiceInternalException("实例化构造函数时异常",e); 
                }
                catch (IllegalAccessException e) {
                    throw new NServiceInternalException("实例化构造函数时异常",e); 
                }
                catch (InvocationTargetException e) {
                    throw new NServiceInternalException("实例化构造函数时异常",e); 
                }
                try {
                    field.set(obj, valueObj);
                }
                catch (IllegalArgumentException e) {
                    throw new NServiceInternalException("Object set对象时错误",e); 
                }
                catch (IllegalAccessException e) {
                    throw new NServiceInternalException("Object set对象时错误",e); 
                }
            }
//            else {
//                //throw new NServiceInternalException(); 
//            }
        }
        return obj;
    }
}
