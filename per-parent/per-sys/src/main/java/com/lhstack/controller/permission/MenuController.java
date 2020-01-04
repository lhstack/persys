package com.lhstack.controller.permission;

import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.aspect.permission.InitAuthority;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.permission.Menu;
import com.lhstack.entity.permission.Permission;
import com.lhstack.service.permission.IMenuService;
import com.lhstack.service.permission.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;


    @Autowired
    private IPermissionService permissionService;

    @GetMapping("list")
    @DynAuthority
    public ResponseEntity<LayuiResut<List<Menu>>> findByAllMenu(){
        return ResponseEntity.ok(LayuiResut.buildSuccess(menuService.findAllMenu()).setMsg("获取菜单成功"));
    }

    @GetMapping("preList")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_MENU_QUERY)")
    @DynAuthority
    public ResponseEntity<LayuiResut<List<Menu>>> findByPreAllMenu(){
        return ResponseEntity.ok(LayuiResut.buildSuccess(menuService.findAllMenuIsAdmin()).setMsg("查询菜单成功"));
    }

    @GetMapping("plist")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_MENU_QUERY)")
    @DynAuthority
    public ResponseEntity<LayuiResut<List<Menu>>> findByParentAllMenu(){
        return ResponseEntity.ok(LayuiResut.buildSuccess(menuService.findByParentId(0L)).setMsg("获取父级菜单成功"));
    }


    @GetMapping("pms/{mid}")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_MENU_QUERY)")
    @DynAuthority
    public ResponseEntity<LayuiResut<List<Permission>>> findByMidPermission(@PathVariable("mid") Long mid){
        return ResponseEntity.ok(LayuiResut.buildSuccess(permissionService.findByMid(mid)).setMsg("获取菜单权限成功"));
    }

    //查询忽略权限以外的所有权限
    @GetMapping("pmsList")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_MENU_QUERY)")
    @DynAuthority
    public ResponseEntity<LayuiResut<List<Permission>>> findAllIgnorePermission(){
        return ResponseEntity.ok(LayuiResut.buildSuccess(permissionService.findAllByExistAdminPermissions()).setMsg("获取菜单列表成功"));
    }

    @PostMapping("update")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_MENU_UPDATE)")
    @DynAuthority
    public ResponseEntity<LayuiResut<Menu>> updateMenu(@RequestBody Menu menu,@RequestParam(value = "pids",required = false) Long[] pids){
        Menu newM = new Menu();
        newM.setId(menu.getId())
            .setEnable(menu.getEnable())
            .setHref(menu.getHref())
            .setIcon(menu.getIcon())
            .setIsParent(menu.getIsParent())
            .setMenuName(menu.getMenuName())
            .setParentId(menu.getParentId())
            .setSortBy(menu.getSortBy());
        return ResponseEntity.ok(LayuiResut.buildSuccess(menuService.updateAndPermission(newM.getId(),newM,pids)).setMsg("更新菜单成功"));
    }

    @PostMapping("add")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_MENU_ADD)")
    @DynAuthority
    public ResponseEntity<LayuiResut<Menu>> addMenu(@RequestBody Menu menu,@RequestParam(value = "pids",required = false) Long[] pids){
        return ResponseEntity.ok(LayuiResut.buildSuccess(menuService.addAndPermission(menu,pids)).setMsg("添加菜单成功"));
    }

    @DeleteMapping("delete/{id}")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_MENU_DELETE)")
    @DynAuthority
    public ResponseEntity<LayuiResut<Integer>> deleteMenu(@PathVariable("id") Long id){
        return ResponseEntity.ok(LayuiResut.buildSuccess(menuService.deleteByIdAndPermission(id)).setMsg("删除菜单成功"));
    }
}
