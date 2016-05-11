package cn.com.cennavi.kfgis.framework.file;

import java.io.File;

import cn.com.cennavi.kfgis.framework.util.ObjUtil;

/**
 * 读取文件
 * @author fengheliang
 *
 */
public class CommonFileReader {
	
    /**
     * 
     * @param fileKey 如果没有输入fileKey值，并且是文件的话 使用文件名称（不使用扩展名）,否则返回false
     * @param path
     * @param parse
     * @parse immediate 强制刷新
     * @return 0:数据没变化 -1:执行失败 1:执行成功
     */
    public int execute(String fileKey, String path, IFileParse<?> parse,boolean immediate) {
        
        if(!immediate&&!parse.isNewFile(path)){
            return 0;
        }
        
        String tempKey = "";
        /**
         * 如果没有输入key值，并且是文件的话 使用文件名称（不使用扩展名）
         */
        if (ObjUtil.isEmpty(fileKey,true)) {
            File f = new File(path);
            if (!f.isFile()) {
                return -1;
            }
            if(f.getName().lastIndexOf(".")>=0){
                tempKey=f.getName().substring(0,f.getName().lastIndexOf("."));
            }else{
                tempKey=f.getName();
            }
        }else{
            tempKey=fileKey;
        }

        Object obj = parse.execute(path);
        if (obj == null) {
            return -1;
        }

        CommonFileMapContainer.getInstance().setFileMap(tempKey, obj);
        return 1;
    }
}
