package cn.com.cennavi.kfgis.bean.geos;

import java.io.Serializable;

/**
 * 地理坐标对象
 * 
 * @author fenghl
 * 
 */
public class GeoCoordinate implements Serializable{
    
    private static final long serialVersionUID = -1342578381996245321L;

    private double _lat;

    private double _lon;

    /**
     * @param lon:经度
     * @param lat：纬度
     */
    public GeoCoordinate(double lon, double lat) {
        this._lat = lat;
        this._lon = lon;
    }

    @Override
    public String toString() {
        return "GeoCoordinate = " + this._lon + "," + this._lat;
    }

    public double getLat() {
        return _lat;
    }

    public void setLat(double lat) {
        this._lat = lat;
    }

    public double getLon() {
        return _lon;
    }

    public void setLon(double lon) {
        this._lon = lon;
    }

}
