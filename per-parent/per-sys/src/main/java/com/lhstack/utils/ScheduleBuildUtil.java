package com.lhstack.utils;

import com.lhstack.config.quartz.TaskJob;
import com.lhstack.entity.task.TaskEntity;
import org.quartz.*;

/**
 * 任务构建工具类
 */
public final class ScheduleBuildUtil {


    /**
     * 构建jobDetail
     * @param result
     * @return
     */
    public static JobDetail buildJobDetail(TaskEntity result){
        return JobBuilder.newJob(TaskJob.class)
                .withIdentity(result.getJobName(),result.getJobGroup())
                .withDescription(result.getDescription())
                .build();
    }

    /**
     * 构建trigger
     * @param result
     * @return
     */
    public static Trigger buildTrigger(TaskEntity result){
        return TriggerBuilder.newTrigger()
                .withIdentity(result.getTriggerName(),result.getTriggerGroup())
                .usingJobData(initJobMetaData(result))
                .withSchedule(initSchdule(result))
                .build();
    }

    private static JobDataMap initJobMetaData(TaskEntity taskEntity) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(TaskJob.PER_SYS_METADATA,taskEntity);
        return jobDataMap;
    }

    private static ScheduleBuilder<? extends Trigger>  initSchdule(TaskEntity result) {
        switch (result.getType().toLowerCase()){
            case "cron":{
                return ScheduleBuildUtil.buildCronSchedule(result.getExpress());
            }
            case "simple":{
                return ScheduleBuildUtil.buildSimpleSchedule(result.getExpress());
            }
            case "calendar":{
                return ScheduleBuildUtil.buildCalendarSchedule(result.getExpress());
            }
            default:{
                throw new NullPointerException("任务调度器不支持此类型");
            }
        }
    }


    /**
     * 通过cron表达式生成任务调度器 如: 0 0 0 * * *
     * @param express
     * @return
     */
    public static CronScheduleBuilder buildCronSchedule(String express) {
        return CronScheduleBuilder.cronSchedule(express);
    }

    /**
     *  通过规则生成Simple任务调度器
     *  规则如下:
     *      10@m[@5] 代表每10分钟执行1次  [@5]可选，不写默认每到触发点执行1次,如果写了，就每秒执行1次，总共只执行5次
     *      10@s[@5] 代表每10秒执行5次
     *      10@h[@5] 代表每10小时执行5次
     *
     *      10@count_s 代表每秒执行1次 总共执行10次
     *      10@count_m 代表每分执行1次 总共执行10次
     *      10@count_h 代表每小执行1次 总共执行10次
     * @param express
     * @return
     */
    public static SimpleScheduleBuilder buildSimpleSchedule(String express) {
        String[] parameters = express.split("@");
        Integer value = Integer.parseInt(parameters[0]);
        String unit = parameters[1];
        switch (unit.toLowerCase()){
            case "s":{
                if(parameters.length == 3){
                    return SimpleScheduleBuilder.repeatSecondlyForTotalCount(Integer.parseInt(parameters[2]),value);
                }
                return SimpleScheduleBuilder.repeatSecondlyForever(value);
            }
            case "m":{
                if(parameters.length == 3){
                    return SimpleScheduleBuilder.repeatMinutelyForTotalCount(Integer.parseInt(parameters[2]),value);
                }
                return SimpleScheduleBuilder.repeatMinutelyForever(value);
            }
            case "h":{
                if(parameters.length == 3){
                    return SimpleScheduleBuilder.repeatHourlyForTotalCount(Integer.parseInt(parameters[2]),value);
                }
                return SimpleScheduleBuilder.repeatHourlyForever(value);
            }
            case "count_s":{
                return SimpleScheduleBuilder.repeatSecondlyForTotalCount(value);
            }
            case "count_m":{
                return SimpleScheduleBuilder.repeatMinutelyForTotalCount(value);
            }
            case "count_h":{
                return SimpleScheduleBuilder.repeatHourlyForTotalCount(value);
            }
            default:{
                return SimpleScheduleBuilder.repeatSecondlyForever();
            }
        }
    }

    /**
     *  通过规则生成calendar任务调度器
     *  规则如下:
     *      10@day 代表每日执行1次
     *      10@year 代表每年执行1次
     *      10@week 代表每周执行1次
     *      10@month 代表每月执行1次
     * @param express
     * @return
     */
    public static CalendarIntervalScheduleBuilder buildCalendarSchedule(String express) {
        CalendarIntervalScheduleBuilder calendarSchedule = CalendarIntervalScheduleBuilder.calendarIntervalSchedule();
        String[] parameters = express.split("@");
        Integer value = Integer.parseInt(parameters[0]);
        switch (parameters[1].toLowerCase()){
            case "day":{
                return calendarSchedule.withInterval(value, DateBuilder.IntervalUnit.DAY);
            }
            case "year":{
                return calendarSchedule.withInterval(value, DateBuilder.IntervalUnit.YEAR);
            }
            case "week":{
                return calendarSchedule.withInterval(value, DateBuilder.IntervalUnit.WEEK);
            }
            case "month":{
                return calendarSchedule.withInterval(value, DateBuilder.IntervalUnit.MONTH);
            }
            default:{
                throw new NullPointerException("任务调度器不支持此类型");
            }
        }
    }
}
