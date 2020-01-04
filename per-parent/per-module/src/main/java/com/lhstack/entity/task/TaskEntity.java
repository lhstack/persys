package com.lhstack.entity.task;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * TaskEntity
 *
 * @blame Android Team
 */
@Data
@Accessors(chain = true)
@Document("tb_task_entity")
@CompoundIndexes(
        @CompoundIndex(name = "uniqueTaskEntityIndex",
                def = "{bean_name:1,method:1,job_name:1,job_group:1,trigger_name:1,trigger_group:1,express:1,type:1}",
                unique = true)
)
public class TaskEntity implements Serializable {
    private static final long serialVersionUID = -131558830605985285L;

    /**
     * id 唯一标识
     */
    @Id
    private String id;

    /**
     * beanName
     */
    @Field("bean_name")
    private String beanName;

    /**
     * bean 方法
     */
    private String method;

    /**
     * 参数
     */
    private String[] parameters;

    /**
     * 参数类型,类全路径
     */
    private String[] parameterTypes;

    /**
     * 任务执行状态
     */
    private String state;

    /**
     * 任务名称
     */
    @Field("job_name")
    private String jobName;

    /**
     * 任务组
     */
    @Field("job_group")
    private String jobGroup;

    /**
     * 触发器名
     */
    @Field("trigger_name")
    private String triggerName;

    /**
     * 触发器组
     */
    @Field("trigger_group")
    private String triggerGroup;

    /**
     * 时间日期或者cron表达式
     */
    private String express;

    /**
     * 任务调度类型，date,time,cron
     */
    private String type;

    /**
     *  任务创建时间
     */
    @Field("create_time")
    private Date createTime;


    /**
     * 任务注释
     */
    private String description;

    /**
     * 任务状态 false代表暂停，true代表运行
     */
    @Field("task_state")
    private Boolean taskState;
}
