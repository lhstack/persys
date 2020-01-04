package com.lhstack.entity.log;

/**
 * 日志类型枚举
 */
public enum FileLogType {
    /**
     * ALL所有日志，包涵所有级别日志, DEBUG 包涵Debug日志，类推
     */
    ALL("all"),DEBUG("debug"),INFO("info"),WARNING("warn"),ERROR("error");

    /**
     * 类型value
     */
    private String typeValue;

    private FileLogType(String typeValue){
        this.typeValue = typeValue;
    }

    public String getTypeValue() {
        return typeValue;
    }
}
