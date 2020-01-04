<!DOCTYPE html>
<html lang="en">
<head>
    <title>file List</title>
    <#include "/common/header.ftl"/>
    <#include "/common/avue-common.ftl"/>
    <script type="text/javascript" src="/js/axios.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/log/fileLog.css" />
</head>
<body>
    <div id="app">
        <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-left: 10px;top:10px;position: fixed">
            <span @click="loadMethod(item.method,item.args)" style="cursor: pointer" v-for="item,index in history">
                {{item.title}}<span v-show="history.length - 1 != index"><i class="el-icon-arrow-right"></i></span>
            </span>
        </el-breadcrumb>
        <div style="width:100%;margin-left: 10px;margin-top: 40px;overflow-y: auto;height: 80vh;position:fixed;">
            <div v-for="item,index in data" :key="index" class="item_file" @click="loadChild(item)">
                <img class="item_img" :src="item.icon" />
                <div>
                    <div class="t1" v-text="item.fileName"></div>
                    <div v-if="item.fileType == 'FILE'" class="t2" v-text="item.sizeName"></div>
                </div>
            </div>
        </div>
        <el-dialog
                :title="title"
                :visible.sync="dialogVisible"
                id="content"
                width="80%"
                top="8vh"
                >
            <div v-html="content" style="overflow-y: auto;height: 400px"></div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false;">取 消</el-button>
                <el-button type="primary" @click="dialogVisible = false;">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</body>
<script type="text/javascript" src="/js/log/fileLog.js"></script>
</html>