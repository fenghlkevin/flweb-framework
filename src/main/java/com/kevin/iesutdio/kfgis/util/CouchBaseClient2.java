//package cn.com.cennavi.kfgis.util;
//
//import java.net.URI;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import net.spy.memcached.internal.OperationFuture;
//
//import NServiceInternalException;
//
//import com.couchbase.client.CouchbaseClient;
//
///**
// * 
// * Created on 2012-8-2
// * <p>Title: WEB-T GIS核心系统_CouchBase服务_CouchBase客户端程序</p>
// * <p>Copyright: Copyright (c) 2011</p>
// * <p>Company: 沈阳世纪高通科技有限公司</p>
// * <p>Department: 技术开发部</p>
// * 
// * @author Kevin Kevin@cennavi.com.cn
// * @version 1.0
// * @update 修改日期 修改描述
// */
//public class CouchBaseClient2 {
//
//    private static Logger loger = LoggerFactory.getLogger(CouchBaseClient2.class);
//
//    private static CouchBaseClient2 getInstance;
//
//    private static Map<String, CouchbaseClient> clientMap;
//
//    private static CouchBaseParams cbp;
//
//    public static CouchBaseClient2 newInstance(CouchBaseParams cbp) {
//        //if (clientMap==null||clientMap.isEmpty()) {
//        getInstance = new CouchBaseClient2(cbp);
//        //}
//        return getInstance;
//    }
//
//    public static CouchBaseClient2 getInstance() {
//        if (getInstance == null || clientMap == null || clientMap.isEmpty()) {
//            throw new NServiceInternalException("初始化CouchBase客户端管理程序时，未返回正确的数据对象");
//        }
////        CouchbaseClient client = clientMap.get(key);
////        if (client == null) {
////            throw new GisQueryServiceException("CouchBase客户端不存在，未返回正确的数据对象");
////        }
//        return getInstance;
//    }
//
//    private CouchBaseClient2(CouchBaseParams cbp) {
//
//        CouchBaseClient2.cbp = cbp;
//        createClient();
//    }
//
//    public static class CouchBaseParams {
//
//        /**
//         * value值配置方法 couchbase address,couchbase address#bucketName
//         */
//        private Map<String, String> couchBaseParamGroup;
//
//        public Map<String, String> getCouchBaseParamGroup() {
//            return couchBaseParamGroup;
//        }
//
//        public void setCouchBaseParamGroup(Map<String, String> couchBaseParamGroup) {
//            this.couchBaseParamGroup = couchBaseParamGroup;
//        }
//
//    }
//
//    public void createClient() {
//        clientMap = new HashMap<String, CouchbaseClient>();
//        String[] keys = cbp.getCouchBaseParamGroup().keySet().toArray(new String[0]);
//
//        CouchbaseClient lastclient = null;
//        String lastClientParam = "";
//        for (String key : keys) {
//            String clientParam = cbp.getCouchBaseParamGroup().get(key);
//            CouchbaseClient client = null;
//            if (lastClientParam.equalsIgnoreCase(clientParam)) {
//                client = lastclient;
//            }
//            else {
//                List<URI> uris = new LinkedList<URI>();
//                String temp[] = clientParam.split("#");
//                String ips[] = temp[0].split(",");
//
//                for (String ip : ips) {
//                    uris.add(URI.create(ip));
//                }
//
//                try {
//                    client = new CouchbaseClient(uris, temp[1], "");
//                }
//                catch (Exception e) {
//                    loger.error("Error connecting to Couchbase " + key + "", e);
//                }
//            }
//            clientMap.put(key, client);
//            lastClientParam = clientParam;
//            lastclient = client;
//        }
//    }
//
//    public boolean add(String clientKey, String key, Object value, Integer expire) {
//        if (value == null) {
//            return false;
//        }
//        CouchbaseClient client = clientMap.get(clientKey);
//        if (client == null) {
//            loger.error("Error connecting to Couchbase {}", new String[] { clientKey });
//            return false;
//        }
//
//        OperationFuture<Boolean> of = null;
//        if (client.get(key) != null) {
//            of = client.replace(key, expire, value);
//        }
//        else {
//            of = client.add(key, expire, value);
//        }
//
//        return of == null ? false : of.getStatus().isSuccess();
//
//    }
//
//    public Object get(String clientKey, String key) {
//        CouchbaseClient client = clientMap.get(clientKey);
//        if (client == null) {
//            loger.error("Error connecting to Couchbase {}", new String[] { clientKey });
//            return null;
//        }
//
//        Object temp = client.get(key);
//        return temp;
//    }
//
//}
