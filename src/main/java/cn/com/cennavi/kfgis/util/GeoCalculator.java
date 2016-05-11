package cn.com.cennavi.kfgis.util;

import cn.com.cennavi.kfgis.bean.geos.GeoCoordinate;

public class GeoCalculator {

    private static final double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double calcLength(GeoCoordinate one, GeoCoordinate two) {
        double radLat1 = rad(one.getLat());
        double radLat2 = rad(two.getLat());
        double a = radLat1 - radLat2;
        double b = rad(one.getLon()) - rad(two.getLon());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        //s = Math.round(s * 10000) / 10000;
        return s;
    }
}
