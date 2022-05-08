package net.seehope.crowd.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author JoinYang
 * @date 2022/3/30 16:04
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginAcctIsExistValidator.class)
public @interface LoginAcctIsExistAnnotation {
    String message() default "login_acct is exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
