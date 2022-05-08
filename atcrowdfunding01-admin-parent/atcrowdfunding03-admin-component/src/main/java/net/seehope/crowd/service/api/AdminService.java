package net.seehope.crowd.service.api;


import com.github.pagehelper.PageInfo;
import net.seehope.crowd.entity.Admin;

import java.util.List;

/**
 * @author JoinYang
 * @date 2022/3/8 21:11
 */
public interface AdminService {

    int updateByPrimaryKeySelective(Admin admin);

    // 根据用户id查询用户
    Admin selectByPrimaryKey(Integer id);

    //添加用户
    boolean saveAdmin(Admin admin);

    //获取所有用户
    List<Admin> getAllAdmin();

    //根据用户账号查询用户
    Admin getAdminByLogAcct(String loginAcct, String userPswd);

    //根据传进来的页面大小，数量，进行分页
    PageInfo<Admin> getPageInfo(String keyWord, Integer pageNum, Integer pageSize);

    //根据用户的id进行用户删除
    int deleteByPrimaryKey(Integer id);
}
