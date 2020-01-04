<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/common/header.ftl"/>
    <script type="application/javascript" src="/js/echarts.min.js"></script>
    <#include "/common/avue-common.ftl"/>
    <link href="/css/permission/index.css" type="text/css" rel="stylesheet"/>
    <title>redis List</title>
    <style>
        body, html {
            padding: 0;
            margin: 0;
            overflow-x: hidden;
        }

        .box {
            display: flex;
            flex-wrap: wrap;
        }

        .item {
            width: 20%;
            margin: 2%;
            min-height: 100px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            box-sizing: border-box;
        }

        .item:hover {
            cursor: pointer;
            transition: 0.5s all;
            box-shadow: 0 0 3px 3px #00FFFF;
            background: gray;
            color: #fff;
        }

        .item > span:nth-child(1) {
            font-size: 14px;
            font-family: "微软雅黑";
            font-weight: bold;
            display: block;
            text-align: center;
            margin-bottom: 10px;
            margin-top: 10px;
        }

        .item > div:nth-child(2) {
            overflow-x: auto;
            min-height: 60px;
            text-align: center;
        }
    </style>
</head>
<body>
<div id="app">
    <el-dialog
            title="添加连接信息"
            :visible.sync="dialogVisible"
            width="30%">
        <el-form :rules="rules" ref="ruleForm" :model="redisInfo" label-width="80px">
            <el-form-item label="host" prop="dbHost">
                <el-input placeholder="redisHost地址" v-model="redisInfo.dbHost"></el-input>
            </el-form-item>
            <el-form-item label="port">
                <el-input type="number" placeholder="redis端口，不填默认6379" v-model="redisInfo.dbPort"></el-input>
            </el-form-item>
            <el-form-item label="database">
                <el-input type="number" placeholder="redis数据库，不填默认0" v-model="redisInfo.database"></el-input>
            </el-form-item>
            <el-form-item label="password">
                <el-input placeholder="redisHost密码，可选" type="password" v-model="redisInfo.dbPassword"></el-input>
            </el-form-item>
            <el-form-item label="注释">
                <el-input placeholder="注释信息" type="text" v-model="redisInfo.description"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </span>
    </el-dialog>
    <div style="display: flex;justify-content: flex-end;width: 100%;margin-bottom: 100px;">
        <el-select style="width: 500px" @change="change" v-model="select" placeholder="请选择要监控的redis">
            <el-option
                    v-for="(item,index) in redisList"
                    :key="index"
                    :label="item.dbHost"
                    :value="item.id">
                <span style="float: left">{{ item.id }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ item.description }}</span>
            </el-option>
        </el-select>
        <span>&nbsp;&nbsp;</span>
        <el-button type="primary" @click="update">更新</el-button>
        <el-button type="primary" @click="dialogVisible=true;type='save';redisInfo={}">添加</el-button>
        <el-button type="danger" @click="del">删除</el-button>
    </div>
    <div style="width:100%;display: flex;overflow-x: hidden;overflow-y: hidden;margin-bottom: 100px">
        <div style="width: 60%">
            <div id="line" style="width: 100%;height:400px;"></div>
        </div>
        <div style="width: 40%">
            <div id="pie" style="width: 100%;height:400px;"></div>
        </div>
    </div>
    <div class="box">
        <div v-for="(item,index) in list" :key="index" class="item">
                <span v-text="item.key">

                </span>
            <div v-text="item.value">

            </div>
        </div>
    </div>
</div>
<script type="application/javascript" src="/js/monitoring/remote/redis.js"></script>
</body>
</html>