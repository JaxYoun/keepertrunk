package com.troy.keeper.core.error;

/**
 * Created with IntelliJ IDEA.
 * User: Harry
 * Date: 17-6-29
 * Time: 下午4:24
 * To change this template use File | Settings | File Templates.
 */
public class KeeperException extends RuntimeException {
    /**
     * 错误编码
     */
    private int code;

    /**
     * Constructs an KeeperException with no detail message.
     * A detail message is a String that describes this particular exception.
     */
    public KeeperException() {
        super();
    }
    /**
     * 自定义错误编码构造函数
     */
    public KeeperException(int code,String msg) {
        super(msg);
        this.code = code;
    }
    /**
     * Constructs an KeeperException with the specified detail
     * message.  A detail message is a String that describes this particular
     * exception.
     *
     * @param s the String that contains a detailed message
     */
    public KeeperException(String s) {
        super(s);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * <p>Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this exception's detail
     * message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link Throwable#getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link Throwable#getCause()} method).  (A <tt>null</tt> value
     *         is permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since 1.5
     */
    public KeeperException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link Throwable#getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.5
     */
    public KeeperException(Throwable cause) {
        super(cause);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    static final long serialVersionUID = -1848914673093119416L;
}
