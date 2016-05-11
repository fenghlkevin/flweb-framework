//package cn.com.cennavi.kfgis.util;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * Created on 2011-12-21
// * <p>Title: WEB-T 公共参数校验功能模块</p>
// * <p>Copyright: Copyright (c) 2011</p>
// * <p>Company: 沈阳世纪高通科技有限公司</p>
// * <p>Department: 技术开发部</p>
// * 
// * @author caodezhi@cennavi.com.cn
// * @version 1.0
// * @update 修改日期 修改描述
// */
//public class CheckUtil {
//    /**
//     * <p>Discription:[校验是否为数字]</p>
//     * @return
//     * @author:wanglei
//     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
//     */
//    public static boolean isNum(String num) {
//        if (num == null || num.equals(""))
//            return false;
//        Pattern pattern = Pattern.compile("[1-9][0-9]*");
//        Matcher isNum = pattern.matcher(num);
//        return isNum.matches();
//    }
//
//    /**
//     * <p>Discription:[校验参数value是否在给定的enumerate的范围内；如2是否在1,2，3的范围内]</p>
//     * @param value
//     * @param enumerate
//     * @return
//     * @author:wanglei
//     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
//     */
//    public static boolean isEnumerate(String value, String enumerates) {
//        boolean aflag = false;
//        String[] allenumerate = enumerates.split(",");
//        for (String enumerate : allenumerate) {
//            if (value.equals(enumerate)) {
//                aflag = true;
//            }
//        }
//        return aflag;
//    }
//    
//    /**
//     * <p>Discription:[校验IP地址是否合法]</p>
//     * @param ipAddress IP地址
//     * @return
//     * @author liuchao
//     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
//     */
//    public static boolean validateIPAddress(String ipAddress) {
//        if (ipAddress == null || ipAddress.equals(""))
//            return false;
//        Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
//        Matcher matcher = pattern.matcher(ipAddress);
//        return matcher.matches();
//    }
//    
//    /**
//     * <p>Discription:[判断经纬度范围是否正确]</p>
//     * @param x
//     * @param y
//     * @return
//     * @author:caodezhi
//     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
//     */
//    public static boolean validateLonlat(double x,double y){
//        if(x > 180 || x < -180 || y > 90 || y < -90){
//            return false;
//        }
//        return true;
//    }
//    
//    /**
//     * <p>Discription:[校验六位国标码是否合法]</p>
//     * @param adcode    六位国标码
//     * @return
//     * @author liuchao
//     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
//     */
//    public static boolean validateAdcode(String adcode) {
//        if (adcode == null || adcode.equals(""))
//            return false;
//        Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
//        Matcher matcher = pattern.matcher(adcode);
//        return matcher.matches();
//    }
//    
//    /**
//     * <p>Discription:[校验传入时间是否合法]</p>
//     * @param timeStr    时间的字符串形式
//     * @return
//     * @author liuchao
//     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
//     */
//    public static boolean validateTime(String timeStr) {
//        if (timeStr == null || timeStr.equals(""))
//            return false;
//        /**
//         * 时间格式化对象
//         */
//        SimpleDateFormat timeFormater = new SimpleDateFormat("HH:mm");
//        try {
//            timeFormater.parse(timeStr);
//            return true;
//        } catch (ParseException e) {
//            return false;
//        }
//    }
//    
//    public static void main(String[] args) {
//        System.out.println(validateTime("21:36"));
//    }
//
//}
