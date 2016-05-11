package cn.com.cennavi.kfgis.framework.dao;

import java.util.Map;

public interface IAuthDAO {
    
    /**
     * <p>Discription:[静态参数查询功能]</p>
     * @param serverid:服务id
     * @param keys: key-value
     * @param groups:key-groupid
     * @author:冯贺亮
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
//    public void queryStaticConfigure(Map<String,String> keys,Map<String,String> groups);
    
    /**
     * <p>Discription:[动态参数查询功能]</p>
     * @param serverid:服务id
     * @param keys: key-value
     * @param groups:key-groupid
     * @author:冯贺亮
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public void queryDynamicConfigure(Map<String,String> keys,Map<String,String> groups);
    
    /**
     * <p>Discription:[错误码查询]</p>
     * @param keys
     * @param groups
     * @author:Kevin Feng
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
//    public void queryErrorConfigure(Map<String,String> keys,Map<String,String> groups);
    
    /**
     * 
     * <p>Discription:[查询 auth用户信息]</p>
     * @param keys
     * @param groups
     * @author:Kevin
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
//    public void queryAuthUser(Map<String, String> keys, Map<String, String> groups) ;
    
    /**
     * <p>Discription:[行政区域信息]</p>
     * @param sql
     * @param keys
     * @param groups
     * @author:Kevin Feng
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
//    public HashMap<String, String> getAdcodeNameMap();
}
