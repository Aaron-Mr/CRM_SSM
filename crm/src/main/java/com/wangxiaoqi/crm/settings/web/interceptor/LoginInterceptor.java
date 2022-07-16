package com.wangxiaoqi.crm.settings.web.interceptor;

import com.wangxiaoqi.crm.commons.contants.Contants;
import com.wangxiaoqi.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //登录成功的时候，会将当前用户放入到session中
        User user = (User) request.getSession().getAttribute(Contants.SESSION_USER);
        //若session中的user为空即表示未登录
        if (user == null){
            //重定向到登录页面。request.getContextPath：表示当前项目的路径
            response.sendRedirect(request.getContextPath());
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
