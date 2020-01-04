package com.lhstack.controller.permission.api;

import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.aspect.permission.InitAuthority;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.layui.LayuiTableResult;
import com.lhstack.entity.permission.Permission;
import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.api.ApiInfo;
import com.lhstack.service.permission.api.IApiInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("authority")
public class ApiAuthorityController {

    @Autowired
    private IApiInfoService apiInfoService;


    @PostMapping("authorityType/{authorityType}/{apiId}")
    @DynAuthority
    @InitAuthority("hasRole(ADMIN)")
    public ResponseEntity<LayuiResut<Boolean>> updateAuthorityType(@PathVariable("authorityType") Integer type,
                                                                   @PathVariable("apiId") Long id,
                                                                   @RequestParam("description") String description) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(apiInfoService.updateAuthorityType(type,id,description) > 0).setMsg("更新鉴权类型成功"));
    }

    @PostMapping("list")
    @DynAuthority
    @InitAuthority("hasRole(ADMIN)")
    public ResponseEntity<LayuiTableResult<ApiInfo>> list(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "size",defaultValue = "10") Integer size,
            @RequestBody ApiInfo apiInfo) throws Exception {
        LayuiTableResult<ApiInfo> layuiTableResult = new LayuiTableResult<>();
        Page<ApiInfo> infoPage = apiInfoService.findByExample(page, size,apiInfo);
        layuiTableResult.setCount(infoPage.getTotalElements())
                .setCode(200)
                .setMsg("查询成功")
                .setData(infoPage.getContent());
        return ResponseEntity.ok(layuiTableResult);
    }


    @PostMapping("roles")
    @DynAuthority
    @InitAuthority("hasRole(ADMIN)")
    public ResponseEntity<LayuiResut<Boolean>> updateRoles(@RequestParam(value = "ids",required = true) Long[] roleIds,
                                                              @RequestParam(value = "apiId",required = true)Long apiId){
        apiInfoService.updateRoles(roleIds,apiId);
        return ResponseEntity.ok(LayuiResut.buildSuccess(true));
    }

    @GetMapping("roles")
    @DynAuthority
    @InitAuthority("hasRole(ADMIN)")
    public ResponseEntity<LayuiTableResult<Role>> listRoles(@RequestParam(value = "id",required = true) Long apiInfoId){
        List<Role> roles = apiInfoService.listRoles(apiInfoId);
        LayuiTableResult<Role> layuiTableResult = new LayuiTableResult<>();
        layuiTableResult.setCount((long) roles.size())
                .setCode(200)
                .setMsg("获取接口角色成功")
                .setData(roles);
        return ResponseEntity.ok(layuiTableResult);
    }


    @PostMapping("permissions")
    @DynAuthority
    @InitAuthority("hasRole(ADMIN)")
    public ResponseEntity<LayuiResut<Boolean>> updatePermission(@RequestParam(value = "ids",required = true) Long[] permissionIds,
                                                                         @RequestParam(value = "apiId",required = true)Long apiId){
        apiInfoService.updatePermission(permissionIds,apiId);
        return ResponseEntity.ok(LayuiResut.buildSuccess(true));
    }


    @GetMapping("permissions")
    @DynAuthority
    @InitAuthority("hasRole(ADMIN)")
    public ResponseEntity<LayuiTableResult<Permission>> listPermission(@RequestParam(value = "id",required = true) Long apiInfoId){
        List<Permission> permissions = apiInfoService.listPermission(apiInfoId);
        LayuiTableResult<Permission> layuiTableResult = new LayuiTableResult<>();
        layuiTableResult.setCount((long) permissions.size())
                .setCode(200)
                .setMsg("获取接口权限成功")
                .setData(permissions);
        return ResponseEntity.ok(layuiTableResult);
    }
}
