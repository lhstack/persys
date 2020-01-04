package com.lhstack.controller.monitoring.remote;

import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.config.security.holder.SecurityContextHolder;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.monitoring.RedisConnectionInfo;
import com.lhstack.service.monitoring.remote.IRedisConnectionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("remote/monitoring/redis")
public class RedisConnectionInfoController {


    @Autowired
    private IRedisConnectionInfoService redisConnectionInfoService;

    @PostMapping("save")
    @DynAuthority
    public ResponseEntity<LayuiResut<RedisConnectionInfo>> save(@RequestBody RedisConnectionInfo redisConnectionInfo) throws Exception {
        String username = SecurityContextHolder.getUsername();
        redisConnectionInfo.setUsername(username)
                .setCreateTime(new Date());
        return ResponseEntity.ok(LayuiResut.buildSuccess(redisConnectionInfoService.save(redisConnectionInfo)));
    }

    @PutMapping("update/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<RedisConnectionInfo>> update(@PathVariable("id") String id,@RequestBody RedisConnectionInfo redisConnectionInfo) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(redisConnectionInfoService.update(id,redisConnectionInfo)));
    }

    @GetMapping("find/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<RedisConnectionInfo>> findById(@PathVariable("id") String id) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(redisConnectionInfoService.findById(id)));
    }

    @DeleteMapping("del/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<Object>> del(@PathVariable("id") String id) throws Exception {
        redisConnectionInfoService.deleteById(id);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null));
    }

    @GetMapping("list")
    @DynAuthority
    public ResponseEntity<LayuiResut<List<RedisConnectionInfo>>> list(){
        String username = SecurityContextHolder.getUsername();
        return ResponseEntity.ok(LayuiResut.buildSuccess(redisConnectionInfoService.findAll(username)));
    }

    @GetMapping("state/{id}")
    @DynAuthority
    public ResponseEntity<LayuiResut<Map<String,Object>>> state(@PathVariable("id") String id) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(redisConnectionInfoService.getState(id)));
    }
}
