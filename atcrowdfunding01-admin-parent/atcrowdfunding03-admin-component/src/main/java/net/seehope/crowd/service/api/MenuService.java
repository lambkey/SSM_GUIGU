package net.seehope.crowd.service.api;

import net.seehope.crowd.entity.Menu;

import java.util.List;

/**
 * @author JoinYang
 * @date 2022/4/19 15:36
 */
public interface MenuService {
    int insert(Menu record);
    List<Menu> getAll();

    void updateMenu(Menu menu);

    void removeMenu(Menu menu);
}
