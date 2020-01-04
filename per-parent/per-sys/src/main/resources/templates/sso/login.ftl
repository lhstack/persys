<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login</title>
    <#include "/common/header.ftl"/>
    <#include "/common/avue-common.ftl"/>
    <link rel="stylesheet" type="text/css" href="/css/sso/login.css" />
    <link rel="stylesheet" type="text/css" href="/css/font/iconfont.css"/>
</head>
<body>
    <div id="app">
        <img class="visible-middle-display-screen f-img-bg" src="/images/log.jpg"/>
        <span class="visible-middle-display-screen welcome">PerSys 单点登陆</span>
        <div class="visible-middle-display-screen bgLeft" >
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
                    <span>PerSys 单点登陆</span>
                </div>
            </div>

            <!-- 电脑小屏显示 -->
            <div class="visible-middle-display-screen header">
                <!-- <img src="./image/header.jpg"/> -->
                <span>PerSys</span>
            </div>

            <form class="f-form-input" ref="form" action="/sso/login" method="post">
                <input class="f-input" name="username" @keydown.enter="submit" v-model="user.username" @input.stop="validUsername" type="text" placeholder="用户名/邮箱" />
                <input class="f-input" name="password" @keydown.enter="submit" v-model="user.password" @input.stop="validPassword" type="password" placeholder="密码"/>
                <div class="f-form-valid">
                    <input @keydown.enter="submit" name="validCode" v-model="user.validCode" @input.stop="validCodeValid" type="text" placeholder="验证码"/>
                    <img :src="code" @click="code = code + '?random=' + Math.random() * 10000"/>
                </div>
            </form>

            <div class="f-form-other">
                <a @click="toReset" href="javascript:;">忘记密码?</a>
                <a href="/sso/registry.html">立即注册</a>
            </div>
            <div class="f-form-button">
                <button @click="submit">登录</button>
            </div>
            <span class="f-info" v-text="info"></span>
            <div class="f-footer visible-middle-screen">
                <a>permission manager system</a>
            </div>
        </div>
        <div class="f-boty-footer visible-middle-display-screen ">
            <a>permission manager system</a>
        </div>
    </div>
    <script>
        var staticInfo = "<#if infoMsg?exists>${infoMsg}><#else ></#if>";
    </script>
    <script type="text/javascript" src="/js/sso/login.js"></script>
</body>
</html>