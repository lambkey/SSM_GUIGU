package net.seehope.crowd.exception;

/**
 * @author JoinYang
 * @date 2022/4/7 20:29
 */
public class LoginAcctExistToUpdate extends RuntimeException{
    public LoginAcctExistToUpdate() {
        super();
    }

    public LoginAcctExistToUpdate(String message) {
        super(message);
    }

    public LoginAcctExistToUpdate(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctExistToUpdate(Throwable cause) {
        super(cause);
    }

    protected LoginAcctExistToUpdate(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
