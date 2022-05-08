package net.seehope.crowd.exception;

/**
 * @author JoinYang
 * @date 2022/4/14 19:14
 */
public class RoleNameExistToUpdate extends RuntimeException{
    public RoleNameExistToUpdate() {
        super();
    }

    public RoleNameExistToUpdate(String message) {
        super(message);
    }

    public RoleNameExistToUpdate(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleNameExistToUpdate(Throwable cause) {
        super(cause);
    }

    protected RoleNameExistToUpdate(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
