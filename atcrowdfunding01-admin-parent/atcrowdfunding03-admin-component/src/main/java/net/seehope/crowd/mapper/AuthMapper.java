package net.seehope.crowd.mapper;

import net.seehope.crowd.entity.Auth;
import net.seehope.crowd.entity.AuthExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthMapper {
    int countByExample(AuthExample example);

    int deleteByExample(AuthExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Auth record);

    int insertSelective(Auth record);

    List<Auth> selectByExample(AuthExample example);

    Auth selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByExample(@Param("record") Auth record, @Param("example") AuthExample example);

    int updateByPrimaryKeySelective(Auth record);

    int updateByPrimaryKey(Auth record);

    List<Integer> getAuthByRoleId(Integer roleId);

    void removeOldRoleAuth(Integer roleId);

    void addNewRoleAuth(@Param("roleId") Integer roleId, @Param("authList") List<Integer> authList);
}