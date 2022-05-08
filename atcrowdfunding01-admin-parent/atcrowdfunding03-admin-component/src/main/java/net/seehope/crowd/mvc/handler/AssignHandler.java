package net.seehope.crowd.mvc.handler;

import net.seehope.crowd.entity.Auth;
import net.seehope.crowd.entity.Role;
import net.seehope.crowd.service.api.AdminService;
import net.seehope.crowd.service.api.AuthService;
import net.seehope.crowd.service.api.RoleService;
import net.seehope.crowd.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author JoinYang
 * @date 2022/4/24 12:22
 */
@Controller
public class AssignHandler {
    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    // 给角色分配权限
    @ResponseBody
    @RequestMapping("/assign/do/save/role/auth/relationship.json")
    public JSONResult<String> saveRoleAuthRelationship(
            // 用一个map接收前端发来的数据
            @RequestBody Map<String,List<Integer>> map
    ){
        // 保存更改后的Role与Auth关系
        authService.saveRoleAuthRelationship(map);

        return JSONResult.successWithoutData();

    }

    // 获得被勾选的auth信息，提供给前端回显
    @ResponseBody
    @RequestMapping("/assign/get/checked/auth/id.json")
    public JSONResult<List<Integer>> getAuthByRoleId(Integer roleId){
        List<Integer> authIdList = authService.getAuthByRoleId(roleId);
        return JSONResult.successNeedData(authIdList);
    }

    // 获取auth实体的整体信息
    @ResponseBody
    @RequestMapping("/assign/get/tree.json")
    public JSONResult<List<Auth>> getAuthTree(){
        List<Auth> authList = authService.getAuthList();
        return JSONResult.successNeedData(authList);
    }

    // 先删除已分配的角色，再执行保存要分配的角色
    @RequestMapping("/assign/do/role/assign.html")
    public String doAssignRole(
                    @RequestParam("pageNum") Integer pageNum,
                    @RequestParam("keyword") String keyword,
                    @RequestParam("adminId") Integer adminId,

                    // required=false,设置请求的roleIdList的值可以为空
                    @RequestParam(value = "roleIdList",required = false) List<Integer> integerList
    ){

        roleService.deleteOldRoleToSaveNewRoleForAdmin(adminId,integerList);

        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
}



    // 根据管理员的AdminId显示它所分配的角色和未分配的角色
    @RequestMapping("/assign/to/assign/role/page.html")
    public String toAssignRolePage(@RequestParam("adminId") Integer adminId,
                                    ModelMap modelMap
    ){
        // 1、查询已分配的角色
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);

        // 2、查询未分配的角色
        List<Role> unAssignedRoleList = roleService.getUnAssignedRole(adminId);

        // 3、存入模型
        modelMap.addAttribute("assignedRoleList",assignedRoleList);
        modelMap.addAttribute("unAssignedRoleList",unAssignedRoleList);
        return "assign-role";
    }
}
