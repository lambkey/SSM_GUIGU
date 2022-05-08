package net.seehope.crowd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.seehope.crowd.constant.CrowdConstant;
import net.seehope.crowd.entity.Admin;
import net.seehope.crowd.entity.AdminExample;
import net.seehope.crowd.exception.LoginAcctExist;
import net.seehope.crowd.exception.LoginAcctExistToUpdate;
import net.seehope.crowd.exception.LoginFailedException;
import net.seehope.crowd.mapper.AdminMapper;
import net.seehope.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author JoinYang
 * @date 2022/3/8 21:11
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    //数据加密
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public int updateByPrimaryKeySelective(Admin admin){

        // 判断，若果不存在此账号就执行管理用户的资料更新
        if (adminMapper.getAdminByLogAcct(admin.getLogin_Acct())!=null){
            //抛出的异常将通过CrowdExceptionResolver类进行异常的显示处理
            throw new LoginAcctExistToUpdate(CrowdConstant.ADMIN_ALREADY_IN_TO_UPDATE);
        }
        int updateNum = adminMapper.updateByPrimaryKeySelective(admin);
        return updateNum;
    }

    @Override
    public Admin selectByPrimaryKey(Integer id) {
        Admin admin = adminMapper.selectByPrimaryKey(id);
        return admin;
    }

    @Override
    public boolean saveAdmin(Admin admin) {

        if (adminMapper.getAdminByLogAcct(admin.getLogin_Acct())!=null){
            //抛出的异常将通过CrowdExceptionResolver类进行异常的显示处理
            throw new LoginAcctExist(CrowdConstant.ADMIN_ALREADY_IN);
        }
        //传进来的密码加密处理
        String md5Pswd= passwordEncoder.encode(admin.getUser_Pswd());
        admin.setUser_Pswd(md5Pswd);
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String createTime = sdf.format(new Date(System.currentTimeMillis()));
        admin.setCreateTime(createTime);
        adminMapper.insert(admin);
        return true;
    }


    public List<Admin> getAllAdmin(){
        //查询所有的
      return   adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLogAcct(String loginAcct, String userPswd) {

        // 1、根据账号查询Admin对象
        Admin admin =adminMapper.getAdminByLogAcct(loginAcct);
        // 2、判断Admin对象是否为null

        if (admin==null){
            // 3、如果为空则抛出异常
            throw new LoginFailedException(CrowdConstant.MASSAGE_LOGIN_FAILED);
        }
        // 4、如果Admin对象不为null则将数据密码从Admin对象中取出
        String userPswd_DB =   admin.getUser_Pswd();

        // 6、对密码进行比较
        if (!passwordEncoder.matches(userPswd,userPswd_DB)){
            // 7、如果比较不一致则抛出异常
            throw new LoginFailedException(CrowdConstant.MASSAGE_LOGIN_FAILED);
        }
        // 8、如果一致则返回admin对象
        return admin;
    }


    @Override
    public PageInfo<Admin> getPageInfo(String keyWord, Integer pageNum, Integer pageSize) {
        // 1、调用PageHelper的静态方法开启分页功能
        //这里充分体现了PageHandler的“非侵入式”设计;原本要做的查询不必有任何修改
        //要分页就使用，不用分页就不用,没有任何的侵入

        PageHelper.startPage(pageNum,pageSize);

        //2、执行查询
        List<Admin> adminList = adminMapper.selectAdminByKeyword(keyWord);

        //3、封装到PageInfo对象中
        return new PageInfo<>(adminList);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
       return adminMapper.deleteByPrimaryKey(id);
    }
}
