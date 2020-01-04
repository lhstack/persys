<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <#include "/common/header.ftl"/>
    <#include "/common/avue-common.ftl"/>
    <title>Per sys</title>
    <link rel="stylesheet" type="text/css" href="/css/index/index.css">
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin" id="app">
    <div class="layui-header">
        <div class="layui-logo" style="font-size: 16px;color: #f3d19e"><a style="color: #deded1" @click="toBody({href:'/page/home',menuName:'首页'})" href="javascript:;">Per Sys</a></div>
        <ul class="layui-nav layui-layout-left">
            <li class="layui-nav-item"><a href="javascript:;"><i class="layui-icon layui-icon-shrink-right"></i></a></li>
            <li class="layui-nav-item"><a @click="toBody({href:'${admin_url}',menuName:'控制台'})" href="javascript:;" target="body">控制台</a></li>
            <#--<li class="layui-nav-item"><a href="">商品管理</a></li>
            <li class="layui-nav-item"><a href="">用户</a></li>
            <li class="layui-nav-item">
                <a href="javascript:;">其它系统</a>
                <dl class="layui-nav-child">
                    <dd><a href="">邮件管理</a></dd>
                    <dd><a href="">消息管理</a></dd>
                    <dd><a href="">授权管理</a></dd>
                </dl>
            </li>-->
        </ul>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;" style="font-size: 16px;">
                    <img :src="user.icon" class="layui-nav-img">
                    {{user.nickName}}
                </a>
                <dl class="layui-nav-child">
                    <dd><a @click="toBody({href:'/page/user@info',menuName:'基本资料'})" href="javascript:;" target="body">基本资料</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item"><a href="javascript:;" onclick="logout()">退出登录</a></li>
        </ul>
    </div>

    <div class="layui-side layui-bg-black" lay-allowClose="true">
        <div class="layui-side-scroll">
            <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
            <ul class="layui-nav layui-nav-tree">
                <li class="layui-nav-item" v-for="mItem,index in menus" :key="index">
                    <a class="" v-if="mItem.isParent" href="javascript:;" :title="mItem.menuName"><i :class="mItem.icon">&nbsp;&nbsp;</i><span v-text="mItem.menuName"></span></a>
                    <a class="" v-else href="javascript:;" @click="toBody(mItem)" :title="mItem.menuName"><i :class="mItem.icon">&nbsp;&nbsp;</i><span v-text="mItem.menuName"></span></a>
                    <dl class="layui-nav-child" v-if="mItem.isParent">
                        <dd v-for="childItem,index in mItem.child" :key="index"><a href="javascript:;" @click="toBody(childItem)"><i :class="childItem.icon">&nbsp;&nbsp;</i>{{childItem.menuName}}</a></dd>
                    </dl>
                </li>

            </ul>
        </div>
    </div>

    <div class="layui-body">
        <div class="f-tab-body">
            <ul class="f-tab">
                <span class="f-tab-item-header">
                    <i class="layui-icon layui-icon-home"></i>
                </span>
                <span v-for="crumb,index in crumbs" :class="{'f-tab-active':crumbsIndex==index,'f-tab-item':true}">
                    <li @click="crumbClick(crumb)" :key="index" v-text="crumb.menuName">用户列表</li><i class="layui-icon layui-icon-close f-close" @click="closeTab(index)"></i>
                </span>
            </ul>
        </div>
        <!-- 内容主体区域 -->
        <div id="loading_body" style="height: 92% !important;padding-left: 15px;padding-top: 15px;">
            <iframe style="width: 100%;min-height:100%;border: none;" name="body" id="body" src="/page/home"></iframe>
        </div>
    </div>

    <div class="layui-footer" style="color: #01AAED">
        <span class="layui-layout-left">
            permission manager system
        </span>
        <span class="layui-layout-right" style="padding-right: 5vw;">

        </span>
    </div>
</div>
<script src="/js/index/data.js" type="application/javascript"></script>
<script src="/js/index/index.js" type="application/javascript"></script>
</body>
</html>