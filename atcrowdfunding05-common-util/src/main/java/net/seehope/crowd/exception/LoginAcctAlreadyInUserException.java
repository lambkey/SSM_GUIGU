package net.seehope.crowd.exception;

/**
 * @author JoinYang
 * @date 2022/4/1 20:43
 */
// 检查或者更新Admin时如果检测到登录账号重复会抛出异常
public class LoginAcctAlreadyInUserException extends RuntimeException{

    private static final long serialVersionUID=1L;

    public LoginAcctAlreadyInUserException() {
    }

    public LoginAcctAlreadyInUserException(String message) {
        super(message);
    }

    public LoginAcctAlreadyInUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctAlreadyInUserException(Throwable cause) {
        super(cause);
    }

    public LoginAcctAlreadyInUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
