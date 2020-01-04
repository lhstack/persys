<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>oss</title>
    <script type="text/javascript" src="/js/FileSaver.min.js"></script>
    <script type="text/javascript" src="/js/xlsx.full.min.js"></script>
    <#include "/common/header.ftl"/>
    <#include "/common/avue-common.ftl"/>
</head>
<body>
    <div id="app">
        <avue-crud id="table" @row-del="del" @row-update="update" @row-save="save" v-model="oss" ref="crud" :data="list" @refresh-change="refreshChange"
                   @size-change="sizeChange" :option="options" :page="pager" @current-change="currentChange">
            <template slot="accessKeyId" slot-scope="scope">
                <span v-text="scope.row.accessKeyId"></span>
            </template>
            <template slot="accessKeySecret" slot-scope="scope">
                <span v-text="scope.row.accessKeySecret"></span>
            </template>
            <template slot="endPoint" slot-scope="scope">
                <span v-text="scope.row.endPoint"></span>
            </template>
            <template slot="bucketName" slot-scope="scope">
                <span v-text="scope.row.bucketName"></span>
            </template>
            <template slot="schema" slot-scope="scope">
                <span v-text="scope.row.schema"></span>
            </template>
            <template slot="type" slot-scope="scope">
                <span v-text="scope.row.type"></span>
            </template>
            <template slot="token" slot-scope="scope">
                <span v-text="scope.row.token"></span>
            </template>
            <template slot="ipWhiteList" slot-scope="scope">
                <span v-text="scope.row.ipWhiteList"></span>
            </template>
            <template slot="ipWhiteListForm" slot-scope="scope">
                <el-input type="textarea" v-model="oss.ipWhiteList" placeholder="多个用,隔开，默认所有ip都能使用请定义为0.0.0.0" />
            </template>
            <template slot="typeForm" slot-scope="scope">
                <el-select v-model="oss.type">
                    <el-option
                            value="ALI_YUN"
                            label="阿里云">
                        <span>阿里云</span>
                    </el-option>
                    <el-option
                            value="QINIU_YUN"
                            label="七牛云">
                        <span>七牛云</span>
                    </el-option>
                </el-select>
            </template>
        </avue-crud>
    </div>
</body>
<script src="/js/oss/index.js"></script>
</html>