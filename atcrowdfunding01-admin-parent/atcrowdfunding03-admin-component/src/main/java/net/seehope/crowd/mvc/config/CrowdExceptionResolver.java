package net.seehope.crowd.mvc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.seehope.crowd.constant.CrowdConstant;
import net.seehope.crowd.exception.*;
import net.seehope.crowd.util.CrowdUtil;
import net.seehope.crowd.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author JoinYang
 * @date 2022/3/17 12:47
 */
//@ControllerAdvice表示当前类是一个基于注解的异常处理类
@ControllerAdvice
public class CrowdExceptionResolver {

    @Autowired
    private ObjectMapper objectMapper;

    //@ExceptionHandler将一个具体的异常类型和一个方法关联起来
    // 登录失败异常处理
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailException(LoginFailedException exception, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //1、判断当前请求的类型
        boolean judgeRequestType = CrowdUtil.isAjaxRequestType(request);

        //2、如果是ajax请求
        if (judgeRequestType) {
            //3、创建JSONResult对象
            JSONResult jsonResult = JSONResult.FailureNeedMessage(exception.getMessage());
            //4、将异常信息传回浏览器
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(jsonResult));
        }

        //5、如果不是Ajax请求则创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();

        //6、将Exception存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);

        //7、设置对应的试图
        modelAndView.setViewName("admin-login");

        //8、放回modelAndView对象
        return modelAndView;
    }

    //@ExceptionHandler将一个具体的异常类型和一个方法关联起来
    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveNullPointerException(Exception exception, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //1、判断当前请求的类型
        boolean judgeRequestType = CrowdUtil.isAjaxRequestType(request);

        //2、如果是ajax请求
        if (judgeRequestType) {
            //3、创建JSONResult对象
            JSONResult jsonResult = JSONResult.FailureNeedMessage(exception.getMessage());
            //4、将异常信息传回浏览器
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(jsonResult));
        }

        //5、如果不是Ajax请求则创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();

        //6、将Exception存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);

        //7、设置对应的试图
        modelAndView.setViewName("system-error");

        //8、放回modelandview对象
        return modelAndView;
    }
    //@ExceptionHandler将一个具体的异常类型和一个方法关联起来
    @ExceptionHandler(value = LoginAcctExist.class)
    public ModelAndView resolveAcctExist(LoginAcctExist exception, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //1、判断当前请求的类型
        boolean judgeRequestType = CrowdUtil.isAjaxRequestType(request);

        //2、如果是ajax请求
        if (judgeRequestType) {
            //3、创建JSONResult对象
            JSONResult jsonResult = JSONResult.FailureNeedMessage(exception.getMessage());
            //4、将异常信息传回浏览器
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(jsonResult));
        }

        //5、如果不是Ajax请求则创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();

        //6、将Exception存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);

        //7、设置对应的试图
        modelAndView.setViewName("admin-add");

        //8、放回modelandview对象
        return modelAndView;
    }
    //@ExceptionHandler将一个具体的异常类型和一个方法关联起来
    @ExceptionHandler(value = LoginAcctExistToUpdate.class)
    public ModelAndView resolveAcctExistToUpdate(LoginAcctExistToUpdate exception, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //1、判断当前请求的类型
        boolean judgeRequestType = CrowdUtil.isAjaxRequestType(request);

        //2、如果是ajax请求
        if (judgeRequestType) {
            //3、创建JSONResult对象
            JSONResult jsonResult = JSONResult.FailureNeedMessage(exception.getMessage());
            //4、将异常信息传回浏览器
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(jsonResult));
        }

        //5、如果不是Ajax请求则创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();

        //6、将Exception存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);

        //7、设置对应的试图
        modelAndView.setViewName("admin-update");

        //8、放回modelandview对象
        return modelAndView;
    }
    
}
