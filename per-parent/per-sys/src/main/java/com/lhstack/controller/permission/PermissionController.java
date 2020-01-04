package com.lhstack.controller.permission;

import com.github.pagehelper.PageInfo;
import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.aspect.permission.InitAuthority;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.layui.LayuiTableResult;
import com.lhstack.entity.permission.Permission;
import com.lhstack.service.permission.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("permission")
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;


    @GetMapping("all")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_QUERY)")
    public ResponseEntity<LayuiTableResult<Permission>> allList() throws Exception {
        LayuiTableResult<Permission> layuiTableResult = new LayuiTableResult<>();
        List<Permission> list = permissionService.findAll();
        layuiTableResult.setCount((long) list.size())
                .setCode(200)
                .setMsg("获取数据成功")
                .setData(list);
        return ResponseEntity.ok(layuiTableResult);
    }

    @GetMapping("list")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_QUERY)")
    public ResponseEntity<LayuiTableResult<Permission>> findAllPermission(@RequestParam(value = "page",defaultValue = "1") Integer page,@RequestParam(value = "size",defaultValue = "11") Integer size){
        LayuiTableResult<Permission> layuiTableResult = new LayuiTableResult<>();
        PageInfo<Permission> permission = permissionService.findAllByExistAdminAndPermissions(page, size, PermissionsQueryConst.userPermissions);
        layuiTableResult.setCount(permission.getTotal())
                .setCode(200)
                .setMsg("获取数据成功")
                .setData(permission.getList());
        return ResponseEntity.ok(layuiTableResult);
    }

    @PostMapping("update")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_UPDATE)")
    public ResponseEntity<LayuiResut<Permission>> update(Permission permission) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(permissionService.update(permission.getId(),permission)).setMsg("更新权限成功"));
    }

    @PostMapping("add")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_ADD)")
    public ResponseEntity<LayuiResut<Permission>> add(Permission permission) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(permissionService.save(permission)).setMsg("更新权限成功"));
    }

    @DeleteMapping("del/{id}")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_DELETE)")
    public ResponseEntity<LayuiResut<Object>> add(@PathVariable("id") Long id) throws Exception {
        permissionService.deleteById(id);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null).setMsg("删除权限成功"));
    }
}
