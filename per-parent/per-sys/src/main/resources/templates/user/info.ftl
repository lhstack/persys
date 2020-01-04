<!DOCTYPE html>
<html lang="en">
<head >
    <#include "/common/header.ftl"/>
    <#include "/common/avue-common.ftl"/>

    <title>User Info</title>
    <link rel="stylesheet" href="/css/user/info.css">
</head>
<body>

<div id="app" style="width: 900px;display: flex;justify-content: space-between;margin-top: 60px">
    <el-form ref="form" style="width: 420px" :model="form" label-width="80px">
        <div id="title">
            用户信息
        </div>
        <el-form-item label="用户昵称">
            <el-input v-model="form.nickName" placeholder="请填写用户昵称"></el-input>
        </el-form-item>
        <el-form-item label="用户名">
            <el-input disabled :value="form.username"></el-input>
        </el-form-item>
        <el-form-item label="注册时间">
            <el-col :span="15">
                <el-date-picker type="datetime" disabled :value="form.createTime" style="width: 100%;"></el-date-picker>
            </el-col>
        </el-form-item>
        <el-form-item label="邮箱">
            <div style="display: flex">
                <el-input disabled v-model="form.email" ></el-input><el-button type="warning" @click="isEmailDialogVisible = true;isPasswordDialogVisible=false;editTitle='修改邮箱'" plain>换绑</el-button>
            </div>
        </el-form-item>
        <el-form-item label="密码">
            <div style="display: flex">
                <el-input disabled value="**********" ></el-input><el-button type="warning" @click="isEmailDialogVisible = false;isPasswordDialogVisible=true;editTitle='修改密码'" plain>修改</el-button>
            </div>
        </el-form-item>
        <el-form-item label="头像">
            <div style="height: 100%;display: flex;flex-direction: column;justify-content: center;float: left">
                <span @click="upload"><el-avatar :size="35" id="icon" :src="form.icon" style="cursor: pointer;"></el-avatar></span>
            </div>
            <div style="height: 100%;display: flex;flex-direction: column;justify-content: center;float: left;margin-left: 15px">
                <el-button type="primary" plain @click="upload" size="small">上传头像</el-button>
            </div>
            <input id="fileUpload" type="file" ref="upload" style="display: none" />
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="onSubmit">保存</el-button>
        </el-form-item>
    </el-form>

<#--  换绑邮箱dialog  -->
    <el-form v-if="isEmailDialogVisible" style="width: 420px" label-width="80px">
        <div id="title" v-text="editTitle">

        </div>
        <el-form-item label="新邮箱">
            <div style="margin-bottom: 10px">
                <el-input v-model="email.newEmail1" placeholder="请填写新邮箱"></el-input>
            </div>
            <div>
                <el-input v-model="email.newEmail2" placeholder="请再次确认新邮箱"></el-input>
            </div>
        </el-form-item>
        <el-form-item label="验证码">
            <div style="display: flex;">
                <el-input v-model="email.valid" placeholder="请填写验证码"></el-input>
                <el-button type="primary" style="width: 160px" @click="sendValidCode" v-text="validText" plain></el-button>
            </div>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="onSubmitEmail">更新</el-button>
            <el-button @click="cancelEmailDialog" >取消</el-button>
        </el-form-item>
    </el-form>


<#--  修改密码 dialog  -->
    <el-form v-if="isPasswordDialogVisible" style="width: 420px" label-width="80px">
        <div id="title" v-text="editTitle">

        </div>
        <el-form-item label="原始密码">
            <div>
                <el-input type="password" v-model="pass.oldPass" placeholder="请填写原始密码"></el-input>
            </div>
        </el-form-item>
        <el-form-item label="新密码">
            <div style="margin-bottom: 10px">
                <el-input type="password" v-model="pass.newPass1" placeholder="请填写新密码"></el-input>
            </div>
            <div>
                <el-input type="password" v-model="pass.newPass2" placeholder="请再次确认新密码"></el-input>
            </div>
        </el-form-item>
        <el-form-item label="验证码">
            <div style="display: flex;">
                <el-input v-model="pass.valid" placeholder="请填写验证码"></el-input>
                <el-button type="primary" style="width: 160px" @click="sendEditPassValidCode" v-text="validText" plain></el-button>
            </div>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="onSubmitPass">更新</el-button>
            <el-button @click="cancelPassDialog" >取消</el-button>
        </el-form-item>
    </el-form>
</div>
</body>
<script type="text/javascript" src="/js/user/info.js"></script>
</html>