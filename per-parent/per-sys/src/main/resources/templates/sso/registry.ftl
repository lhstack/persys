<!DOCTYPE html>
<html lang="en">
<head>
    <title>Registry</title>
    <#include "/common/header.ftl"/>
    <#include "/common/avue-common.ftl"/>
    <link rel="stylesheet" type="text/css" href="/css/sso/registry.css"/>
    <link rel="stylesheet" type="text/css" href="/css/font/iconfont.css"/>
</head>
<body>
<div id="app">
    <img class="visible-middle-display-screen f-img-bg" src="/images/log.jpg"/>
    <span class="visible-middle-display-screen welcome">PerSys 用户注册</span>
    <div class="visible-middle-display-screen bgLeft">
        <!-- <img class="zj" src="image/hh.png"/> -->
        <ul>
            <li><i class="iconfont icon-jiumingahelp16"></i></li>
            <li><i class="iconfont icon-quanxianguanli"></i></li>
            <li><i class="iconfont icon-wode"></i></li>
            <li><i class="iconfont icon-hulianwang-"></i></li>
            <li><i class="iconfont icon-sousuo"></i></li>
            <li><i class="iconfont icon-quanxian"></i></li>
            <li><i class="iconfont icon-qiehuanxiangmu"></i></li>
            <li><i class="iconfont icon-jiuyechuangye"></i></li>
        </ul>
    </div>
    <div class="f-bg">
        <!-- 手机端显示 -->
        <div class="visible-middle-screen">
            <div class="header">
                <img src="/images/header.jpg"/>
                <div>
                    <img src="/images/log0.gif"/>
                </div>
                <span>PerSys 单点注册</span>
            </div>
        </div>

        <!-- 电脑小屏显示 -->
        <div class="visible-middle-screen-hide header">
            <!-- <img src="/images/header.jpg"/> -->
            <span>PerSys</span>
        </div>
        <div class="f-form-input">

            <input v-model="user.nickName" class="f-input keyDownLogin" id="nickname" type="text"
                   placeholder="昵称"/>
            <input v-model="user.username" class="f-input keyDownLogin" id="username" type="text"
                   placeholder="用户名"/>

            <input class="f-input keyDownLogin" v-model="user.password" id="password" type="password"
                   placeholder="密码"/>

            <input class="f-input keyDownLogin" v-model="user.email" id="email" type="email"
                   placeholder="邮箱"/>
        </div>
        <div class="f-form-valid">
            <input v-model="email.valid" class='keyDownLogin' id="code" type="text" placeholder="验证码"/>
            <button type="button" @click="sendEmailValid" title="获取邮箱验证码" v-text="validText"></button>
        </div>
        <div class="f-form-other">
            <a href="javascript:;" @click="toReset">忘记密码?</a>
            <a ref="href" href="<#if token?exists && token == ('admin')>/login.html<#else ><#if token?exists && service?exists>/sso/login.html?token=${token}&service=${service} <#else >/login.html</#if></#if>">跳转登录</a>
        </div>
        <div class="f-form-button">
            <button @click="submit">注册</button>
        </div>
        <div class="f-footer visible-middle-screen">
            <a>permission manager system</a>
        </div>
    </div>
    <div class="f-boty-footer visible-max-middle-screen">
        <a>permission manager system</a>
    </div>
</div>
</body>
<script type="application/javascript" src="/js/sso/registry.js"></script>
</html>