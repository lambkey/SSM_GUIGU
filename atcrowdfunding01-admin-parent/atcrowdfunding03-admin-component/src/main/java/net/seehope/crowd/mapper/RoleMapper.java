package net.seehope.crowd.mapper;

import net.seehope.crowd.entity.Role;
import net.seehope.crowd.entity.RoleExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper {
    int countByExample(RoleExample example);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> selectRoleByKeyword(String keyword);

    List <Role> selectAssignedRole(Integer adminId);

    List<Role> selectUnAssignedRole(Integer adminId);



    @Select("SELECT * FROM t_role where name =#{name}")
    Role selectRoleByName(@Param("name") String name);

    void deleteOldRoleForAdmin(Integer adminId);

    void saveNewRoleForAdmin(@Param("adminId") Integer adminId, @Param("roleIdList") List<Integer> roleIdList);
}