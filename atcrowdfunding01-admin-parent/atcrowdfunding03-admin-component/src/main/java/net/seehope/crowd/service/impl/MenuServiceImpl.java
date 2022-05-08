package net.seehope.crowd.service.impl;

import net.seehope.crowd.entity.Menu;
import net.seehope.crowd.entity.MenuExample;
import net.seehope.crowd.mapper.MenuMapper;
import net.seehope.crowd.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author JoinYang
 * @date 2022/4/19 15:36
 */
@Controller
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;


    // 添加菜单
    @Override
    public int insert(Menu record) {
        return menuMapper.insert(record);
    }

    // 更新菜单
    @Override
    public void updateMenu(Menu menu) {
        // 由于pid没有传入，一定要使用有选择更新，保证“pid”字段不会被置空
        menuMapper.updateByPrimaryKeySelective(menu);
    }

    // 删除菜单
    @Override
    public void removeMenu(Menu menu) {
        Integer id = menu.getId();
        menuMapper.deleteByPrimaryKey(id);
    }

    // 获取所有菜单
    @Override
    public List<Menu> getAll() {
        return menuMapper.selectByExample(new MenuExample());
    }
}
