package com.lhstack.controller.monitoring;

import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.layui.LayuiTableResult;
import com.lhstack.entity.monitoring.SqlMonitoring;
import com.lhstack.service.monitoring.ISqlMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("monitoring/mysql")
public class SqlMonitoringController {

    @Autowired
    private ISqlMonitoringService sqlMonitoringService;

    @GetMapping("state")
    @DynAuthority
    public ResponseEntity<LayuiResut<Map<String,Object>>> state() throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(sqlMonitoringService.getMysqlState()));
    }


    @GetMapping("list")
    @DynAuthority
    public ResponseEntity<List<SqlMonitoring>> list() throws Exception {
        return ResponseEntity.ok(sqlMonitoringService.findAll());
    }

    @GetMapping("list/{page}/{size}")
    @DynAuthority
    public ResponseEntity<LayuiTableResult<SqlMonitoring>> list(@PathVariable("page") Integer page,@PathVariable("size") Integer size) throws Exception {
        Page<SqlMonitoring> list = sqlMonitoringService.findAll(page, size);
        LayuiTableResult<SqlMonitoring> layuiTableResult = new LayuiTableResult<>();
        layuiTableResult.setCount(list.getTotalElements())
                .setCode(200)
                .setData(list.getContent())
                .setMsg("success");
        return ResponseEntity.ok(layuiTableResult);
    }
}
