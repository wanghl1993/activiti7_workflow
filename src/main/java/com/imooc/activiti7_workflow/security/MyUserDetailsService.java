package com.imooc.activiti7_workflow.security;

import com.imooc.activiti7_workflow.mapper.UserInfoBeanMapper;
import com.imooc.activiti7_workflow.pojo.UserInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserInfoBeanMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*String password = passwordEncoder().encode("111");
        // 没有做任何数据库校验
        return new User(
                username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ACTIVITI_USER")
        );*/

        // 读取数据库判断用户
        // 如果用户是null抛出异常
        // 返回用户信息

        UserInfoBean userInfoBean = mapper.selectByUsername(username);
        if(userInfoBean == null){
            throw new UsernameNotFoundException("数据库中无此用户");
        }

        return userInfoBean;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
