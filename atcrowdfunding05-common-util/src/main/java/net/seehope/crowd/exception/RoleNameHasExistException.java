package net.seehope.crowd.exception;

/**
 * @author JoinYang
 * @date 2022/4/13 19:44
 */
public class RoleNameHasExistException extends RuntimeException{
    public RoleNameHasExistException() {
        super();
    }

    public RoleNameHasExistException(String message) {
        super(message);
    }

    public RoleNameHasExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleNameHasExistException(Throwable cause) {
        super(cause);
    }

    protected RoleNameHasExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
