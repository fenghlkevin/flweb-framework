//package cn.com.cennavi;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.zip.GZIPInputStream;
//import java.util.zip.GZIPOutputStream;
//
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpException;
//import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
//import org.apache.commons.httpclient.methods.PostMethod;
//
//public class TestGZip {
//
//    public static void main(String[] args) throws Exception {
//        TestGZip tg = new TestGZip();
//        tg.testGzip();
//    }
//
//    // 压缩   
//    public static byte[] compress(byte[] str) throws IOException {
//        if (str == null || str.length == 0) {
//            return new byte[0];
//        }
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        GZIPOutputStream gzip = new GZIPOutputStream(out);
//        gzip.write(str);
//        gzip.close();
//        return out.toByteArray();
//    }
//    
//    public static void decompress(InputStream is, OutputStream os)  
//            throws Exception {  
//      
//        GZIPInputStream gis = new GZIPInputStream(is);  
//      
//        int count;  
//        byte data[] = new byte[1024];  
//        while ((count = gis.read(data, 0, 1024)) != -1) {  
//            os.write(data, 0, count);  
//        }  
//      
//        gis.close();  
//    }  
//
//    public void testGzip() throws Exception {
//        HttpClient httpClient = new HttpClient();
//        PostMethod postMethod = new PostMethod("http://127.0.0.1:8090/bmwcns-gateway/cns?1=1359907911355&Vers=01.00&OP=srt&DriveID=76PM2JLR98Q13699D");
//
//        //GetMethod getMethod = new GetMethod("http://localhost/admin.jsp");
//        try {
//            postMethod.addRequestHeader("Content-Encoding","gzip");
//            //postMethod.addRequestHeader("Content-Type", "text/xml");
//            postMethod.addRequestHeader("accept-encoding", "gzip,deflate");
//            //postMethod.addRequestHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; Alexa Toolbar; Maxthon 2.0)");
//            String content = "<SRTRequest><Route><Length Unit=\"km\">47.230</Length><Duration Unit=\"h\"> 1.217</Duration><CostModel>1</CostModel><Criteria>4</Criteria><AgoraCString>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</AgoraCString></Route></SRTRequest>";
//            byte[] bs=content.getBytes("UTF-8");
//            bs=compress(bs);
//            ByteArrayRequestEntity entity=new ByteArrayRequestEntity(bs,"text/xml");
//            
////            StringRequestEntity entity = new StringRequestEntity(content, "text/xml", "UTF-8");
//            postMethod.setRequestEntity(entity);
//
//            int result = httpClient.executeMethod(postMethod);
//            FileOutputStream f=new FileOutputStream(new File("c:/a.txt"));
//            decompress(postMethod.getResponseBodyAsStream(),f);
//            if (result == 200) {
//                
//                
//                
//                System.out.println(postMethod.getResponseContentLength());
//                String html = postMethod.getResponseBodyAsString();
//                System.out.println(html);
//                System.out.println(html.getBytes().length);
//            }
//        } catch (HttpException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            postMethod.releaseConnection();
//        }
//    }
//
//}
