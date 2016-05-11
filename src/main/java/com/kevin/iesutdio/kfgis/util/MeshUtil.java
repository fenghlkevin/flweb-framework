package com.kevin.iesutdio.kfgis.util;

public class MeshUtil {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String pointLon = MeshUtil.x2mm("122.073391");
        String pointLat = MeshUtil.x2mm("41.119471");
//        System.out.println(calc_mesh(pointLon, pointLat));
        pointLon = MeshUtil.x2mm("122.074455");
        pointLat = MeshUtil.x2mm("41.119652");
//        System.out.println(calc_mesh(pointLon, pointLat));

    }

    public static String calc_mesh(String pointLon, String pointLat) {
        pointLon = MeshUtil.x2mm(pointLon);
        pointLat = MeshUtil.x2mm(pointLat);

        double x = new Double(pointLon).doubleValue();
        double y = new Double(pointLat).doubleValue();
        // 获取mesh
        double sdMashXNum = (x - MESH_BASE_COORX) / MESH_BASE_LENGTHX;
        double sdMashYNum = (y - MESH_BASE_COORY) / MESH_BASE_LENGTHY;
        double udMeshNo = (int) sdMashXNum * PARMETER_0X10000 + (int) sdMashYNum;
        String mesh = MeshUtil.mapabcPathFill(Integer.toHexString((int) udMeshNo));
        // 获取block
        double sdBlockXNum = (x - (MESH_BASE_COORX + Integer.parseInt(mesh.substring(0, 4), 16) * MESH_BASE_LENGTHX))
                / BLOCK_BASE_LENGTHX;
        double sdBlockYNum = (y - (MESH_BASE_COORY + Integer.parseInt(mesh.substring(5, 8), 16) * MESH_BASE_LENGTHY))
                / BLOCK_BASE_LENGTHY;
        double udBlockNo = (int) sdBlockXNum * PARMETER_0X10000 + (int) sdBlockYNum;

        String block = MeshUtil.mapabcPathFill(Integer.toHexString((int) udBlockNo));

        //      return MapabcConstant.MESH_HEAD_NAME + mesh + "/" + MapabcConstant.BLOCK_HEAD_NAME + block + MapabcConstant.BLOCK_END_NAME;
        return mesh + "/" + block;
    }
    
    /**
     * <p>
     * Discription:[计算最终结果时的乘数]
     * </p>
     */
    private final static double PARMETER_0X10000 = 0x10000;

    /**
     * <p>
     * Discription:[转换经纬度需要乘的倍数]
     * </p>
     */
//    private final  static double PARMETER_3600000 = 3600000;

    /**
     * <p>
     * Discription:[全国的左下角经度28度]
     * </p>
     */
    private final  static double MESH_BASE_COORX = 100800000;

    /**
     * <p>
     * Discription:[全国的左下角纬度1.33度]
     * </p>
     */
    private final  static double MESH_BASE_COORY = 4788000;

    /**
     * <p>
     * Discription:[mesh框的长(1/1000)]
     * </p>
     */
    private final  static double MESH_BASE_LENGTHX = 3600000;

    /**
     * <p>
     * Discription:[mesh框的宽(1/1000)]
     * </p>
     */
    private final  static double MESH_BASE_LENGTHY = 2400000;

    /**
     * <p>
     * Discription:[mesh框的长(1/1000)]
     * </p>
     */
    private final  static double BLOCK_BASE_LENGTHX = 112500;

    /**
     * <p>
     * Discription:[mesh框的宽(1/1000)]
     * </p>
     */
    private final  static double BLOCK_BASE_LENGTHY = 75000;
    
    /**
     * <p>
     * Discription:[给字符串补位]
     * </p>
     * 
     * @param args
     * @return
     * @author:[冯贺亮]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static String mapabcPathFill(String args) {
        if (args.length() < 8) {
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < 8 - args.length(); i++) {
                str.append("0");
            }
            str.append(args);
            return str.toString();
        }
        return args;
    }
    
    /**
     * <p>
     * Discription:[度分秒转度]
     * </p>
     * 
     * @param dd
     * @param ms
     * @param ss
     * @return
     * @author:[冯贺亮]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static String xyz2x(int dd, int ms, int ss) {
        double temp = dd + ((double) ms / (double) 60) + ((double) ss / (double) 3600);
        return String.valueOf(temp);
    }

    /**
     * <p>
     * Discription:[度转秒]
     * </p>
     * 
     * @param dd
     * @return
     * @author:[冯贺亮]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static String x2mm(String dd) {
        return String.valueOf((int) (new Double(dd) * 3600000));
    }

    /**
     * <p>
     * Discription:[度分秒转秒]
     * </p>
     * 
     * @param dd
     * @param ms
     * @param ss
     * @return
     * @author:[冯贺亮]
     * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static String xyz2mm(int dd, int ms, int ss) {
//        System.out.println(x2mm(xyz2x(dd, ms, ss)));
        return x2mm(xyz2x(dd, ms, ss));
    }

}
