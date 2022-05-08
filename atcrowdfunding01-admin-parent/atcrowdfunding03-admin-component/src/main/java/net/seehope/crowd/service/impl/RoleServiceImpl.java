package net.seehope.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.seehope.crowd.entity.Role;
import net.seehope.crowd.entity.RoleExample;
import net.seehope.crowd.mapper.RoleMapper;
import net.seehope.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author JoinYang
 * @date 2022/4/12 19:23
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    // 批量删除角色
    @Override
    public void removeRole(List<Integer> role) {
        RoleExample roleExample = new RoleExample();
       RoleExample.Criteria criteria = roleExample.createCriteria();
       // delete from t_role where id in (5,8,12)
       criteria.andIdIn(role);
        roleMapper.deleteByExample(roleExample);
    }

    // 更新角色
    @Override
    public int updateByPrimaryKey(Role record) {
        return roleMapper.updateByPrimaryKey(record);
    }

    // 添加角色
    @Override
    public int insert(Role record) {
        return roleMapper.insert(record);
    }

    // 分页显示角色
    @Override
    public PageInfo<Role> getPageInfo(String keyWord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List <Role> list = roleMapper.selectRoleByKeyword(keyWord);
        return new  PageInfo<>(list);
    }

    // 获取已分配的角色(assign)
    @Override
    public List<Role> getAssignedRole(Integer adminId) {

        return roleMapper.selectAssignedRole(adminId);
    }

    // 获取未分配的角色(assign)
    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {
        return roleMapper.selectUnAssignedRole(adminId);
    }

    // 先删除旧的角色在保存新的角色(assign)
    @Override
    public void deleteOldRoleToSaveNewRoleForAdmin(Integer adminId, List<Integer> integerList) {
        // 1、先根据Admin的id删除旧的角色
        roleMapper.deleteOldRoleForAdmin(adminId);
        if (integerList!=null){

            // 2、对新填写的新的角色执行保存
            roleMapper.saveNewRoleForAdmin(adminId,integerList);
        }

    }

}
