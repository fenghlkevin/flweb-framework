package cn.com.cennavi.kfgis.framework.exception.service;

import cn.com.cennavi.kfgis.framework.exception.NBaseServiceException;

/**
 * Created on 2012-11-29
 * <p>Title: WEB-T GIS核心系统_Empty response ( like empty route calculation result, empty search result) 响应为空（如算路结果为空、搜索结果为空等）</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author fengheliang fengheliang@cennavi.com.cn
 * @version 1.0
 * @update 修改日期 修改描述
 */
public class RttQueryException extends NBaseServiceException {
    
    {
        this.setStatusCode(150600);
    }
	
    /**
     * 
     */
    private static final long serialVersionUID = -3652190134343779675L;

    /** Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for 
     *          later retrieval by the {@link #getMessage()} method.
     */
    public RttQueryException(String message) {
        super(message);
        this.setStatusMsg(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.4
     */
    public RttQueryException(String message, Throwable cause) {
        super(message, cause);
        this.setStatusMsg(message);
    }
	
}
