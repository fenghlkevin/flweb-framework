package cn.com.cennavi.kfgis.util;

public class MeshUtil2 {

//    public static void main(String[] args) {
//        System.out.println(calc_mesh(123.435729392563, 41.7684619330484));
//    }

    private static final double MESH1_N = 2.0 / 3.0; // 1次Mesh高度单位

    private static final double MESH1_E = 1.0; // 1次Mesh宽度单位

    private static final double MESH2_N = 2.0 / 3.0 / 8.0; // 2次Mesh高度单位

    private static final double MESH2_E = 1.0 / 8.0; // 2次Mesh宽度单位

    private static final double LONGI_LEN = 60; // 经度参照长度

    //private static final int PRECISION = 128;//数据精度

    public static String calc_mesh(double lon, double lat) {
        long mesh1_n;
        long mesh1_e;
        long mesh2_n;
        long mesh2_e;
        double dN, dE;
        double north8;
        double east8;
        dE = lon; /// ((double)3600 * (double)PRECISION);
        dN = lat; /// ((double)3600 * (double)PRECISION);
        // MESH 1'WD IS MESH1'S WD *1.5
        mesh1_n = (long) (dN / MESH1_N);
        // MESH 1'JD IS MESH1'S JD -100
        mesh1_e = (long) (dE / MESH1_E - LONGI_LEN);
        // MESH 2'JD IS MESH1'S REMAINDER / 8
        north8 = dN - mesh1_n * MESH1_N;
        mesh2_n = (long) (north8 / MESH2_N + 0.5);
        // MESH 2'WD IS MESH1'S REMAINDER / 8
        east8 = dE - (mesh1_e + LONGI_LEN) * MESH1_E;
        mesh2_e = (long) (east8 / MESH2_E);
        //  Get the Mesh Code 
        String f1 = mesh1_n < 9 ? "0" + mesh1_n : String.valueOf(mesh1_n);
        String f2 = mesh1_e < 9 ? "0" + mesh1_n : String.valueOf(mesh1_e);
        String f3 = String.valueOf(mesh2_n);
        if (mesh2_n > 9) {
            String temp = String.valueOf(mesh2_n);
            f3 = temp.substring(temp.length() - 1, temp.length());
        }
        String f4 = String.valueOf(mesh2_e);
        if (mesh2_e > 9) {
            String temp = String.valueOf(mesh2_e);
            f4 = temp.substring(temp.length() - 1, temp.length());
        }

        return f1 + f2 + f3 + f4;
    }
}
