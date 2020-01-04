package com.lhstack.config.quartz;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhstack.common.ApplicationContextHolder;
import com.lhstack.entity.log.TaskLog;
import com.lhstack.entity.task.TaskEntity;
import com.lhstack.service.log.ITaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.quartz.*;
import org.springframework.context.ApplicationContext;

import java.util.Date;

/**
 * 任务调度实现类
 */
@Slf4j
public class TaskJob implements Job {
    public static final String PER_SYS_METADATA = "PER_SYS_METADATA";
    private ApplicationContext applicationContext;
    private TaskEntity taskEntity;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        init(context);
        if(taskEntity != null && applicationContext != null){
            Object bean = applicationContext.getBean(taskEntity.getBeanName());
            ITaskLogService taskLogService = applicationContext.getBean(ITaskLogService.class);
            TaskLog taskLog = new TaskLog();
            taskLog.setBeanName(taskEntity.getBeanName())
                    .setMethod(taskEntity.getMethod())
                    .setParameters(JSONObject.toJSONString(taskEntity.getParameters()))
                    .setProcessDateTime(new Date())
                    .setDescription(taskEntity.getDescription());
            try {
                Object[] objects = initParameter(taskEntity.getParameters(), taskEntity.getParameterTypes());
                Object o = MethodUtils.invokeExactMethod(bean, taskEntity.getMethod(), taskEntity.getParameters() == null ? new Object[]{} : objects);
                taskLog.setState(true);
            } catch (Exception e) {
                e.printStackTrace();
                taskLog.setState(false);
                taskLog.setExMsg(e.getMessage());
            }finally {
                try {
                    log.info("task execution status as ---> {}",taskLog);
                    taskLogService.save(taskLog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private Object[] initParameter(String[] parameters, String[] parameterTypes) {

        Object[] objects = new Object[parameters.length];

        for (int i = 0; i < objects.length; i++) {
            try {
                objects[i] = parameterBind(parameters[i],parameterTypes[i]);
            } catch (Exception e) {
                objects[i] = null;
            }
        }
        return objects;
    }

    private Object parameterBind(String parameter, String parameterType) throws Exception {
        if(parameterType.equals("java.lang.String") || parameterType.equals(CharSequence.class.getName())){
            return parameter;
        }
        return new ObjectMapper().readValue(parameter,Class.forName(parameterType));
    }

    private void init(JobExecutionContext context) {
        Trigger trigger = context.getTrigger();
        JobDataMap jobDataMap = trigger.getJobDataMap();
        applicationContext = ApplicationContextHolder.getApplicationContext();
        taskEntity = jobDataMap.containsKey(PER_SYS_METADATA) ? (TaskEntity) jobDataMap.get(PER_SYS_METADATA) : null;
    }
}
