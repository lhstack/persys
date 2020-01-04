package com.lhstack.controller.permission;

import com.github.pagehelper.PageInfo;
import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.aspect.permission.InitAuthority;
import com.lhstack.config.security.holder.SecurityContextHolder;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.layui.LayuiTableResult;
import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.vo.RoleAndPermissionVO;
import com.lhstack.service.permission.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private IRoleService roleService;


    @GetMapping("all")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_QUERY)")
    public ResponseEntity<LayuiTableResult<Role>> all() throws Exception {
        LayuiTableResult<Role> layuiTableResult = new LayuiTableResult<>();
        List<Role> roles = roleService.findAll();
        layuiTableResult.setCount((long) roles.size())
                .setCode(200)
                .setMsg("获取角色列表成功")
                .setData(roles);
        return ResponseEntity.ok(layuiTableResult);
    }

    @GetMapping("list")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_QUERY)")
    public ResponseEntity<LayuiTableResult<RoleAndPermissionVO>> findAll(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                                                         @RequestParam(value = "size",defaultValue = "11") Integer size){
        LayuiTableResult<RoleAndPermissionVO> layuiTableResult = new LayuiTableResult<>();
        PageInfo<RoleAndPermissionVO> rolePageInfo = roleService.findAllByIgnoreNameAndNotExistAdmin(page, size, SecurityContextHolder.getRoles().contains("ADMIN"),
                PermissionsQueryConst.userPermissions);
        layuiTableResult.setCount(rolePageInfo.getTotal())
                .setCode(200)
                .setMsg("获取角色列表成功")
                .setData(rolePageInfo.getList());
        return ResponseEntity.ok(layuiTableResult);
    }


    @PostMapping("update")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_UPDATE)")
    public ResponseEntity<LayuiResut<Integer>> update(@RequestParam("pids") Long[] pids,Role role) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(roleService.updateRoleAndRelevancePermission(pids,role)).setMsg("更新角色成功"));
    }

    @PostMapping("add")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_ADD)")
    public ResponseEntity<LayuiResut<Role>> add(@RequestParam("pids") Long[] pids,Role role) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(roleService.saveRoleAndRelevancePermission(pids,role)).setMsg("添加角色成功"));
    }


    @DeleteMapping("del/{id}")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_USER_ADD)")
    public ResponseEntity<LayuiResut<Object>> add(@PathVariable("id") Long id) throws Exception {
        roleService.deleteById(id);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null).setMsg("删除角色成功"));
    }
}
