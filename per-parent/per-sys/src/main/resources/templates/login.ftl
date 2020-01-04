<!DOCTYPE html>
<html>
<head>
    <#include "/common/header.ftl"/>
    <title>Login</title>
    <link href="/css/bootstrap-responsive.min.css" type="text/css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/css/login.css" />
</head>
<body>
<img class="visible-phone-display-screen f-img-bg bg-img" src="/images/bg/bg4.jpeg"/>
<div class="f-bg">
    <!-- 手机端显示 -->
    <div class="visible-middle-screen">
        <div class="header">
            <img src="/images/header.jpg"/>
            <div>
                <img src="/images/log0.gif"/>
            </div>
            <span>登录</span>
        </div>
    </div>

    <!-- 超小屏显示 -->
    <div class="visible-middle-screen-hide header">
        <img src="/images/log.jpg"/>
        <span>欢迎登录权限管理系统</span>
    </div>
    <span class="f-info" id="info" style="display: none;">用户名密码输入有误</span>
    <div class="f-form-input">
        <label for="username" class="visible-max-middle-screen">用户名:</label>
        <input onfocus="$('#info').fadeOut(10)" class="f-input keyDownLogin" id="username" type="text" placeholder="用户名/邮箱" />
            <label class="visible-max-middle-screen" for="password"><div>
                <span style="letter-spacing: 1em;">密</span>码:
            </div></label>
        <input class="f-input keyDownLogin" onfocus="$('#info').fadeOut(10)" id="password" type="password" placeholder="密码"/>
    </div>
    <div class="f-form-valid">
        <input onfocus="$('#info').fadeOut(10)" class='keyDownLogin' id="code" type="text" placeholder="验证码"/>
        <img src="/code" onclick="this.src='/code?random=' + Math.random() * 99999" id="codeImg"/>
    </div>
    <div class="f-form-other">
        <a onclick="toReset()" href="javascript:;">忘记密码?</a>
        <a href="/sso/registry?token=admin">立即注册</a>
    </div>
    <div class="f-form-button">
        <button onclick="loginBtn()">登录</button>
    </div>
    <div class="f-footer visible-middle-screen">
        <a>permission manager system</a>
        <#--<a>QQ:1005767007</a>-->
    </div>
</div>
<div class="f-boty-footer visible-max-middle-screen">
    <a>permission manager system</a>
    <#--<a>QQ:1005767007</a>-->
</div>
</body>
<script type="application/javascript" src="/js/login.js" ></script>
</html>