/**
 * 
 */
package cn.com.cennavi.kfgis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.cennavi.kfgis.bean.GeomObject;
import cn.com.cennavi.kfgis.bean.geos.GeoCoordinate;

/**
 * Created on 2012-1-31
 * <p>Title: WEB-T GIS核心系统_公共模块_将空间数据转换成输出对象</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author liuchao liuchao@cennavi.com.cn
 * @version 1.0
 * @update 修改日期 修改描述
 */
public class GeomUtil {
    /**
     * 输出对象
     */
    private GeomObject geomObject = new GeomObject();
    /**
     * 错误异常
     */
    private static final Exception NotMatheException = new Exception("输入的空间数据字符串格式不正确");
    private static final Exception ObjectTypeException = new Exception("输出对象的类型设置不正确，目前只支持 point 和 line");
    private static final Exception NoTypeInfoException = new Exception("请先调用 setGeomObjectType 方法设置对象类别");
    /**
     * 点类型对象的坐标数组
     */
    private String[] pointCoordinateArray = new String[2];
    /**
     * 线类型对象的坐标列表
     */
    private List<String[]> lineCoordinateList = new ArrayList<String[]>();
    
    /**
     * <p>Discription:[将空间数据转换成对象]</p>
     * @param geomText      空间数据字符串
     * @return  GeomObject  输出对象
     * @author liuchao
     * @throws  Exception     转换失败      
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static GeomObject geomToObject(String geomText) throws  Exception {
        /**
         *  输入示例
         *  点：POINT(120.153602 30.265554)
         *  线：MULTILINESTRING((119.969999 31.785248,119.969146 31.78569,119.96796 31.786233,119.966587 31.78672))
         *  面：
         */
        /**
         * 输出对象
         */
        GeomObject geomObject = new GeomObject();
        /**
         * 从空间数据字符串中截取类别信息
         */
        String typeInGeom = geomText.substring(0, geomText.indexOf("("));
        /**
         * 匹配空间数据字符串的正则表达式
         */
        Pattern pattern = Pattern.compile("([A-Z]+)(\\(+)([^\\(\\)]*)(\\)+)");
        /**
         * 匹配对象
         */
        Matcher matcher = pattern.matcher(geomText);
        /**
         * 判断是否匹配，如果不匹配则抛出错误信息
         */
        if(!matcher.find()) {
            throw NotMatheException;
        }
        /**
         * 从空间数据字符串中截取坐标信息
         */
        String coordinatesInGeom = matcher.group(3);
        
        /**
         * 判断截取到的类别信息是否正确
         */
        if(!"POINT".equals(typeInGeom) && !"MULTILINESTRING".equals(typeInGeom)) {
            throw NotMatheException;
        }
        
        /**
         * 分析空间数据字符串，将相应信息赋值给输出对象
         */
        if("POINT".equals(typeInGeom)) {
            geomObject.setType("Point");
            String[][] point=new String[1][];
            point[0]=coordinatesInGeom.split(" ");
            geomObject.setCoordinates(point);
        } else if("MULTILINESTRING".equals(typeInGeom)) {
            geomObject.setType("LineString");
            String coordinatesInGeomA[]= coordinatesInGeom.split(",");
            String[][] coordinatesArray = new String[coordinatesInGeomA.length][];
            for(int i = 0;i < coordinatesInGeomA.length;i ++) {
                coordinatesArray[i] = coordinatesInGeomA[i].split(" ");
            }
            geomObject.setCoordinates(coordinatesArray);
        }
        
        return geomObject;
    }
    
    /**
     * <p>Discription:[获取Point对象的简便方法]</p>
     * @param lon
     * @param lat
     * @return
     * @author:fengheliang
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static GeomObject getPoint(double lon,double lat){
        GeomObject geomObject = new GeomObject();
        geomObject.setType("Point");
        String[][] point=new String[1][];
        point[0]=new String[]{String.valueOf(lon),String.valueOf(lat)};
        geomObject.setCoordinates(point);
        return geomObject;
    }
    
    /**
     * <p>Discription:[获取Point对象的简便方法]</p>
     * @param lonlat
     * @return
     * @author:fengheliang
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static GeomObject getPoint(GeoCoordinate lonlat){
        GeomObject geomObject = new GeomObject();
        geomObject.setType("Point");
        String[][] point=new String[1][];
        point[0]=new String[]{String.valueOf(lonlat.getLon()),String.valueOf(lonlat.getLat())};
        geomObject.setCoordinates(point);
        return geomObject;
    }
    
    /**
     * <p>Discription:[直接输出对象]</p>
     * @return  GeomObject  输出对象
     * @throws  Exception     没有设置对象类型      
     * @author liuchao
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public GeomObject getGeomObject() throws  Exception {
        /**
         * 判断是否设置了对象类别，如果没有设置则抛出错误
         */
        if("".equals(geomObject.getType())) {
            throw NoTypeInfoException;
        }
        
        /**
         * 根据设置的对象类型，将相应信息赋值给输出对象
         */
        if("Point".equals(geomObject.getType())) {
            String[][] point=new String[1][];
            point[0]=pointCoordinateArray;
            geomObject.setCoordinates(point);
        } else if("LineString".equals(geomObject.getType())) {
            geomObject.setCoordinates(lineCoordinateList.toArray(new String[lineCoordinateList.size()][]));
        }
        
        return geomObject;
    }
    
    /**
     * 
     * <p>Discription:[设置对象输出类别，目前支持 点：point；线：line]</p>
     * @param type  类别信息，目前支持 点：point；线：line
     * @throws  Exception     对象类型设置不正确  
     * @author liuchao
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public void setGeomObjectType(String type)  throws  Exception {
        /**
         * 判断设置的类别信息是否正确
         */
        if("point".equalsIgnoreCase(type)) {
            geomObject.setType("Point");
        } else if("line".equalsIgnoreCase(type)) {
            geomObject.setType("LineString");
        } else {
            throw ObjectTypeException;
        }
    }
    
    /**
     * 
     * <p>Discription:[添加坐标点]</p>
     * @param lon  坐标点经度
     * @param lat  坐标点纬度
     * @throws  Exception
     * @author liuchao
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public void addPoint(String lon, String lat)  throws  Exception {
        /**
         * 判断是否设置了对象类别，如果没有设置则抛出错误
         */
        if("".equals(geomObject.getType())) {
            throw NoTypeInfoException;
        }
        
        /**
         * 将输入的坐标加入到对应的类型数组中
         */
        if("Point".equals(geomObject.getType())) {
            pointCoordinateArray = new String[]{lon, lat};
        } else if("LineString".equals(geomObject.getType())) {
            lineCoordinateList.add(new String[]{lon, lat});
        } else {
            throw ObjectTypeException;
        }
    }
}
