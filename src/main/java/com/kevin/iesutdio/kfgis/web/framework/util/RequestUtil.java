package com.kevin.iesutdio.kfgis.web.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;

import com.kevin.iesutdio.kfgis.web.framework.exception.NServiceInternalException;

public class RequestUtil {

    private static void decompress(InputStream is, OutputStream os) {
        GZIPInputStream gis = null;
        try {
            gis = new GZIPInputStream(is);
            int count;
            byte data[] = new byte[1024];
            while ((count = gis.read(data, 0, 1024)) != -1) {
                os.write(data, 0, count);
            }
        } catch (IOException e) {
            throw new NServiceInternalException("解码gzip异常", e);
        } finally {
            if (gis != null) {
                try {
                    gis.close();
                } catch (IOException e) {
                    throw new NServiceInternalException("关闭gzip异常", e);
                }
            }
        }

    }
    public static byte[] getPostValue(InputStream request) {
        InputStream in = null;
        byte allBytes[] = new byte[0];
        
        try{
            in = request;
            int i = in.read();
            while (i != -1) {
                allBytes = ArrayUtils.add(allBytes, (byte)i);
                i = in.read();
            }
        }catch(Exception e){
            throw new NServiceInternalException("获取请求 stream 时异常", e);
        }finally {
            if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
             throw new NServiceInternalException("关闭请求流时异常", e);
            }
        }
  }
        return allBytes;
    }
//    public static byte[] getPostValue(InputStream request) {
//        InputStream in = null;
//        byte allBytes[] = new byte[0];
//
//        try {
//            in = request;
//            int len;
//            byte[] bytes = null;
//            while (true) {
//                bytes = new byte[1024];
//                len = in.read(bytes);
//                byte[] tempallBytes = new byte[allBytes.length + bytes.length];
//                System.arraycopy(allBytes, 0, tempallBytes, 0, allBytes.length);
//                System.arraycopy(bytes, 0, tempallBytes, allBytes.length, bytes.length);
//                allBytes = tempallBytes;
//
//                if (len <= 0) {
//                    break;
//                }
//            }
//
//        } catch (Exception e) {
//            if (e instanceof NServiceInternalException) {
//                throw (NServiceInternalException) e;
//            }
//            throw new NServiceInternalException("获取请求 stream 时异常", e);
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    throw new NServiceInternalException("关闭请求流时异常", e);
//                }
//            }
//        }
//        return allBytes;
//    }

    /**
     * MARKSOURCE fenghl add
     * 
     * @param clazz
     * @param fieldNameStr
     * @param declared
     * @return
     */
    public static byte[] getPostValue(HttpServletRequest request) {
        byte[] bytes = null;
        ByteArrayInputStream bsi = null;
        ByteArrayOutputStream bso = null;
        try {
            bytes = getPostValue(request.getInputStream());

//            String content_Encoding = request.getHeader("Content-Encoding");
//            if (bytes != null && bytes.length > 0 && content_Encoding != null && content_Encoding.toLowerCase().startsWith("gzip")) {
//                bsi = new ByteArrayInputStream(bytes);
//                bso = new ByteArrayOutputStream();
//                decompress(bsi, bso);
//                bytes = bso.toByteArray();
//            }
        } catch (Exception e) {
            if (e instanceof NServiceInternalException) {
                throw (NServiceInternalException) e;
            }
            throw new NServiceInternalException("获取请求stream时异常", e);
        } finally {
            if (bsi != null) {
                try {
                    bsi.close();
                } catch (IOException e) {
                    throw new NServiceInternalException("关闭解码临时输入流时异常", e);
                }
            }

            if (bso != null) {
                try {
                    bso.close();
                } catch (IOException e) {
                    throw new NServiceInternalException("关闭解码临时输出流时异常", e);
                }
            }
        }
        return bytes;

    }
}
