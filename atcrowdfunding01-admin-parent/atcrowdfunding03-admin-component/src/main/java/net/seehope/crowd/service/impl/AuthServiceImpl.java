package net.seehope.crowd.service.impl;

import net.seehope.crowd.entity.Auth;
import net.seehope.crowd.entity.AuthExample;
import net.seehope.crowd.mapper.AuthMapper;
import net.seehope.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author JoinYang
 * @date 2022/4/27 9:27
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthMapper authMapper;

    // 获取所有权限节点，获取auth实体的整体信息
    @Override
    public List<Auth> getAuthList() {
        return authMapper.selectByExample(new AuthExample());
    }

    // 根据角色id获取它所拥有的权限
    @Override
    public List<Integer> getAuthByRoleId(Integer roleId) {
        return authMapper.getAuthByRoleId(roleId);
    }

    // 接收前端的角色的id和所做出的分配的权限执行保存
    @Override
    public void saveRoleAuthRelationship(Map<String, List<Integer>> map) {
        // 通过键值获取roleId、authList
        List<Integer> roleIdList = map.get("roleId");
        Integer roleId = roleIdList.get(0);
        List<Integer> authList = map.get("authIdList");
        // 1、先根据roleId 删除之前存在的权限
        authMapper.removeOldRoleAuth(roleId);
        // 2、根据roleId、和authList在对应的表中插入数据
        if (authList!=null&&authList.size()>0){
            authMapper.addNewRoleAuth(roleId,authList);
        }

    }
}
