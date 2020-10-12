package com.imooc.activiti7_workflow.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("loginFailureHandler")
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

//        httpServletResponse.setStatus(500);
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write("登录失败,原因是:" +e.getMessage());

    }

}
