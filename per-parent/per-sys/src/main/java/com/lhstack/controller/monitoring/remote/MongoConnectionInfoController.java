package com.lhstack.controller.monitoring.remote;

import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.config.security.holder.SecurityContextHolder;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.monitoring.MongoConnectionInfo;
import com.lhstack.service.monitoring.remote.IMongoConnectionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("remote/monitoring/mongo")
public class MongoConnectionInfoController {

    @Autowired
    private IMongoConnectionInfoService mongoConnectionInfoService;


    @PostMapping("save")
    @DynAuthority
    public ResponseEntity<LayuiResut<MongoConnectionInfo>> save(@RequestBody MongoConnectionInfo mongoConnectionInfo) throws Exception {
        String username = SecurityContextHolder.getUsername();
        mongoConnectionInfo.setUsername(username)
                .setCreateTime(new Date());
        return ResponseEntity.ok(LayuiResut.buildSuccess(mongoConnectionInfoService.save(mongoConnectionInfo)));
    }

    @PutMapping("update/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<MongoConnectionInfo>> update(@PathVariable("id") String id,@RequestBody MongoConnectionInfo mongoConnectionInfo) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(mongoConnectionInfoService.update(id,mongoConnectionInfo)));
    }

    @DeleteMapping("del/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<Object>> del(@PathVariable("id") String id) throws Exception {
        mongoConnectionInfoService.deleteById(id);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null));
    }

    @GetMapping("find/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<MongoConnectionInfo>> findById(@PathVariable("id") String id) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(mongoConnectionInfoService.findById(id)));
    }

    @GetMapping("list")
    @DynAuthority
    public ResponseEntity<LayuiResut<List<MongoConnectionInfo>>> list(){
        String username = SecurityContextHolder.getUsername();
        return ResponseEntity.ok(LayuiResut.buildSuccess(mongoConnectionInfoService.findAll(username)));
    }

    @GetMapping("state/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<Map<String,Object>>> state(@PathVariable("id") String id) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(mongoConnectionInfoService.getState(id)));
    }
}
