package net.seehope.crowd.mapper;


import net.seehope.crowd.entity.Admin;
import net.seehope.crowd.entity.AdminExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
public interface AdminMapper {

    int countByExample(AdminExample example);


    int deleteByExample(AdminExample example);


    int deleteByPrimaryKey(Integer id);


    int insert(Admin record);


    int insertSelective(Admin record);


    List<Admin> selectByExample(AdminExample example);


    Admin selectByPrimaryKey(Integer id);



    int updateByExampleSelective(@Param("record") Admin record, @Param("example") AdminExample example);


    int updateByExample(@Param("record") Admin record, @Param("example") AdminExample example);



    int updateByPrimaryKeySelective(Admin record);


    int updateByPrimaryKey(Admin record);

    List<Admin> selectAdminByKeyword(String keyword);


    @Select("SELECT * FROM t_admin where login_acct=#{login_acct}")
    Admin getAdminByLogAcct(@Param("login_acct") String login_acct);


}