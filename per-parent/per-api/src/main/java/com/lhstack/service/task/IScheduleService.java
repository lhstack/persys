package com.lhstack.service.task;

import com.lhstack.entity.task.TaskEntity;
import com.lhstack.service.IBaseService;
import org.quartz.SchedulerException;

public interface IScheduleService extends IBaseService<String,TaskEntity> {


    void runOnceTask(String id);

    void runAll();

    void pauseAll() throws SchedulerException;

    Boolean pauseTask(String id) throws SchedulerException;

    Boolean resumeTask(String id) throws SchedulerException;
}
