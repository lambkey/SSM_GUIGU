package net.seehope.crowd.mvc.handler;

import com.github.pagehelper.PageInfo;
import net.seehope.crowd.entity.Role;
import net.seehope.crowd.service.api.RoleService;
import net.seehope.crowd.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author JoinYang
 * @date 2022/4/12 19:21
 */
@Controller
public class RoleHandler {
    @Autowired
    private RoleService roleService;


    @ResponseBody
    @RequestMapping(("/role/delete/by/role/id/array.json"))
    public JSONResult<String> removeByRoleIdArray(@RequestBody List<Integer> roleIdList){
        roleService.removeRole(roleIdList);
        return JSONResult.successWithoutData();
    }


    @ResponseBody
    @RequestMapping("/role/update.json")
    public JSONResult<String> updateRole(@RequestBody Role role){

        roleService.updateByPrimaryKey(role);

        return JSONResult.successWithoutData();
    }
    @ResponseBody
    @RequestMapping("/role/save.json")
    public JSONResult<String> saveRole(Role role){
        roleService.insert(role);
        return JSONResult.successWithoutData();
    }


    @ResponseBody
    @RequestMapping("/role/get/page/info.json")
    public JSONResult<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize

    ){
        // 调用Service方法获取分页数据
        PageInfo<Role> pageInfo = roleService.getPageInfo(keyword, pageNum, pageSize);

        // 封装到JSONResult对象中返回（如果上面操作抛出异常，交给异常映射机制处理）
        return JSONResult.successNeedData(pageInfo);
    }
}
