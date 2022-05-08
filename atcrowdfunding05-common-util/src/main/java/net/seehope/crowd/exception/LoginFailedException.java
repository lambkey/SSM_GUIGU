package net.seehope.crowd.exception;

/**
 * @author JoinYang
 * @date 2022/3/19 14:18
 *登录失败后抛出的异常
 */

public class LoginFailedException extends RuntimeException{
    private static final long serialVersionUID=1L;

    public LoginFailedException() {
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }

    public LoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
