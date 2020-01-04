<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/common/header.ftl"/>
    <#include "/common/avue-common.ftl"/>
    <title>Menu List</title>
    <link rel="stylesheet" type="text/css" href="/css/menu/index.css">
</head>
<body>
<div id="app">
    <span class="el-button--small" style="font-size: 20px;font-weight: bold;color: #409EFF;margin-bottom: 15px;display: inline-block">菜单列表</span>
    <div style="width: 100%;">
        <div style="width: 40%;float: left">
            <el-row style="margin-bottom: 20px;" class="el-button--small">
                <el-button style="width: 60px" size="mini" type="primary" @click="form={parentId:0,sortBy:0,enable: true,isParent:false};permissionTags=[];perMap={}">添加</el-button>
            </el-row>
            <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick">
            <span class="custom-tree-node" slot-scope="{ node, data }">
                <span v-text="node.label"></span>
                <span>
                  <el-button
                          type="text"
                          size="mini"
                          @click.stop="add(data,node)" :disabled="!data.isParent || data.parentId != 0">
                    添加
                  </el-button>
                    <el-button
                            type="text"
                            size="mini"
                            @click.stop="edit(data,node)">
                    编辑
                  </el-button>
                  <el-button
                          type="text"
                          size="mini"
                          @click.stop="remove(data,node)">
                    删除
                  </el-button>
                </span>
            </span>
            </el-tree>
        </div>
        <div style="border-left: 1px solid #409EFF;height: 600px;width: 1px;float: left;margin-left: 5%">

        </div>
        <div style="width: 40%;float: left;margin-left: 5%">
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="菜单名称">
                    <el-input v-model="form.menuName"></el-input>
                </el-form-item>
                <el-form-item label="URL">
                    <el-input v-model="form.href"></el-input>
                </el-form-item>
                <el-form-item label="菜单图标">
                    <el-input v-model="form.icon" style="width: 90%"></el-input>
                    <span @click.stop="iconClick" style="cursor: pointer;">
                        <avue-avatar style="background:#409EFF;width: 32px;color: #fff;float: right;display: flex;flex-direction: column;justify-content: center;margin-top: 2px;"><icon :class="form.icon" ></icon></avue-avatar>
                    </span>
                </el-form-item>
                <el-form-item label="是否可见">
                    <el-switch v-model="form.enable"></el-switch>
                </el-form-item>
                <el-form-item label="父节点">
                    <el-switch v-model="form.isParent"></el-switch>
                </el-form-item>
                <el-form-item label="父菜单" v-if="!form.isParent">
                    <el-select v-model="form.parentId" placeholder="请选择父级菜单">
                        <el-option label="无" :value="0"></el-option>
                        <el-option :label="item.menuName" :value="item.id" v-for="item,index in pList" :key="index" v-if="item.menuName != form.menuName && item.isParent"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item  label="绑定权限">
                    <el-tag
                            :key="index"
                            v-for="permission,index in permissionTags"
                            closable
                            :disable-transitions="false"
                            @close="tagClose(index,permission)" :title="permission.logogramName">
                        {{permission.logogramName | tagFilter}}
                    </el-tag>
                    <el-button type="primary" size="small" @click="permissionState = true">添加权限</el-button>
                </el-form-item>
                <el-form-item label="排序字段">
                    <el-input v-model="form.sortBy" min="0" @blur="checkSortBy" title="数值越小,越靠前" placeholder="数值越大,越靠前" type="number"></el-input>
                </el-form-item>

                <el-button type="primary" @click="onSubmit">保存</el-button>
            </el-form>
        </div>
    </div>
    <icons-template :dialog-visible="dialogVisible" :icons="icons" @handler-click-result="handlerIconClick" @handler-close-rollback="close">
            <span slot="footer" class="dialog-footer" slot-scope="{icon}">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="resultIcon(icon)">确 定</el-button>
            </span>
    </icons-template>
    <el-dialog
            title="绑定权限"
            :visible.sync="permissionState"
            width="50%">
        <div style="width: 100%;height: 100%;overflow-y: auto">
            <el-tag
                    :key="index"
                    v-for="permission,index in allPermission"
                    :disable-transitions="false"
                    :style="'cursor:pointer;margin: 6px;width: 170px;' + active(permission)"
                    :title="permission.logogramName"
                    @click="checkTagState(permission)"
            >
                {{permission.logogramName | tagFilter}}
            </el-tag>
        </div>
        <span slot="footer" class="dialog-footer">
    <el-button @click="permissionState = false">取 消</el-button>
    <el-button type="primary" @click="permissionState = false">确 定</el-button>
  </span>
    </el-dialog>
</div>
</body>
<script type="text/javascript" src="/js/global-data.js"></script>
<script type="text/javascript" src="/js/components/icons.js"></script>
<script type="text/javascript" src="/js/menu/data.js"></script>
<script type="text/javascript" src="/js/menu/list.js"></script>
</html>