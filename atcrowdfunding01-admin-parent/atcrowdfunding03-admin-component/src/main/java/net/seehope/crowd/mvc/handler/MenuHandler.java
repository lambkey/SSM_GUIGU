package net.seehope.crowd.mvc.handler;

import net.seehope.crowd.entity.Menu;
import net.seehope.crowd.service.api.MenuService;
import net.seehope.crowd.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JoinYang
 * @date 2022/4/19 15:36
 */
@RestController
public class MenuHandler {

    @Autowired
    private MenuService menuService;


    @RequestMapping("/menu/remove.json")
    public JSONResult<String> removeMenu(Menu menu){

        menuService.removeMenu(menu);

        return JSONResult.successWithoutData();
    }



    @RequestMapping("/menu/update.json")
    public JSONResult<String> updateMenu(Menu menu){
        menuService.updateMenu(menu);
        return JSONResult.successWithoutData();
    }


    @RequestMapping("/menu/save.json")
    public JSONResult<String> saveMenu(Menu menu){
        Integer menuPid =(Integer)menu.getPid();
        menu.setPid(menuPid);
        menuService.insert(menu);
        return JSONResult.successWithoutData();
    }




    @RequestMapping("/menu/do/get.json")
    public JSONResult<Menu> getWholeTree(){

        // 通过Service层方法得到全部Menu对象的List
        List<Menu> menuList = menuService.getAll();

        // 声明一个Menu对象root，用来存放找到的根节点
        Menu root = null;

        // 使用map表示每一个菜单与id的对应关系
        Map<Integer,Menu> menuMap =new HashMap<>();

        // 将菜单id与菜单对象以k-v对模式存入map
        for (Menu menu: menuList) {
            menuMap.put(menu.getId(),menu);
        }
        for (Menu menu: menuList) {

            Integer pid = menu.getPid();

            if (pid == null){

                // pid为null表示该menu是根节点
                root = menu;

                // 如果当前节点时根节点，那么肯定没有父节点，停止循环继续执行下一个循环
                continue;
            }

            // 如果pid不为null，说明当前节点有父节点，通过当前遍历的menu的pid得到其父节点
            Menu father = menuMap.get(pid);
            father.getChildren().add(menu);
        }

        // 将根节点作为data返回（返回根节点也就是返回了整棵树）
        return JSONResult.successNeedData(root);
    }
}
