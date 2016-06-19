//package com.kevin.iesutdio.kfgis.util;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//import org.slf4j.Logger;
//
//public class FileIOUtil<T> {
//
//    /**
//     * 把对象写入临时文件
//     * @param logger
//     * @param path
//     * @param writeObj
//     * @param fileFilters
//     * @param format
//     */
//    public void writeObj(Logger logger, String path, T writeObj, boolean append) {
//        File file = new File(path);
//        File pf=file.getParentFile();
//        if (!pf.exists() || pf.isFile()) {
//            pf.mkdirs();
//        }
//        //File file = new File(path);
//        FileOutputStream fo = null;
//        try {
//            fo = new FileOutputStream(file, append);
//            fo.write(ObjectUtil.getObjectBytes(writeObj));
//            fo.flush();
//        } catch (FileNotFoundException e) {
//            logger.error("写入文件失败 [{}]", new String[] { file.getAbsolutePath() }, e);
//        } catch (IOException e) {
//            logger.error("写入文件失败 [{}]", new String[] { file.getAbsolutePath() }, e);
//        } finally {
//            if (fo != null) {
//                try {
//                    fo.close();
//                } catch (IOException e) {
//                    logger.error("关闭FileOutputStream文件失败 [{}]", new String[] { file.getAbsolutePath() }, e);
//                }
//            }
//        }
//    }
//
//    /**
//     * 把对象写入临时文件
//     * @param logger
//     * @param path
//     * @param writeObj
//     * @param fileFilters
//     * @param format
//     */
//    public void writeObj(Logger logger, String path, T writeObj) {
//    	this.writeObj(logger, path, writeObj, false);
//    }
//
//    /**
//     * 把对象从文件中读取出来
//     * @param logger
//     * @param path
//     * @param fileFilters
//     * @param format
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public T readObj(Logger logger, String path) {
//        File f = new File(path);
//        if (f == null || !f.exists() || !f.isFile()) {
//            return null;
//        }
//
//        FileInputStream fi = null;
//        try {
//            fi = new FileInputStream(f);
//            byte[] bs = new byte[fi.available()];
//            fi.read(bs);
//            Object reObj = ObjectUtil.getObjectBytes(bs);
//            if(reObj==null){
//                return null;
//            }else{
//                return (T) ObjectUtil.getObjectBytes(bs);
//            }
//
//        } catch (FileNotFoundException e) {
//            logger.error("读取文件失败 [{}]", new String[] { f.getAbsolutePath() }, e);
//            return null;
//        } catch (IOException e) {
//            logger.error("读取文件失败 [{}]", new String[] { f.getAbsolutePath() }, e);
//        } finally {
//            if (fi != null) {
//                try {
//                    fi.close();
//                } catch (IOException e) {
//                    logger.error("关闭FileInputStream文件失败 [{}]", new String[] { f.getAbsolutePath() }, e);
//                }
//            }
//        }
//        return null;
//    }
//}
