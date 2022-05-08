package net.seehope.crowd.interceptor;

import net.seehope.crowd.constant.CrowdConstant;
import net.seehope.crowd.entity.Admin;
import net.seehope.crowd.exception.AccessForbiddenException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author JoinYang
 * @date 2022/3/20 13:14
 */
//登录拦截器，要去mvc注册才生效
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1、通过request对象获取Session对象
        HttpSession session=request.getSession();

        //2、尝试从Session域中获取Admin对象
        Admin admin= (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN_SESSION);

        //3、判断Session域中是否有Admin对象
        if (admin==null){
            throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
        }
        //如果Admin对象不为null，则放行
        return true;
    }
}
