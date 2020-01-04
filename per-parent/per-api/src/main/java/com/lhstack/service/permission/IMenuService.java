package com.lhstack.service.permission;

import com.lhstack.entity.permission.Menu;
import com.lhstack.service.IBaseService;

import java.util.List;

public interface IMenuService extends IBaseService<Long, Menu> {

    List<Menu> findAllMenu();
    ;
    List<Menu> findAllByChildMenu(Long parentId);

    List<Menu> findByParentId(Long parentId);

    List<Menu> findAllMenuIsAdmin();

    //更新菜单以及关联权限
    Menu updateAndPermission(Long id, Menu newM, Long[] pids);

    //添加菜单以及关联权限
    Menu addAndPermission(Menu menu, Long[] pids);

    //删除菜单以及关联权限
    Integer deleteByIdAndPermission(Long id);
}
