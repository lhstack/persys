<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/common/header.ftl"/>
    <#include "/common/avue-common.ftl"/>
    <link href="/css/permission/index.css" type="text/css" rel="stylesheet"/>
    <title>Permission List</title>
</head>
<body>
<div id="app">

    <avue-card :option="option" :data="data" @row-click="edit" @row-add="add" style="height: 650px">
        <template slot="menu" slot-scope="scope">
            <span @click.stop="edit(scope.row,scope.index)">修改</span>
            <span @click.stop="del(scope.row,scope.index)">删除</span>
        </template>
    </avue-card>
    <el-dialog
            :title="title"
            :visible.sync="dialogVisible"
            width="40%"
    >
        <el-form ref="form" :model="currentData" label-width="80px">
            <el-form-item label="权限名称">
                <el-input v-model="currentData.title" :disabled="title=='修改'" placeholder="规则/^[A-Z]*_?[A-Z]([A-Z]|_)*$/g 如USER_ADMIN"></el-input>
            </el-form-item>
            <el-form-item label="权限介绍">
                <el-input v-model="currentData.text" placeholder="介绍角色作用"></el-input>
            </el-form-item>
            <el-form-item label="权限头像">
                <el-row class="demo-avatar demo-basic">
                    <el-col :span="12">
                        <div class="demo-basic--circle">
                            <div class="block" @click="processUpload">
                                <el-avatar :size="50" id="currentImg" :src="currentData.img" style="cursor: pointer;"></el-avatar>
                                <input @change="fileUpload" type="file" style="display: none" id="fileUpload"/>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="submit">保 存</el-button>
        </span>
    </el-dialog>

    <div id="pager">

    </div>
</div>
</body>
<script type="text/javascript" src="/js/permission/list.js">

</script>
</html>