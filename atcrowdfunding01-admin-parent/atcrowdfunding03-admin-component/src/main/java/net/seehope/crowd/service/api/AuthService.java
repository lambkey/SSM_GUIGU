package net.seehope.crowd.service.api;

import net.seehope.crowd.entity.Auth;

import java.util.List;
import java.util.Map;

/**
 * @author JoinYang
 * @date 2022/4/27 9:27
 */
public interface AuthService {
    List<Auth> getAuthList();

    List<Integer> getAuthByRoleId(Integer roleId);

    void saveRoleAuthRelationship(Map<String, List<Integer>> map);
}
