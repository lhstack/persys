package com.lhstack.service.quartz;

import com.lhstack.aspect.sys.SysLog;
import com.lhstack.config.quartz.TaskJob;
import com.lhstack.entity.task.TaskEntity;
import com.lhstack.repository.task.TaskRepository;
import com.lhstack.service.task.IScheduleService;
import com.lhstack.utils.ScheduleBuildUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScheduleServiceImpl implements IScheduleService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private TaskRepository taskRepository;

    /**
     * 分页查询任务信息
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<TaskEntity> findAll(Integer page, Integer size){
        if(page < 1){
            page = 1;
        }
        Page<TaskEntity> taskEntityPage = taskRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("create_time"))));
        return taskEntityPage.map(item ->{
            try {
                Trigger.TriggerState triggerState = scheduler.getTriggerState(TriggerKey.triggerKey(item.getTriggerName(), item.getTriggerGroup()));
                item.setState(triggerState.name());
                if(triggerState == Trigger.TriggerState.NONE){
                    item.setTaskState(false);
                }
            } catch (SchedulerException e) {
                item.setState("NONE");
                item.setTaskState(false);
            }
            return item;
        });
    }

    /**
     * 更新任务
     * @param id
     * @param taskEntity
     * @return
     * @throws SchedulerException
     */
    @Override
    @SysLog
    public TaskEntity update(String id,TaskEntity taskEntity) throws SchedulerException {

        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(id);
        if(!optionalTaskEntity.isPresent())
            throw new NullPointerException("任务不存在");
        TaskEntity oldTask = optionalTaskEntity.get();
        TriggerKey triggerKey = TriggerKey.triggerKey(oldTask.getTriggerName(), oldTask.getTriggerGroup());
        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);
        scheduler.deleteJob(JobKey.jobKey(oldTask.getJobName(),oldTask.getJobGroup()));
        String state = "NONE";
        if(taskEntity.getTaskState() != null && taskEntity.getTaskState()){
            state = runTaskReturnState(taskEntity);
        }
        taskEntity
                .setState(state)
                .setId(id);
        return taskRepository.save(taskEntity);
    }

    @Override
    public void delete(TaskEntity entity) throws Exception {

    }

    /**
     * 运行一次创建的任务
     * @param id
     * @return
     */
    @Override
    @SysLog
    public void runOnceTask(String id){
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(id);
        if(optionalTaskEntity.isPresent()){
            TaskEntity taskEntity = optionalTaskEntity.get();
            try {
                JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(taskEntity.getJobName(), taskEntity.getJobGroup()));
                JobDataMap jobDataMap = new JobDataMap();
                jobDataMap.put(TaskJob.PER_SYS_METADATA,taskEntity);
                if(jobDetail == null){
                    scheduler.scheduleJob(ScheduleBuildUtil.buildJobDetail(taskEntity),ScheduleBuildUtil.buildTrigger(taskEntity));
                    scheduler.pauseJob(JobKey.jobKey(taskEntity.getJobName(),taskEntity.getJobGroup()));
                    scheduler.triggerJob(JobKey.jobKey(taskEntity.getJobName(),taskEntity.getJobGroup()),jobDataMap);
                }else{
                    scheduler.triggerJob(JobKey.jobKey(taskEntity.getJobName(),taskEntity.getJobGroup()),jobDataMap);
                }
            } catch (SchedulerException e) {
                throw new RuntimeException("运行任务失败");
            }
        }else{
            throw new NullPointerException("任务不存在");
        }

    }

    /**
     * 创建一个任务
     * @param taskEntity
     * @return
     * @throws SchedulerException
     */
    @Override
    @SysLog
    public TaskEntity save(TaskEntity taskEntity) throws SchedulerException {
        taskEntity.setCreateTime(new Date())
        .setId(null)
        .setJobName(UUID.randomUUID().toString())
        .setJobGroup(UUID.randomUUID().toString())
        .setTriggerGroup(UUID.randomUUID().toString())
        .setTriggerName(UUID.randomUUID().toString());
        TaskEntity result = taskRepository.save(taskEntity);
        if(result == null)
            throw new TaskRejectedException("任务创建失败");
        result.setState(runTaskReturnState(result));
        return taskRepository.save(result);
    }


    private String runTaskReturnState(TaskEntity result) throws SchedulerException {
        if(result.getTaskState() != null && result.getTaskState()){
            scheduler.scheduleJob(ScheduleBuildUtil.buildJobDetail(result),ScheduleBuildUtil.buildTrigger(result));
            return scheduler.getTriggerState(TriggerKey.triggerKey(result.getTriggerName(), result.getTriggerGroup())).name();
        }
        return "NONE";
    }


    /**
     * 运行所有任务
     */
    @Override
    @SysLog
    public void runAll(){
        List<TaskEntity> list = taskRepository.findAll();
        list.forEach(item ->{
            try {
                resumeTask(item.getId());
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * 暂停所有任务
     */
    @Override
    @SysLog
    public void pauseAll() throws SchedulerException {
        scheduler.pauseAll();
    }


    /**
     * 移除一个任务
     * @param id
     */
    @Override
    @SysLog
    public void deleteById(String id) throws SchedulerException {
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(id);
        if(optionalTaskEntity.isPresent()){
            TaskEntity taskEntity = optionalTaskEntity.get();
            TriggerKey triggerKey = TriggerKey.triggerKey(taskEntity.getTriggerName(), taskEntity.getTriggerGroup());
            JobKey jobKey = JobKey.jobKey(taskEntity.getJobName(), taskEntity.getJobGroup());
            /**
             * 暂停触发器
             */
            scheduler.pauseTrigger(triggerKey);
            /**
             * 移除触发器
             */
            scheduler.unscheduleJob(triggerKey);
            /**
             * 删除job
             */
            scheduler.deleteJob(jobKey);
            /**
             * 删除任务
             */
            taskRepository.deleteById(id);
        }else{
            throw new NullPointerException("任务不存在");
        }
    }

    @Override
    public TaskEntity findById(String s) throws Exception {
        return taskRepository.findById(s).orElse(null);
    }

    @Override
    public List<TaskEntity> findAll() throws Exception {
        return taskRepository.findAll();
    }

    /**
     * 暂停一个任务
     * @param id
     */
    @Override
    @SysLog
    public Boolean pauseTask(String id) throws SchedulerException {
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(id);
        if(optionalTaskEntity.isPresent()){
            TaskEntity taskEntity = optionalTaskEntity.get();
            scheduler.pauseJob(JobKey.jobKey(taskEntity.getJobName(),taskEntity.getJobGroup()));
            taskEntity.setTaskState(false);
            taskRepository.save(taskEntity);
            return true;
        }else{
            return false;
        }
    }

    /**
     * 恢复一个任务
     * @param id
     */
    @Override
    @SysLog
    public Boolean resumeTask(String id) throws SchedulerException {
        Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(id);
        if(optionalTaskEntity.isPresent()){
            TaskEntity taskEntity = optionalTaskEntity.get();
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(taskEntity.getJobName(), taskEntity.getJobGroup()));
            String state = "NONE";
            if(jobDetail == null){
                taskEntity.setTaskState(true);
                state = runTaskReturnState(taskEntity);
            }else{
                scheduler.resumeJob(JobKey.jobKey(taskEntity.getJobName(),taskEntity.getJobGroup()));
            }
            taskEntity.setTaskState(true)
                        .setState(state);
            taskRepository.save(taskEntity);
            return true;
        }else{
            return false;
        }
    }

}
