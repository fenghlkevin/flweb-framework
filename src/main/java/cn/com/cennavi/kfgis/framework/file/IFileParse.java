package cn.com.cennavi.kfgis.framework.file;

/**
 * 文件的读取类，并返回解析后数据
 * @author fengheliang
 *
 */
public interface IFileParse<T> {
    
    /**
     * 执行数据解析
     * @param filePath
     * @return
     */
    public abstract T execute(String filePath);
    
    /**
     * 是否存在新文件
     * @param filePath
     * @return
     */
    public abstract boolean isNewFile(String filePath);
    
}
