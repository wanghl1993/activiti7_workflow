package com.imooc.activiti7_workflow.mapper;

import com.imooc.activiti7_workflow.pojo.UserInfoBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserInfoBeanMapper {

    // generator
    @Select("select * from user where username = #{username}")
    UserInfoBean selectByUsername(@Param("username") String username);
}
