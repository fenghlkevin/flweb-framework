//package cn.com.cennavi.kfgis.framework.exception;
//
//import org.springframework.validation.ObjectError;
//
//
///**
// * Created on 2012-11-29
// * <p>Title: WEB-T GIS核心系统_公共服务_连接异常 返回130102错误</p>
// * <p>Copyright: Copyright (c) 2011</p>
// * <p>Company: 沈阳世纪高通科技有限公司</p>
// * <p>Department: 技术开发部</p>
// * 
// * @author fengheliang fengheliang@cennavi.com.cn
// * @version 1.0
// * @update 修改日期 修改描述
// */
//public class NetConnectionException extends BaseServiceException {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -4097374894833629043L;
//
//	private ObjectError error;
//
//	/**
//	 * Constructs a new runtime exception with <code>null</code> as its detail
//	 * message. The cause is not initialized, and may subsequently be
//	 * initialized by a call to {@link #initCause}.
//	 */
//	public NetConnectionException(ObjectError error) {
//		super();
//		this.error = error;
//	}
//
//	/**
//	 * Constructs a new runtime exception with the specified detail message. The
//	 * cause is not initialized, and may subsequently be initialized by a call
//	 * to {@link #initCause}.
//	 * 
//	 * @param message
//	 *            the detail message. The detail message is saved for later
//	 *            retrieval by the {@link #getMessage()} method.
//	 */
//	public NetConnectionException(String message, ObjectError error) {
//		super(message);
//		this.error = error;
//	}
//
//	/**
//	 * Constructs a new runtime exception with the specified detail message and
//	 * cause.
//	 * <p>
//	 * Note that the detail message associated with <code>cause</code> is
//	 * <i>not</i> automatically incorporated in this runtime exception's detail
//	 * message.
//	 * 
//	 * @param message
//	 *            the detail message (which is saved for later retrieval by the
//	 *            {@link #getMessage()} method).
//	 * @param cause
//	 *            the cause (which is saved for later retrieval by the
//	 *            {@link #getCause()} method). (A <tt>null</tt> value is
//	 *            permitted, and indicates that the cause is nonexistent or
//	 *            unknown.)
//	 * @since 1.4
//	 */
//	public NetConnectionException(String message, Throwable cause, ObjectError error) {
//		super(message, cause);
//		this.error = error;
//	}
//
//	/**
//	 * Constructs a new runtime exception with the specified cause and a detail
//	 * message of <tt>(cause==null ? null : cause.toString())</tt> (which
//	 * typically contains the class and detail message of <tt>cause</tt>). This
//	 * constructor is useful for runtime exceptions that are little more than
//	 * wrappers for other throwables.
//	 * 
//	 * @param cause
//	 *            the cause (which is saved for later retrieval by the
//	 *            {@link #getCause()} method). (A <tt>null</tt> value is
//	 *            permitted, and indicates that the cause is nonexistent or
//	 *            unknown.)
//	 * @since 1.4
//	 */
//	public NetConnectionException(Throwable cause, ObjectError error) {
//		super(cause);
//		this.error = error;
//	}
//
//	/**
//	 * Constructs a new runtime exception with <code>null</code> as its detail
//	 * message. The cause is not initialized, and may subsequently be
//	 * initialized by a call to {@link #initCause}.
//	 */
//	public NetConnectionException() {
//		super();
//	}
//
//	/**
//	 * Constructs a new runtime exception with the specified detail message. The
//	 * cause is not initialized, and may subsequently be initialized by a call
//	 * to {@link #initCause}.
//	 * 
//	 * @param message
//	 *            the detail message. The detail message is saved for later
//	 *            retrieval by the {@link #getMessage()} method.
//	 */
//	public NetConnectionException(String message) {
//		super(message);
//	}
//
//	/**
//	 * Constructs a new runtime exception with the specified detail message and
//	 * cause.
//	 * <p>
//	 * Note that the detail message associated with <code>cause</code> is
//	 * <i>not</i> automatically incorporated in this runtime exception's detail
//	 * message.
//	 * 
//	 * @param message
//	 *            the detail message (which is saved for later retrieval by the
//	 *            {@link #getMessage()} method).
//	 * @param cause
//	 *            the cause (which is saved for later retrieval by the
//	 *            {@link #getCause()} method). (A <tt>null</tt> value is
//	 *            permitted, and indicates that the cause is nonexistent or
//	 *            unknown.)
//	 * @since 1.4
//	 */
//	public NetConnectionException(String message, Throwable cause) {
//		super(message, cause);
//	}
//
//	/**
//	 * Constructs a new runtime exception with the specified cause and a detail
//	 * message of <tt>(cause==null ? null : cause.toString())</tt> (which
//	 * typically contains the class and detail message of <tt>cause</tt>). This
//	 * constructor is useful for runtime exceptions that are little more than
//	 * wrappers for other throwables.
//	 * 
//	 * @param cause
//	 *            the cause (which is saved for later retrieval by the
//	 *            {@link #getCause()} method). (A <tt>null</tt> value is
//	 *            permitted, and indicates that the cause is nonexistent or
//	 *            unknown.)
//	 * @since 1.4
//	 */
//	public NetConnectionException(Throwable cause) {
//		super(cause);
//	}
//
//	@Override
//    public ObjectError getError() {
//		return error;
//	}
//
//	@Override
//    public void setError(ObjectError error) {
//		this.error = error;
//	}
//	
//}
