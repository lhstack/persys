package com.lhstack.controller.monitoring.remote;

import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.layui.LayuiTableResult;
import com.lhstack.entity.monitoring.remote.RemoteSqlMonitoring;
import com.lhstack.service.monitoring.remote.IRemoteSqlMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("remote/sql")
public class RemoteSqlController {

    @Autowired
    private IRemoteSqlMonitoringService remoteSqlMonitoringService;


    @GetMapping("namespaceList")
    @DynAuthority
    public ResponseEntity<LayuiResut<List<String>>> findAllApplicationName() {

        return ResponseEntity.ok(LayuiResut.buildSuccess(remoteSqlMonitoringService.findAllApplicationName()));
    }

    @PostMapping("push")
    public ResponseEntity<LayuiResut<Boolean>> pushSql(@RequestParam("c") String content) throws Exception {
        return ResponseEntity.ok(LayuiResut.buildSuccess(remoteSqlMonitoringService.saveSqlMonitoryData(content)));
    }

    @DeleteMapping("del/{group}")
    @DynAuthority
    public ResponseEntity<LayuiResut<Boolean>> del(@PathVariable("group") String group){

        remoteSqlMonitoringService.deleteByNamespace(group);
        return ResponseEntity.ok(LayuiResut.buildSuccess(true));
    }

    @GetMapping("list")
    @DynAuthority
    public ResponseEntity<LayuiTableResult<RemoteSqlMonitoring>> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                      @RequestParam(value = "size", defaultValue = "10") Integer size) throws Exception {
        Page<RemoteSqlMonitoring> list = remoteSqlMonitoringService.findAll(page, size);

        LayuiTableResult<RemoteSqlMonitoring> layuiTableResult = new LayuiTableResult<>();
        layuiTableResult.setCount(list.getTotalElements())
                .setCode(200)
                .setData(list.getContent())
                .setMsg("获取数据成功");
        return ResponseEntity.ok(layuiTableResult);
    }

    @GetMapping("list/{group}")
    @DynAuthority
    public ResponseEntity<LayuiTableResult<RemoteSqlMonitoring>> listGroup(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                           @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                           @PathVariable("group") String appName) throws Exception {
        Page<RemoteSqlMonitoring> list = remoteSqlMonitoringService.findAllByApplicationName(appName,page, size);

        LayuiTableResult<RemoteSqlMonitoring> layuiTableResult = new LayuiTableResult<>();
        layuiTableResult.setCount(list.getTotalElements())
                .setCode(200)
                .setData(list.getContent())
                .setMsg("获取数据成功");
        return ResponseEntity.ok(layuiTableResult);
    }
}
