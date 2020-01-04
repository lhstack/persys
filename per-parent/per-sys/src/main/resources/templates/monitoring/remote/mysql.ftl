<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/common/header.ftl"/>
    <script type="application/javascript" src="/js/echarts.min.js"></script>
    <#include "/common/avue-common.ftl"/>
    <link href="/css/permission/index.css" type="text/css" rel="stylesheet"/>
    <title>Mysql List</title>
    <style>
        body,html{
            padding: 0;
            margin:0;
            overflow-x: hidden;
        }
        .box{
            display: flex;
            flex-wrap: wrap;
        }
        .item{
            width: 20%;
            margin: 2%;
            min-height: 100px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            box-sizing: border-box;
        }
        .item:hover{
            cursor: pointer;
            transition: 0.5s all;
            box-shadow: 0 0 3px 3px #00FFFF;
            background: gray;
            color: #fff;
        }
        .item > span:nth-child(1){
            font-size: 14px;
            font-family: "微软雅黑";
            font-weight: bold;
            display: block;
            text-align: center;
            margin-bottom: 10px;
            margin-top: 10px;
        }
        .item > div:nth-child(2){
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
            <el-form :rules="rules" ref="ruleForm" :model="mysqlInfo" label-width="80px">
                <el-form-item label="url" prop="dbUrl">
                    <el-input placeholder="mysql Url" v-model="mysqlInfo.dbUrl"></el-input>
                </el-form-item>
                <el-form-item label="用户名" prop="dbUsername">
                    <el-input placeholder="mysql用户名，可选" type="text" v-model="mysqlInfo.dbUsername"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="dbPassword">
                    <el-input type="password" placeholder="mysql数据库密码，可选" type="text" v-model="mysqlInfo.dbPassword"></el-input>
                </el-form-item>
                <el-form-item label="注释">
                    <el-input placeholder="注释信息" type="text" v-model="mysqlInfo.description"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </span>
        </el-dialog>
        <div style="display: flex;justify-content: flex-end;width: 100%;margin-bottom: 100px;">
            <el-select style="width: 500px" @change="change" v-model="select" placeholder="请选择要监控的mysql">
                <el-option
                        v-for="(item,index) in mysqlInfoList"
                        :key="index"
                        :label="item.dbUrl"
                        :value="item.id">
                    <span style="float: left">{{ item.id }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">{{ item.description }}</span>
                </el-option>
            </el-select>
            <span>&nbsp;&nbsp;</span>
            <el-button type="primary" @click="update">更新</el-button>
            <el-button type="primary" @click="dialogVisible=true;type='save';mysqlInfo={}">添加</el-button>
            <el-button type="danger" @click="del">删除</el-button>
        </div>

        <div style="width:100%;display: flex;overflow-x: hidden;overflow-y: hidden;margin-bottom: 50px">
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
<script type="application/javascript" src="/js/monitoring/remote/mysql.js"></script>
</body>
</html>