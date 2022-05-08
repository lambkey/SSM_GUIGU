package net.seehope.crowd.service.api;

import com.github.pagehelper.PageInfo;
import net.seehope.crowd.entity.Role;

import java.util.List;

/**
 * @author JoinYang
 * @date 2022/4/12 19:22
 */
public interface RoleService {

    // 传入数组，根据数组里面的id进行角色删除
    void removeRole(List<Integer> role);

    // 根据主键id更新角色
    int updateByPrimaryKey(Role record);

    // 添加角色
    int insert(Role record);

    // 根据传进来的页面大小，数量，进行分页
    PageInfo<Role> getPageInfo(String keyWord, Integer pageNum, Integer pageSize);

    // 根据adminId获取已分配的角色
    List <Role> getAssignedRole(Integer adminId);

    // 根据adminId获取未分配的角色
    List<Role> getUnAssignedRole(Integer adminId);

    void deleteOldRoleToSaveNewRoleForAdmin(Integer adminId, List<Integer> integerList);
}
