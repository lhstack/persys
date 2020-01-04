<!DOCTYPE html>
<html lang="en">
<head>
    <title>SSO Service</title>
    <#include "/common/header.ftl"/>
    <#include "/common/avue-common.ftl"/>
    <style>
        #title {
            width: 100%;
            text-align: center;
            font-size: 20px;
            font-family: "微软雅黑";
            font-weight: bold;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
<div id="app" style="margin-left: 60px;width: 1000px">
    <el-tabs style="width: 870px" v-model="showName" type="card" @tab-click="handleClick">
        <el-tab-pane v-for="(item,index) in apps" :key="index" :name="item.name">
            <span slot="label"><i :class="item.icon"></i> {{item.simpleName}}</span>
        </el-tab-pane>
    </el-tabs>
    <el-button slot="label" @click="showName = 'create';sso={type:'UUID'}" type="primary" size="small" style="float: right;margin-top: -55px;"><i class="el-icon-circle-plus-outline"></i> 创建应用</el-button>


        <#--我的app单点服务-->
    <transition name="el-zoom-in-center">
        <div v-if="showName == 'myApp'" style="width: 100%;" >
            <avue-crud v-model="sso" @row-del="del" :data="myApps" @refresh-change="refresh" :page="pager" :option="option" @row-update="update" @current-change="currentChange" @size-change="sizeChange">
                <template slot="typeForm" slot-scope="scope">
                    <el-select v-model="sso.type" placeholder="token类型">
                        <el-option label="UUID" value="UUID"></el-option>
                        <el-option label="JWT" value="JWT"></el-option>
                    </el-select>
                </template>
                <template slot="jwtSignKeyForm" slot-scope="scope">
                    <el-input :disabled="sso.type != 'JWT'" placeholder="请填写jwtToken签名"  v-model="sso.jwtSignKey"></el-input>
                </template>
                <template slot="isGeneratorPermissionAndRoleForm" slot-scope="scope">
                    <el-switch v-model="sso.isGeneratorPermissionAndRole"></el-switch>
                </template>
            </avue-crud>
        </div>
    </transition>

    <transition name="el-zoom-in-center">
        <div v-if="showName == 'declare'" style="width: 800px;margin-left: 100px">
            使用说明
        </div>
    </transition>


    <#--创建应用-->
    <transition name="el-zoom-in-center">
        <div v-if="showName == 'create'" style="width: 800px;margin-left: 100px">
            <el-form :rules="rules" ref="sso" :model="sso">
                <el-form-item label="应用名称" prop="appName">
                    <el-input v-model="sso.appName" placeholder="请输入应用名称"></el-input>
                </el-form-item>
                <el-form-item label="应用描述" prop="appDescription">
                    <el-input type="textarea" placeholder="请输入应用描述" v-model="sso.appDescription"></el-input>
                </el-form-item>
                <el-form-item label="token生成类别">
                    <el-select style="width: 100%" v-model="sso.type" placeholder="请选token生成类别">
                        <el-option label="UUID" value="UUID"></el-option>
                        <el-option label="JWT" value="JWT"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="是否生成角色和权限">
                    <el-switch v-model="sso.isGeneratorPermissionAndRole"></el-switch>
                </el-form-item>
                <transition name="el-fade-in-linear">
                    <el-form-item v-if="sso.type=='JWT'" label="JwtSignKey">
                        <el-input placeholder="请输入JwtSignKey" v-model="sso.jwtSignKey"></el-input>
                    </el-form-item>
                </transition>
                <el-form-item label="应用主页" prop="index">
                    <el-input v-model="sso.index" placeholder="请输入应用主页: http:// | https://"></el-input>
                </el-form-item>
                <el-form-item label="应用回调地址" prop="service">
                    <el-input type="textarea" placeholder="请输入应用回调地址,多个请用,隔开" v-model="sso.service"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit">立即创建</el-button>
                </el-form-item>
            </el-form>
        </div>
    </transition>

</div>
<script type="text/javascript" src="/js/sso/sso.js"></script>
</body>
</html>