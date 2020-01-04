<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>task List</title>
    <script type="text/javascript" src="/js/FileSaver.min.js"></script>
    <script type="text/javascript" src="/js/xlsx.full.min.js"></script>
    <#include "/common/header.ftl"/>
    <#include "/common/avue-common.ftl"/>
    <style>
        .el-form-item__label{
            width: 100px !important;
        }
        .el-form-item__content{
            margin-left: 100px !important;
        }
    </style>
</head>
<body>
    <div id="app">
        <avue-crud @row-save="addRollback" @row-update="update" v-model="task" :table-loading="loading" id="table" ref="crud" :data="data" @row-del="del"  @refresh-change="refreshChange" @size-change="sizeChange" :option="options" :page="pager" @current-change="currentChange">
            <template slot="id" slot-scope="scope">
                <span :title="scope.row.id">{{scope.row.id}}</span>
            </template>
            <template slot="beanName" slot-scope="scope">
                <span :title="scope.row.beanName">{{scope.row.beanName}}</span>
            </template>
            <template slot="method" slot-scope="scope">
                <span :title="scope.row.method">{{scope.row.method}}</span>
            </template>
            <template slot="type" slot-scope="scope">
                <span :title="scope.row.type">{{scope.row.type | typeFilter}}</span>
            </template>
            <template slot="state" slot-scope="scope">
                <el-button :type="getBtnType(scope.row.state)" size="small">{{scope.row.state}}</el-button>
            </template>
            <template slot="createTime" slot-scope="scope">
                <span :title="scope.row.createTime">{{scope.row.createTime}}</span>
            </template>
            <template slot="express" slot-scope="scope">
                <span :title="getExpressToString(scope.row.express)">{{scope.row.express}}</span>
            </template>
            <template slot="parameters" slot-scope="scope">
                <span :title="scope.row.parameters">{{scope.row.parameters}}</span>
            </template>
            <template slot="parameterTypes" slot-scope="scope">
                <span :title="scope.row.parameterTypes">{{scope.row.parameterTypes}}</span>
            </template>
            <template slot="jobName" slot-scope="scope">
                <span :title="scope.row.jobName">{{scope.row.jobName}}</span>
            </template>
            <template slot="jobGroup" slot-scope="scope">
                <span :title="scope.row.jobGroup">{{scope.row.jobGroup}}</span>
            </template>
            <template slot="triggerName" slot-scope="scope">
                <span :title="scope.row.triggerName">{{scope.row.triggerName}}</span>
            </template>
            <template slot="triggerGroup" slot-scope="scope">
                <span :title="scope.row.triggerGroup">{{scope.row.triggerGroup}}</span>
            </template>
            <template slot="description" slot-scope="scope">
                <span :title="scope.row.description">{{scope.row.description}}</span>
            </template>

            <template slot="parametersForm" slot-scope="scope">
                <div>
                    <el-button type="primary" plant @click="addParameters">添加参数</el-button>
                    <div v-for="item,index in task.parameters" :key="index" style="display: flex;flex-wrap: wrap;width: 600px">
                        <el-input v-model="task.parameters[index]" type="textarea" placeholder="请输入参数" style="width: 40%"></el-input>
                        <el-input v-model="task.parameterTypes[index]" type="textarea" placeholder="请输入参数类型" style="width: 40%"></el-input>
                        <div style="width: 20%;display: flex;flex-direction: column;justify-content: center;">
                            <el-button style="height: 40px;" plain type="primary" @click="removeParameter(index)">移除</el-button>
                        </div>
                    </div>
                </div>
            </template>
            <template slot="expressForm" slot-scope="scope">
                <el-tooltip class="item" effect="light" placement="right">
                    <el-input v-model="task.express" placeholder="请输入任务调度表达式"></el-input>
                    <template slot="content">
                        <dl>
                            <dt>时间类型</dt>
                            <dd>
                                <p>&nbsp;&nbsp;10@m[@5] 代表每10分钟执行1次  [@5]可选，不写默认每到触发点执行1次,如果写了，就每秒执行1次，总共只执行5次</p>
                                <p>&nbsp;&nbsp;10@s[@5] 代表每10秒执行1次</p>
                                <p> &nbsp;&nbsp;10@h[@5] 代表每10小时执行1次</p>
                                <p>&nbsp;&nbsp;10@count_s 代表每秒执行1次 总共执行10次</p>
                                <p>&nbsp;&nbsp;10@count_m 代表每分执行1次 总共执行10次</p>
                                <p>&nbsp;&nbsp;10@count_h 代表每小执行1次 总共执行10次</p>
                            </dd>
                            <dt>日历类型</dt>
                            <dd>
                                <p>&nbsp;&nbsp;10@day 代表每日执行1次</p>
                                <p>&nbsp;&nbsp;10@year 代表每年执行1次</p>
                                <p> &nbsp;&nbsp;10@week 代表每周执行1次</p>
                                <p>&nbsp;&nbsp;10@month 代表每月执行1次</p>
                            </dd>
                            <dt>cron</dt>
                            <dd>
                                <p>&nbsp;&nbsp;通过cron表达式生成任务调度器 如: 0 0 0 * * *</p>
                            </dd>
                        </dl>
                    </template>
                </el-tooltip>
            </template>

            <template slot="typeForm" slot-scope="scope">
                <el-select v-model="task.type" clearable  placeholder="请选择任务调度类型">
                    <el-option
                            label="日历"
                            value="calendar">
                    </el-option>
                    <el-option
                            label="时间"
                            value="simple">
                    </el-option>
                    <el-option
                            label="cron"
                            value="cron">
                    </el-option>
                </el-select>
            </template>
            <template slot="menu" slot-scope="scope">
                <el-button type="text" size="small" @click="runOnce(scope.row)">
                    <i class="el-icon-video-pause"></i>
                    触发
                </el-button>
                <el-button type="text" size="small" @click="updateState(scope.row)">
                    <i class="el-icon-video-pause" v-if="scope.row.state == 'NORMAL'"></i>
                    <i class="el-icon-video-play" v-else></i>
                    {{scope.row.state == 'NORMAL' ? '暂停' : '运行'}}
                </el-button>
            </template>
            <template slot-scope="scope" slot="menuLeft">
                <el-button type="primary"
                           icon="el-icon-plus"
                           size="small"
                           @click="addForm()"
                >添加</el-button>
                <el-button type="primary"
                           icon="el-icon-check"
                           size="small"
                           @click="delSelect"
                >删除选中的</el-button>
            </template>
            <template slot-scope="scope" slot="menuRight">
                <el-button type="primary"
                           icon="el-icon-video-play"
                           size="small"
                           @click="runAll"
                >运行所有任务</el-button>
                <el-button type="primary"
                           icon="el-icon-video-pause"
                           size="small"
                           @click="pauseAll"
                >暂停所有任务</el-button>
            </template>
        </avue-crud>
    </div>
</body>
<script type="text/javascript" src="/js/quartz/quartz.js"></script>
</html>