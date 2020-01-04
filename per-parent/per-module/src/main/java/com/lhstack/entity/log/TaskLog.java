package com.lhstack.entity.log;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


/**
 * 任务日志类
 */
@Data
@Accessors(chain = true)
@Document("tb_task_log")
public class TaskLog implements Serializable {


    private static final long serialVersionUID = 2268897105708434074L;
    @Id
    private String id;

    /**
     * 任务执行beanName
     */
    @Field("bean_name")
    private String beanName;

    /**
     * 任务执行方法
     */
    @Field("method")
    private String method;

    /**
     * 任务执行时间
     */
    @Field(value = "process_date_time")
    private Date processDateTime;

    /**
     * 任务执行状态,true 执行成功,false 执行失败
     */
    private Boolean state;

    /**
     * 方法参数
     */
    private String parameters;


    /**
     * 任务注释
     */
    private String description;

    /**
     * 异常信息
     */
    @Field("ex_msg")
    private String exMsg;

}
