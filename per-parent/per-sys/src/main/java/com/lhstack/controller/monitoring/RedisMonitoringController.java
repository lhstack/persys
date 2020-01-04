package com.lhstack.controller.monitoring;

import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.service.monitoring.IRedisMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("monitoring/redis")
public class RedisMonitoringController {

    @Autowired
    private IRedisMonitoringService redisMonitoringService;


    @GetMapping("state")
    @DynAuthority
    public ResponseEntity<LayuiResut<Map<String,Object>>> getStats(){
        return ResponseEntity.ok(LayuiResut.buildSuccess(redisMonitoringService.getState()));
    }
}
