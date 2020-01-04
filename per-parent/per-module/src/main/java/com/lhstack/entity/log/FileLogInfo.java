package com.lhstack.entity.log;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 日志文件封装类
 */
@Data
@Accessors(chain = true)
public class FileLogInfo implements Serializable {

    private static final long serialVersionUID = -3173986038736025551L;
    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 全路径
     */
    private String fullPath;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 对size进行处理，变成人看得懂的
     */
    private String sizeName;


    /**
     * DIRECTOR 目录 FILE 文件
     */
    private String fileType;

    /**
     * 文件所属类型
     */
    private String type;
}
