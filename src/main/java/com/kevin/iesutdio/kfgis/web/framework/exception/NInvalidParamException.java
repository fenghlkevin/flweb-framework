package com.kevin.iesutdio.kfgis.web.framework.exception;

/**
 * Created on 2012-11-29
 * <p>Title: WEB-T GIS核心系统_Input parameter validation error.输入参数验证错误。</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 沈阳世纪高通科技有限公司</p>
 * <p>Department: 技术开发部</p>
 * 
 * @author fengheliang fengheliang@cennavi.com.cn
 * @version 1.0
 * @update 修改日期 修改描述
 */
public class NInvalidParamException extends NBaseServiceException {
    
    {
        this.setStatusCode(140000);
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
    public NInvalidParamException(String message) {
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
    public NInvalidParamException(String message, Throwable cause) {
        super(message, cause);
        this.setStatusMsg(message);
    }
    
    public NInvalidParamException(String errorCode,String message) {
        super(message);
        this.setStatusCodeStr(errorCode);
        this.setStatusMsg(message);
    }
    
    public NInvalidParamException(long errorCode,String message) {
        super(message);
        this.setStatusCode(errorCode);
        this.setStatusMsg(message);
    }
	
}
