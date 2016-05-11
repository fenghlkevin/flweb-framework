/**
 * 
 */
package com.kevin.iesutdio.kfgis.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


/**
 * Created on 2012-1-31
 * <p>Title: WEB-T GIS核心系统_公共模块_空间数据对象</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author liuchao liuchao@cennavi.com.cn
 * @version 1.0
 * @update 修改日期 修改描述
 */
@XStreamAlias("geom")
public class GeomObject implements Serializable{
    /**
     * <p>Description:[字段功能描述]</p>
     */
    private static final long serialVersionUID = 2399398408979707636L;
    /**
     * 类别
     * 包括：Point，LineString
     */
    @XStreamAlias("geomType")
    private String type;
    /**
     * 坐标点集合[[lon,lat][lon,lat]...]
     */
    @XStreamAsAttribute
    @XStreamImplicit(itemFieldName="coordinate")
    private String[][] coordinates;
    
    /**
     * <p>Discription:[属性type的GET方法]</p>
     * @return String type.
     */
    public String getType() {
        return type;
    }
    /**
     * <p>Discription:[属性type的SET方法]</p>
     * @param type The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * <p>Discription:[属性coordinates的GET方法]</p>
     * @return Object[] coordinates.
     */
    public String[][] getCoordinates() {
        return coordinates;
    }
    /**
     * <p>Discription:[属性coordinates的SET方法]</p>
     * @param coordinates The coordinates to set.
     */
    public void setCoordinates(String[][] coordinates) {
        this.coordinates = coordinates;
    }
    
}
