package cn.com.cennavi.kfgis.framework.exception;

/**
 * 基础错误类
 * @author fengheliang
 *
 */
public abstract class NBaseServiceException extends RuntimeException {
    
    /**
     * 错误码
     */
    private long statusCode;
    
    /**
     * 用于不支持数字的错误码
     */
    private String statusCodeStr;
    
    /**
     * 错误描述
     */
    private String statusMsg;

    /**
     * 
     */
    private static final long serialVersionUID = 6772697126311007784L;

    /** Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for 
     *          later retrieval by the {@link #getMessage()} method.
     */
    public NBaseServiceException(String message) {
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
    public NBaseServiceException(String message, Throwable cause) {
        super(message, cause);
        this.setStatusMsg(message);
    }


    public void setStatusCode(long statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public long getStatusCode() {
        return statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }
    
    public String getStatusCodeStr() {
        return statusCodeStr;
    }

    public void setStatusCodeStr(String statusCodeStr) {
        this.statusCodeStr = statusCodeStr;
    }

}
