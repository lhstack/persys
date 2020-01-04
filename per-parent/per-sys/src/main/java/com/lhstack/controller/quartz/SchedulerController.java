package com.lhstack.controller.quartz;

import com.lhstack.aspect.permission.DynAuthority;
import com.lhstack.aspect.permission.InitAuthority;
import com.lhstack.entity.layui.LayuiResut;
import com.lhstack.entity.layui.LayuiTableResult;
import com.lhstack.entity.task.TaskEntity;
import com.lhstack.service.task.IScheduleService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quartz")
public class SchedulerController {

    @Autowired
    private IScheduleService scheduleService;

    @GetMapping("list")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_TASK_QUERY)")
    public ResponseEntity<LayuiTableResult<TaskEntity>> findAll(@RequestParam(value = "page",defaultValue = "1") Integer page, @RequestParam(value = "size",defaultValue = "10") Integer size) throws Exception {
        Page<TaskEntity> pages = scheduleService.findAll(page, size);
        LayuiTableResult<TaskEntity> layuiTableResult = new LayuiTableResult<>();
        layuiTableResult.setCount(pages.getTotalElements())
                .setCode(200)
                .setData(pages.getContent())
                .setMsg("获取任务列表成功");
        return ResponseEntity.ok(layuiTableResult);
    }


    @GetMapping("runOnce/{id}")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_TASK_UPDATE)")
    public ResponseEntity<LayuiResut<Boolean>> runOnce(@PathVariable("id") String id){
        scheduleService.runOnceTask(id);
        return ResponseEntity.ok(LayuiResut.buildSuccess(true).setMsg("触发任务成功"));
    }


    /**
     * 添加任务
     */
    @PostMapping("add")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_TASK_ADD)")
    public ResponseEntity<LayuiResut<TaskEntity>> add(@RequestBody TaskEntity taskEntity) throws Exception {
        TaskEntity result = scheduleService.save(taskEntity);
        return ResponseEntity.ok(LayuiResut.buildSuccess(taskEntity).setMsg("任务添加成功"));
    }

    /**
     * 更新任务
     */
    @PutMapping("update/{id}")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_TASK_UPDATE)")
    public ResponseEntity<LayuiResut<TaskEntity>> updateTask(@PathVariable("id") String id, @RequestBody TaskEntity taskEntity) throws Exception {
       TaskEntity result = scheduleService.update(id, taskEntity);
        return ResponseEntity.ok(LayuiResut.buildSuccess(taskEntity).setMsg("任务更新成功"));
    }

    /**
     * 暂停一个正在运行的任务
     * @param id
     * @return
     * @throws SchedulerException
     */
    @PutMapping("pause/{id}")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_TASK_UPDATE)")
    public ResponseEntity<LayuiResut<Boolean>> pauseTask(@PathVariable("id") String id) throws SchedulerException {
        return ResponseEntity.ok(LayuiResut.buildSuccess( scheduleService.pauseTask(id)).setMsg("暂停任务成功"));
    }

    /**
     * 恢复一个暂停的任务
     * @param id
     * @return
     * @throws SchedulerException
     */
    @PutMapping("resume/{id}")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_TASK_UPDATE)")
    public ResponseEntity<LayuiResut<Boolean>> resumeTask(@PathVariable("id") String id) throws SchedulerException {
        return ResponseEntity.ok(LayuiResut.buildSuccess( scheduleService.resumeTask(id)).setMsg("运行任务成功"));
    }

    /**
     * 删除一个任务
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("delete/{id}")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_TASK_DELETE)")
    public ResponseEntity<LayuiResut<Boolean>> deleteById(@PathVariable("id") String id) throws Exception {
        scheduleService.deleteById(id);
        return ResponseEntity.ok(LayuiResut.buildSuccess(true).setMsg("移除任务成功"));
    }

    /**
     * 暂停所有任务
     * @return
     * @throws SchedulerException
     */
    @PutMapping("pauseAll")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_TASK_UPDATE)")
    public ResponseEntity<LayuiResut<Boolean>> pauseAll() throws SchedulerException {
        scheduleService.pauseAll();
        return ResponseEntity.ok(LayuiResut.buildSuccess(true).setMsg("任务暂停成功"));
    }

    /**
     * 运行所有任务
     * @return
     * @throws SchedulerException
     */
    @PutMapping("runAll")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_TASK_UPDATE)")
    public ResponseEntity<LayuiResut<Boolean>> runAll() throws SchedulerException {
        scheduleService.runAll();
        return ResponseEntity.ok(LayuiResut.buildSuccess(true).setMsg("任务执行成功"));
    }

    /**
     * 删除任务
     * @param ids
     * @return
     * @throws Exception
     */
    @DeleteMapping("deletes/{ids}")
    @DynAuthority
    @InitAuthority("anyAuthority(ROLE_ADMIN,PERMISSION_ADMIN_TASK_DELETE)")
    public ResponseEntity<LayuiResut<Boolean>> deleteByIds(@PathVariable("ids") List<String> ids) throws Exception {
        ids.forEach(item ->{
            try {
                scheduleService.deleteById(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return ResponseEntity.ok(LayuiResut.buildSuccess(true).setMsg("移除任务成功"));
    }
}
