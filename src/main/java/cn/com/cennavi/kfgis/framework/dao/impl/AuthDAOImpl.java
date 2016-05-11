package cn.com.cennavi.kfgis.framework.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.com.cennavi.kfgis.framework.contant.SQLContants;
import cn.com.cennavi.kfgis.framework.dao.IAuthDAO;

/**
 * Created on 2011-12-19
 * <p>Title: WEB-T GIS核心系统_XX服务_XX功能模块</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author Kevin Feng Kevin Feng@cennavi.com.cn
 * @version 1.0
 * @update 修改日期 修改描述
 */
@Repository
public class AuthDAOImpl extends JdbcTemplate implements IAuthDAO {

    private Logger logger = LoggerFactory.getLogger(AuthDAOImpl.class);

    public String getServerid() {
        return System.getProperty("server.serverid");
    }

    //    @Override
    //    public void queryStaticConfigure(final Map<String, String> keys, final Map<String, String> groups) {
    //        Object objs[]=null;
    //        String sql=SQLContants.SQL_PARAM_STATIC_CONFIGURE_NOSERVER;
    //        if(this.getServerid()!=null){
    //            objs=new Object[]{this.getServerid()};
    //            sql=SQLContants.SQL_PARAM_STATIC_CONFIGURE;
    //        }
    //        this.queryConfigure(sql, keys, groups,objs);
    //    }

    @Override
    public void queryDynamicConfigure(Map<String, String> keys, Map<String, String> groups) {
        Object objs[] = null;
        String sql = SQLContants.SQL_PARAM_DYNAMIC_CONFIGURE_NOSERVER;
        if (this.getServerid() != null) {
            objs = new Object[] { this.getServerid() };
            sql = SQLContants.SQL_PARAM_DYNAMIC_CONFIGURE;
        }
        this.queryConfigure(sql, keys, groups, objs);
    }

    public void queryErrorConfigure(Map<String, String> keys, Map<String, String> groups) {
//        this.queryConfigure(SQLContants.SQL_PARAM_ERROR_CONFIGURE, keys, groups, new Object[0]);

    }

    //    @Override
    //    public void queryAuthUser(Map<String, String> keys, Map<String, String> groups) {
    //        this.queryConfigure(SQLContants.SQL_PARAM_AUTH_USER, keys, groups,new Object[0]);
    //        
    //    }
    /**
     * <p>Discription:[查询配置文件参数]</p>
     * @param sql
     * @param keys
     * @param groups
     * @author:Kevin Feng
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    @SuppressWarnings("rawtypes")
    private void queryConfigure(final String sql, final Map<String, String> keys, final Map<String, String> groups, Object[] objs) {
//        List<Map<String, Object>> resultList = null;
        List<Map<String, Object>> resultList = this.queryForList(sql, objs);
        if (resultList != null && resultList.size() > 0) {
            for (Map info : resultList) {
                String s1 = info.get("a") + "";
                String s2 = info.get("b") + "";
                String groupid = info.get("c") + "";
                if (keys.containsKey(s1)) {
                    logger.error("Load Configure 配置参数 [" + s1 + "] 重复，后项会覆盖前项配置. [" + sql + "]");
                }
                keys.put(s1, s2);
                if (groupid != null && !"".equalsIgnoreCase(groupid)) {
                    String str = groups.get(groupid);
                    if (str == null || "".equalsIgnoreCase(str)) {
                        str = "";
                    }
                    str += ("".equalsIgnoreCase(str) ? "" : ",") + s1;
                    groups.put(groupid, str);
                }
            }
        }
    }

    /**
     * <p>Discription:[行政区域信息]</p>
     * @param sql
     * @param keys
     * @param groups
     * @author:Kevin Feng
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
//    public HashMap<String, String> getAdcodeNameMap() {
//        final HashMap<String, String> resultMap = new HashMap<String, String>();
//        //        JdbcTemplate jdbc = this.getJdbcTemplate();
//        this.query("SELECT ADCODE,NAME FROM geo_adcode_search", new RowCallbackHandler() {
//            @Override
//            public void processRow(ResultSet rs) throws SQLException {
//                resultMap.put(rs.getString(1), rs.getString(2));
//            }
//        });
//        return resultMap;
//    }

}
