package com.imooc.activiti7_workflow.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Mapper
@Component
public interface ActivitiMapper {

    @Insert("<script> insert into formdata (PROC_DEF_ID_,PROC_INST_ID_,FORM_KEY_,Control_ID_,Control_VALUE_)" +
            " VALUES" +
            "<foreach collection=\"maps\" item=\"formData\" index=\"index\" separator=\",\">" +
            "(#{formData.PROC_DEF_ID_,jdbcType=VARCHAR},#{formData.PROC_INST_ID_,jdbcType=VARCHAR}," +
            "#{formData.FORM_KEY_,jdbcType=VARCHAR},#{formData.Control_ID_,jdbcType=VARCHAR}," +
            "#{formData.Control_VALUE_,jdbcType=VARCHAR}" +
            ")</foreach> </script>")
    int insertFormData(@Param("maps") List<HashMap<String, Object>> maps);
}
