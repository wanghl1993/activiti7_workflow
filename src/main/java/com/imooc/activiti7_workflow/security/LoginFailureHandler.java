package com.imooc.activiti7_workflow.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.activiti7_workflow.util.AjaxResponse;
import com.imooc.activiti7_workflow.util.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

//        httpServletResponse.setStatus(500);
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(
                objectMapper.writeValueAsString(
                        AjaxResponse.AjaxData(
                                GlobalConfig.ResponseCode.ERROR.getCode(),
                                GlobalConfig.ResponseCode.ERROR.getDesc(),
                                "登录失败,原因是:" + e.getMessage()
                        )
                )
        );

    }

}
