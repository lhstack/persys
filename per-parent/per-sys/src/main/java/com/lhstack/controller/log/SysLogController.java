package com.lhstack.controller.log;

import cn.hutool.core.io.FileUtil;
import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.aspect.permission.InitAuthority;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.layui.LayuiTableResult;
import com.lhstack.entity.log.FileLogInfo;
import com.lhstack.entity.log.SysLog;
import com.lhstack.entity.log.TaskLog;
import com.lhstack.service.log.IFileLogService;
import com.lhstack.service.log.ISysLogService;
import com.lhstack.service.log.ITaskLogService;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("log")
public class SysLogController {

    @Autowired
    private ISysLogService sysLogService;

    @Autowired
    private IFileLogService fileLogService;

    @Autowired
    private ITaskLogService taskLogService;

    @PostMapping("task/list")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_LOG_QUERY)")
    @DynAuthority
    public ResponseEntity<LayuiTableResult<TaskLog>> findTaskLogList(
            @RequestParam( value = "page",defaultValue = "1") Integer page
            ,@RequestParam( value = "size",defaultValue = "10") Integer size,
            @RequestBody Map<String,Object> examples) throws ParseException {
        LayuiTableResult<TaskLog> logLayuiTableResult = new LayuiTableResult<>();
        Page<TaskLog> pageResult = taskLogService.findByExample(examples, page, size);
        logLayuiTableResult.setCount(pageResult.getTotalElements())
                .setCode(200)
                .setMsg("获取任务日志成功")
                .setData(pageResult.getContent());
        return ResponseEntity.ok(logLayuiTableResult);
    }


    /**
     * 删除任务日志
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("delete/task/{id}")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_LOG_DELETE)")
    @DynAuthority
    public ResponseEntity<LayuiResut<Object>> deleteTaskById(@PathVariable("id") String id) throws Exception {
        taskLogService.deleteById(id);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null).setMsg("删除成功"));
    }

    /**
     * 根据ids删除操作日志
     * @param ids
     * @return
     */
    @DeleteMapping("delete/tasks/{ids}")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_LOG_DELETE)")
    @DynAuthority
    public ResponseEntity<LayuiResut<Object>> deleteTaskByIds(@PathVariable("ids") List<String> ids){
        taskLogService.deleteAllById(ids);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null).setMsg("删除成功"));
    }


    /**
     * 下载大日志文件
     * @param fullPath 全路径，规则 / \\ == @ 例如 E:/logback/v1 ==> E:@logback@v1
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("file/download")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_LOG_QUERY)")
    @DynAuthority
    public Mono<Void> findChildFile(@RequestParam("fullPath") String fullPath, ServerHttpResponse response) throws UnsupportedEncodingException {
        String path = fullPath.replace("@", "/");
        File file = new File(path);
        response.getHeaders().add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        response.getHeaders().add("Content-Length",file.length() + "");
        response.getHeaders().add("content-type","text/log");
        byte[] bytes = FileUtil.readBytes(file);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    /**
     * 读取日志文件内容
     * @param fileLogInfo
     * @return
     */
    @PostMapping(value = "file/childFile")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_LOG_QUERY)")
    @DynAuthority
    public Mono<LayuiResut<List<String>>> findChildFile(@RequestBody FileLogInfo fileLogInfo){
        List<String> content = FileUtil.readLines(fileLogInfo.getFullPath(), CharsetUtil.UTF_8);
        return Mono.just(LayuiResut.buildSuccess(content).setMsg("加载日志文件成功"));
    }

    /**
     * 打开文件夹，返回文件夹里面的目录
     * @param fileLogInfo
     * @return
     */
    @PostMapping("file/childDirector")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_LOG_QUERY)")
    @DynAuthority
    public ResponseEntity<LayuiResut<List<FileLogInfo>>> findChildDirector(@RequestBody FileLogInfo fileLogInfo){
        return ResponseEntity.ok(LayuiResut.buildSuccess(fileLogService.getDirectoryChild(fileLogInfo.getFullPath())));
    }

    /**
     * 根目录
     * @return
     */
    @GetMapping("file/root")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_LOG_QUERY)")
    @DynAuthority
    public ResponseEntity<LayuiResut<List<FileLogInfo>>> findRoot(){
        return ResponseEntity.ok(LayuiResut.buildSuccess(fileLogService.getRoot()).setMsg("获取根节点成功"));
    }

    /**
     * 操作日志复杂查询
     * @param page
     * @param size
     * @param search
     * @return
     */
    @PostMapping("matter/list")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_LOG_QUERY)")
    @DynAuthority
    public ResponseEntity<LayuiTableResult<SysLog>> findAll(@RequestParam(value = "page",defaultValue = "1") Integer page, @RequestParam(value = "size",defaultValue = "10") Integer size ,@RequestBody Map<String,Object> search) throws ParseException {
        Page<SysLog> sysLogPage = sysLogService.findAllByExample(search,page, size);
        LayuiTableResult<SysLog> logLayuiTableResult = new LayuiTableResult<>();
        logLayuiTableResult.setCount(sysLogPage.getTotalElements())
                .setCode(200)
                .setMsg("获取日志成功")
                .setData(sysLogPage.getContent());
        return ResponseEntity.ok(logLayuiTableResult);
    }


    /**
     * 删除操作日志
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("delete/matter/{id}")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_LOG_DELETE)")
    @DynAuthority
    public ResponseEntity<LayuiResut<Object>> deleteById(@PathVariable("id") String id) throws Exception {
        sysLogService.deleteById(id);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null).setMsg("删除成功"));
    }

    /**
     * 根据ids删除操作日志
     * @param ids
     * @return
     */
    @DeleteMapping("delete/matters/{ids}")
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_LOG_DELETE)")
    @DynAuthority
    public ResponseEntity<LayuiResut<Object>> deleteByIds(@PathVariable("ids") List<String> ids){
        sysLogService.deleteAllById(ids);
        return ResponseEntity.ok(LayuiResut.buildSuccess(null).setMsg("删除成功"));
    }
}
