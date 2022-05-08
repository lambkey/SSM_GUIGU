package net.seehope.crowd.annotation;

import net.seehope.crowd.entity.Admin;
import net.seehope.crowd.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author JoinYang
 * @date 2022/3/30 16:11
 */
public class LoginAcctIsExistValidator implements ConstraintValidator<LoginAcctIsExistAnnotation,Object> {
    @Autowired
    private AdminMapper adminMapper;
    @Override
    public void initialize(LoginAcctIsExistAnnotation userIsExistOrNoExist) {

    }
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        //校验方法
        //这里的o是前端所传进来的，也就是要注解地方的字段，在AdminVo类里
        String loginAcct= (String) o;
        Admin select = adminMapper.getAdminByLogAcct(loginAcct);
        return select==null;
    }

}
