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
        <avue-crud :table-loading="loading" id="table" ref="crud" :data="data" @row-del="del" @search-reset="searchReset" @search-change="searchChange"  @refresh-change="refreshChange" @size-change="sizeChange" :option="options" :page="pager" @current-change="currentChange">
            <template slot="id" slot-scope="scope">
                <span :title="scope.row.id">{{scope.row.id}}</span>
            </template>
            <template slot="beanName" slot-scope="scope">
                <span :title="scope.row.beanName">{{scope.row.beanName}}</span>
            </template>
            <template slot="method" slot-scope="scope">
                <span :title="scope.row.method">{{scope.row.method}}</span>
            </template>
            <template slot="processDateTime" slot-scope="scope">
                <span :title="scope.row.processDateTime">{{scope.row.processDateTime}}</span>
            </template>
            <template slot="state" slot-scope="scope">
                <el-button :type="scope.row.state ? 'primary' : 'danger'" :title="scope.row.state" v-text="scope.row.state ? '成功' : '失败'" size="small"></el-button>
            </template>
            <template slot="description" slot-scope="scope">
                <span :title="scope.row.description">{{scope.row.description}}</span>
            </template>
            <template slot="parameters" slot-scope="scope">
                <span :title="scope.row.parameters">{{scope.row.parameters}}</span>
            </template>
            <template slot-scope="scope" slot="menuLeft">
                <el-button type="primary"
                           icon="el-icon-check"
                           size="small"
                           @click="delSelect"
                >删除选中的</el-button>
            </template>
            <el-form-item slot="search">
                <el-col :md="6" :xs="24">
                    <el-form-item label="用户名">
                        <el-input placeholder="请输入bean名称" size="small" v-model="search.beanName" />
                    </el-form-item>
                </el-col>
                <el-col :md="6" :xs="24">
                    <el-form-item label="方法">
                        <el-input placeholder="请输入bean方法" size="small" v-model="search.method" />
                    </el-form-item>
                </el-col>
                <el-col :md="5" :xs="24">
                    <el-form-item label="排序方式">
                        <el-radio v-model="search.sortBy" label="asc">升序</el-radio>
                        <el-radio v-model="search.sortBy" label="desc">降序</el-radio>
                    </el-form-item>
                </el-col>

                <el-col :md="6" :xs="24">
                    <el-form-item label="排序字段">
                        <el-select v-model="search.sortField" placeholder="请选择排序字段">
                            <el-option
                                    v-for="item,index in sortFields"
                                    :key="index"
                                    :label="item"
                                    :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>

                <el-col :md="12" :xs="24">
                    <el-form-item label="时间区间">
                        <el-date-picker
                                v-model="search.times"
                                type="datetimerange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期">
                        </el-date-picker>
                    </el-form-item>
                </el-col>
            </el-form-item>
        </avue-crud>
    </div>
</body>
<script type="text/javascript" src="/js/log/taskLog.js"></script>
</html>