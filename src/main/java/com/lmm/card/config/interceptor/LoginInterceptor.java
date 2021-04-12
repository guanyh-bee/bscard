package com.lmm.card.config.interceptor;

import com.lmm.card.entity.Operator;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Operator user = (Operator)request.getSession().getAttribute("user");
        if(user == null){
            response.sendRedirect("/op/toLogin");
            return  false;
        }else {
            return true;
        }

    }
}
