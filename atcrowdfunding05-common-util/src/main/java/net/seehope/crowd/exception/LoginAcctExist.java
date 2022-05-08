package net.seehope.crowd.exception;

/**
 * @author JoinYang
 * @date 2022/4/2 0:32
 */
public class LoginAcctExist extends RuntimeException{
    public LoginAcctExist() {
    }

    public LoginAcctExist(String message) {
        super(message);
    }

    public LoginAcctExist(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctExist(Throwable cause) {
        super(cause);
    }

    public LoginAcctExist(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
