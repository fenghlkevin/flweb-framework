//package com.kevin.iesutdio.kfgis.app.framework.util;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//
//public class MD5 {
//
//    // 全局数组
//    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
//            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
//
//    public MD5() {
//    }
//
//    // 返回形式为数字跟字符串
//    private static String byteToArrayString(byte bByte) {
//        int iRet = bByte;
//        // System.out.println("iRet="+iRet);
//        if (iRet < 0) {
//            iRet += 256;
//        }
//        int iD1 = iRet / 16;
//        int iD2 = iRet % 16;
//        return strDigits[iD1] + strDigits[iD2];
//    }
//
//    // 返回形式只为数字
//    private static String byteToNum(byte bByte) {
//        int iRet = bByte;
//        System.out.println("iRet1=" + iRet);
//        if (iRet < 0) {
//            iRet += 256;
//        }
//        return String.valueOf(iRet);
//    }
//
//    // 转换字节数组为16进制字串
//    private static String byteToString(byte[] bByte) {
//        StringBuffer sBuffer = new StringBuffer();
//        for (int i = 0; i < bByte.length; i++) {
//            sBuffer.append(byteToArrayString(bByte[i]));
//        }
//        return sBuffer.toString();
//    }
//
//    public static String getMD5Code(byte[] bs){
//    	String resultString=null;;
//         try {
//             MessageDigest md = MessageDigest.getInstance("MD5");
//             // md.digest() 该函数返回值为存放哈希值结果的byte数组
//             resultString = byteToString(md.digest(bs));
//         } catch (NoSuchAlgorithmException ex) {
//        	 throw new RuntimeException("Get md5 code error",ex);
//         }
//         return resultString;
//    }
//
//    public static String getMD5Code(String strObj) {
//    	if(ObjUtil.isEmpty(strObj,true)){
//    		return "";
//    	}
//       return getMD5Code(strObj.getBytes());
//    }
//
//    public static void main(String[] args) {
//        MD5 getMD5 = new MD5();
//        System.out.println("MIB2   "+getMD5.getMD5Code("MIB2"));
//        System.out.println("DAIMLER   "+getMD5.getMD5Code("DAIMLER"));
//        System.out.println(getMD5.getMD5Code("0000000001"));
//    }
//}