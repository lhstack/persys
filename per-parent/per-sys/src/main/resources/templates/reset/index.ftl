<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/common/header.ftl"/>
    <#include "/common/avue-common.ftl"/>
    <meta charset="UTF-8">
    <title>找回密码</title>
    <link rel="stylesheet" type="text/css" href="/css/reset/index.css">
</head>
<body>
    <div id="app">

        <div class="box" v-if="page == 1">
            <div class="box_header">
                <span>找回密码</span>
            </div>
            <div v-text="msg" class="box_msg">msg</div>
            <div class="box_input_pass">
                <label for="newPass">新密码:</label>
                <input id="newPass" @keydown.enter="submit" @focus="msg=''"  v-model="newPass1" type="password" placeholder="请输入新密码"/>
            </div>
            <div class="box_input_pass">
                <label for="newPass2">确认密码:</label>
                <input id="newPass2" @keydown.enter="submit" @focus="msg=''" v-model="newPass2" type="password" placeholder="请再次确认密码" />
            </div>
            <div class="box_other">
                <a href="javascript:;" @click="toLogin">跳转登陆</a>
                <a href="javascript:;" @click="toRegistry">立即注册</a>
            </div>
            <div class="box_footer_pass">
                <button @click="page = 0;msg=''">上一步</button>
                <button @click="submit">提交</button>
            </div>
        </div>
        <div class="box" v-if="page == 0">
            <div class="box_header">
                <span>找回密码</span>
            </div>
            <div v-text="msg" class="box_msg">msg</div>
            <div class="box_input">
                <label for="email">邮箱:</label>
                <input id="email" @keydown.enter="next" @focus="msg=''"  v-model="email" placeholder="请输入邮箱"/>
                <div>
                    <button @click="send" v-text="validText">发送</button>
                </div>
            </div>
            <div class="box_input">
                <label for="valid">验证码:</label>
                <input id="valid" @keydown.enter="next" @focus="msg=''" v-model="validCode" type="text" placeholder="请输入验证码" />
            </div>
            <div class="box_other">
                <a href="javascript:;" @click="toLogin">跳转登陆</a>
                <a href="javascript:;" @click="toRegistry">立即注册</a>
            </div>
            <div class="box_footer">
                <button @click="next">下一步</button>
            </div>
        </div>
    </div>
    <script type="application/javascript" src="/js/reset/index.js"></script>
</body>
</html>