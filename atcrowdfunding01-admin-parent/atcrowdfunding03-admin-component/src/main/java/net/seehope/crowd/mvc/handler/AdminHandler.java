package net.seehope.crowd.mvc.handler;

import com.github.pagehelper.PageInfo;
import net.seehope.crowd.constant.CrowdConstant;
import net.seehope.crowd.entity.Admin;
import net.seehope.crowd.entity.AdminVo;
import net.seehope.crowd.service.api.AdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author JoinYang
 * @date 2022/3/19 15:05
 */
@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;





    // 更新用户，根据前端用户修改的信息执行数据库更新操作
    @RequestMapping(value = "/admin/to/updateAdmin.html")
    public String doExeUpdate(Admin admin,ModelMap modelMap){
        // 根据传进来封装的对象进行对应的更新,id允许为空
        int updateRecord=adminService.updateByPrimaryKeySelective(admin);

        if (updateRecord==1){
            modelMap.addAttribute("SuccessTip","更新用户成功");
        }

        return "system-success";
    }

    // 更新用户，根据用户id显示用户信息
    @RequestMapping(value = "/admin/update/{adminId}.html")
    public String updateAdmin(@PathVariable("adminId") Integer adminId,ModelMap modelMap){

        Admin admin = adminService.selectByPrimaryKey(adminId);

        modelMap.addAttribute("Admin",admin);

        return "admin-update";
    }

    // 添加用户
    @RequestMapping(value = "/admin/to/addAdmin.html",method = RequestMethod.POST)
    public String addAdmin( @Valid AdminVo adminVo,ModelMap modelMap){

        boolean state=false;

        Admin admin =new Admin();

        // 为了安全起见，将前端传进来的json对象封装再转换为真实对象存储
        BeanUtils.copyProperties(adminVo,admin);

        state=adminService.saveAdmin(admin);

        if (state){
            modelMap.addAttribute("SuccessTip","添加用户成功");
        }

        return "system-success";
    }

    // 管理员用户单点删除
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public  String removeAdmin(@PathVariable ("adminId") Integer adminId,
                               @PathVariable("pageNum") Integer pageNum,
                               @PathVariable("keyword") String keyword
    ){
        adminService.deleteByPrimaryKey(adminId);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    // 用户维护功能,关键词查询/分页查询
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,

            // pageNum默认使用1，去到第一页
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,

            // pageSize,每一页显示五条数据
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            ModelMap modelMap

    ) {
        // 调用Service方法获取PageInfo对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);

        // 将pageInfo对象传到管理员进行用户维护的页面，存入模型
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }


    // 2.退出系统
    @RequestMapping("admin/do/lagout.html")
    public String logOut(HttpSession session) {

        //强制session失效
        session.invalidate();

        return "redirect:/admin/to/login/page.html";
    }

    // 1.用户登录
    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String loginAcct,
                          @RequestParam("userPswd") String userPswd,
                          HttpSession session, HttpServletRequest request
    ) {
        //调用service方法进行校验检查
        //这个方法能够返回admin对象说明登录成功，如果账号密码不正确则会抛出异常
        Admin admin = adminService.getAdminByLogAcct(loginAcct, userPswd);

        //登录成功后返回的admin对象存入Session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN_SESSION, admin);

        //考虑到浏览器刷新
        return "redirect:/admin/to/main/page.html";
    }
}
