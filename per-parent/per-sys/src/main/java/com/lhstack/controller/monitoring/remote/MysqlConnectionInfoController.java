package com.lhstack.controller.monitoring.remote;

import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.config.security.holder.SecurityContextHolder;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.monitoring.MysqlConnectionInfo;
import com.lhstack.entity.monitoring.RedisConnectionInfo;
import com.lhstack.service.monitoring.remote.IMysqlConnectionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("remote/monitoring/mysql")
public class MysqlConnectionInfoController {


    @Autowired
    private IMysqlConnectionInfoService mysqlConnectionInfoService;

    @PostMapping("save")
    @DynAuthority
    public ResponseEntity<LayuiResut<MysqlConnectionInfo>> save(@RequestBody MysqlConnectionInfo mysqlConnectionInfo) throws Exception {
        String username = SecurityContextHolder.getUsername();
        mysqlConnectionInfo.setUsername(username)
                .setCreateTime(new Date());
        return ResponseEntity.ok(LayuiResut.buildSuccess(mysqlConnectionInfoService.save(mysqlConnectionInfo)));
    }

    @GetMapping("find/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<MysqlConnectionInfo>> findById(@PathVariable("id") String id) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(mysqlConnectionInfoService.findById(id)));
    }

    @PutMapping("update/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<MysqlConnectionInfo>> update(@PathVariable("id") String id, @RequestBody MysqlConnectionInfo mysqlConnectionInfo) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(mysqlConnectionInfoService.update(id, mysqlConnectionInfo)));
    }

    @DeleteMapping("del/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<Object>> del(@PathVariable("id") String id) throws Exception {
        mysqlConnectionInfoService.deleteById(id);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null));
    }

    @GetMapping("list")
    @DynAuthority
    public ResponseEntity<LayuiResut<List<MysqlConnectionInfo>>> list() {
        String username = SecurityContextHolder.getUsername();
        return ResponseEntity.ok(LayuiResut.buildSuccess(mysqlConnectionInfoService.findAll(username)));
    }

    @GetMapping("state/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<Map<String, Object>>> state(@PathVariable("id") String id) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(mysqlConnectionInfoService.getState(id)));
    }
}
