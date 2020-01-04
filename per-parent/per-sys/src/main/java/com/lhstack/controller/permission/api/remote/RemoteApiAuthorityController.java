package com.lhstack.controller.permission.api.remote;

import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.aspect.permission.InitAuthority;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.layui.LayuiTableResult;
import com.lhstack.entity.permission.Permission;
import com.lhstack.entity.permission.Role;
import com.lhstack.entity.permission.api.remote.RemoteApiInfo;
import com.lhstack.service.permission.impl.api.remote.RemoteApiInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: RemoteApiAuthorityController
 * Description:
 * date: 2019/12/25 19:40
 *
 * @author lhstack
 * @since 1.8
 */
@RestController
@RequestMapping("remote/api")
public class RemoteApiAuthorityController {

    @Autowired
    private RemoteApiInfoServiceImpl remoteApiInfoService;

    /**
    * Description: 接收客户端传送过来的接口信息
    * date: 2020/1/1 14:03
    * @author lhstack
    * @version 0.0.1
    * @since 1.8
    */
    @PostMapping("receive")
    public ResponseEntity<Boolean> receive(@RequestParam("c") String c) throws Exception {
        remoteApiInfoService.saveClientApiInfo(c);
        return ResponseEntity.ok(true);
    }

    /**
    * Description: 客户端远程校验权限的接口信息
    * date: 2020/1/1 14:03
    * @author lhstack
    * @version 0.0.1
    * @since 1.8
    */
    @PostMapping("check")
    public ResponseEntity<LayuiResut<Boolean>> check(@RequestParam("c") String c) throws Exception {
        remoteApiInfoService.checkAuthority(c);
        return ResponseEntity.ok(LayuiResut.buildSuccess(true));
    }

    @GetMapping("listNamespaces")
    @DynAuthority
    @InitAuthority("hasRole(ADMIN)")
    public ResponseEntity<LayuiResut<List<String>>> listNamespaces(){
        return ResponseEntity.ok(LayuiResut.buildSuccess(remoteApiInfoService.findAllNamespaces()));
    }


    @DeleteMapping("del/{namespace}")
    @DynAuthority
    @InitAuthority("hasRole(ADMIN")
    public ResponseEntity<LayuiResut<Boolean>> delNamespace(@PathVariable("namespace") String namespace){
        remoteApiInfoService.deleteByNamespace(namespace);
        return ResponseEntity.ok(LayuiResut.buildSuccess(true));
    }

    @PostMapping("list/{page}/{size}")
    @DynAuthority
    @InitAuthority("hasRole(ADMIN)")
    public ResponseEntity<LayuiTableResult<RemoteApiInfo>> list(@RequestBody(required = false) RemoteApiInfo remoteApiInfo,
                                                                @PathVariable("page") Integer page,
                                                                @PathVariable("size") Integer size){
        Page<RemoteApiInfo> remoteApiInfoPage = remoteApiInfoService.findAllByExample(remoteApiInfo, page, size);
        LayuiTableResult<RemoteApiInfo> layuiTableResult = new LayuiTableResult<>();
        layuiTableResult.setCount(remoteApiInfoPage.getTotalElements())
                .setCode(200)
                .setMsg("查询成功")
                .setData(remoteApiInfoPage.getContent());
        return ResponseEntity.ok(layuiTableResult);
    }

    @PutMapping("updateApiInfo/{authorityType}/{apiId}")
    @DynAuthority
    @InitAuthority("hasRole(ADMIN)")
    public ResponseEntity<LayuiResut<Boolean>> updateAuthorityType(@PathVariable("authorityType") Integer type,
                                                                   @PathVariable("apiId") Long id,
                                                                   @RequestParam("description") String description) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(remoteApiInfoService.updateAuthorityType(type,id,description) > 0).setMsg("更新鉴权类型成功"));
    }


    @PostMapping("roles")
    @DynAuthority
    @InitAuthority("hasRole(ADMIN)")
    public ResponseEntity<LayuiResut<Boolean>> updateRoles(@RequestParam(value = "ids",required = true) Long[] roleIds,
                                                           @RequestParam(value = "apiId",required = true)Long apiId){
        remoteApiInfoService.updateRoles(roleIds,apiId);
        return ResponseEntity.ok(LayuiResut.buildSuccess(true));
    }

    @GetMapping("roles")
    @DynAuthority
    @InitAuthority("hasRole(ADMIN)")
    public ResponseEntity<LayuiTableResult<Role>> listRoles(@RequestParam(value = "id",required = true) Long apiInfoId){
        List<Role> roles = remoteApiInfoService.listRoles(apiInfoId);
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
        remoteApiInfoService.updatePermission(permissionIds,apiId);
        return ResponseEntity.ok(LayuiResut.buildSuccess(true));
    }


    @GetMapping("permissions")
    @DynAuthority
    @InitAuthority("hasRole(ADMIN)")
    public ResponseEntity<LayuiTableResult<Permission>> listPermission(@RequestParam(value = "id",required = true) Long apiInfoId){
        List<Permission> permissions = remoteApiInfoService.listPermission(apiInfoId);
        LayuiTableResult<Permission> layuiTableResult = new LayuiTableResult<>();
        layuiTableResult.setCount((long) permissions.size())
                .setCode(200)
                .setMsg("获取接口权限成功")
                .setData(permissions);
        return ResponseEntity.ok(layuiTableResult);
    }
}
