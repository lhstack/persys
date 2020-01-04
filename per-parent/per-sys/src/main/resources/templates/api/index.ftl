<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>api info</title>
    <script type="text/javascript" src="/js/FileSaver.min.js"></script>
    <script type="text/javascript" src="/js/xlsx.full.min.js"></script>
    <#include "/common/header.ftl"/>
    <#include "/common/avue-common.ftl"/>
</head>
<body>
<div id="app">
    <avue-crud id="table" @search-reset="reset" @row-update="update" @search-change="searchChange" v-model="api" ref="crud" :data="list" @refresh-change="refreshChange"
               @size-change="sizeChange" :option="options" :page="pager" @current-change="currentChange">
        <template slot="requestMethod" slot-scope="scope">
            <span v-text="scope.row.requestMethod"></span>
        </template>
        <template slot="pattenUrl" slot-scope="scope">
            <span :title="scope.row.pattenUrl"
                  v-text="scope.row.pattenUrl.length > 30 ? scope.row.pattenUrl.substr(0,27) + '...' : scope.row.pattenUrl"></span>
        </template>
        <template slot="handlerMethod" slot-scope="scope">
            <span :title="scope.row.handlerMethod"
                  v-text="scope.row.handlerMethod.length > 30 ? scope.row.handlerMethod.substr(0,27) + '...' : scope.row.handlerMethod"></span>
        </template>
        <template slot="authorityType" slot-scope="scope">
            <span v-text="scope.row.authorityType == 0 ? 'hasRole' : scope.row.authorityType == 1 ? 'hasPermission' : scope.row.authorityType == 2 ? 'anyAuthority' : scope.row.authorityType == 3 ? 'allAuthority' :'无'"></span>
        </template>
        <template slot-scope="scope" slot="menu">
            <el-link style="margin-left: 5px;" icon="el-icon-edit" @click="showPermission(scope.row)" type="primary"
                     :underline="false">权限
            </el-link>
            <el-link style="margin-left: 5px;" icon="el-icon-edit" type="primary" @click="showRole(scope.row)" :underline="false">角色</el-link>
        </template>
        <el-form-item slot="search">
            <el-col :md="6" :xs="24">
                <el-form-item label="注释信息">
                    <el-input placeholder="请输入待查询的注释信息" size="small" v-model="apiInfoSearch.description" />
                </el-form-item>
            </el-col>
            <el-col :md="6" :xs="24">
                <el-form-item label="请求方法">
                    <el-input placeholder="请输入待查询的请求方法" size="small" v-model="apiInfoSearch.requestMethod" />
                </el-form-item>
            </el-col>
            <el-col :md="6" :xs="24">
                <el-form-item label="请求url">
                    <el-input placeholder="请输入待查询的url" size="small" v-model="apiInfoSearch.pattenUrl" />
                </el-form-item>
            </el-col>
            <el-col :md="6" :xs="24">
                <el-form-item label="鉴权类型">
                    <el-select v-model="apiInfoSearch.authorityType" placeholder="请选择鉴权类型">
                        <el-option
                                :value="-1"
                                label="all">
                            <span>all</span>
                        </el-option>
                        <el-option
                                :value="0"
                                label="hasRole">
                            <span>hasRole</span>
                        </el-option>
                        <el-option
                                :value="1"
                                label="hasPermission">
                            <span>hasPermission</span>
                        </el-option>
                        <el-option
                                :value="2"
                                label="anyAuthority">
                            <span>anyAuthority</span>
                        </el-option>
                        <el-option
                                :value="3"
                                label="allAuthority">
                            <span>allAuthority</span>
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-col>
        </el-form-item>
        <template slot="authorityTypeForm" slot-scope="scope">
            <el-select v-model="api.authorityType" placeholder="请选择鉴权类型">
                <el-option
                        :value="0"
                        label="hasRole">
                    <span>hasRole</span>
                </el-option>
                <el-option
                        :value="1"
                        label="hasPermission">
                    <span>hasPermission</span>
                </el-option>
                <el-option
                        :value="2"
                        label="anyAuthority">
                    <span>anyAuthority</span>
                </el-option>
                <el-option
                        :value="3"
                        label="allAuthority">
                    <span>allAuthority</span>
                </el-option>
            </el-select>
        </template>
    </avue-crud>
    <el-dialog
            title="接口权限"
            :visible.sync="dialogShowPermission"
            width="35%">
        <el-transfer
                v-model="permission.apiPermissions"
                :props="{
                  key: 'id',
                  label: 'logogramName'
                }"
                :data="permission.allPermissions">
        </el-transfer>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dialogShowPermission = false">取 消</el-button>
            <el-button type="primary" @click="savePermission">确 定</el-button>
        </span>
    </el-dialog>
    <el-dialog
            title="接口角色"
            :visible.sync="dialogShowRole"
            width="35%">
        <el-transfer
                v-model="role.apiRoles"
                :props="{
                  key: 'id',
                  label: 'logogramName'
                }"
                :data="role.allRoles">
        </el-transfer>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dialogShowRole = false">取 消</el-button>
            <el-button type="primary" @click="saveRole">确 定</el-button>
        </span>
    </el-dialog>

</div>
</body>
<script type="application/javascript" src="/js/api/index.js"></script>
</html>