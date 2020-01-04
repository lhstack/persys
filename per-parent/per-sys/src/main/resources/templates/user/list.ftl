<!DOCTYPE html>
<html lang="en">
<head >
    <#include "/common/header.ftl"/>

    <title>User List</title>
    <style>
        .edit-btn{
            color: #F2F2F2;
            font-size: 0.5vw;
            cursor: pointer;
        }
        .edit-btn:hover{
            color: #000000;
            background: #3c763d;
        }
        .edit-btns{
            display: flex;
            justify-content: space-between;
        }
    </style>
</head>
<body>
    <table id="list" lay-filter="table"></table>

    <div id="userFormBody">
        <div class="modal fade" id="editUserModal" style="display: none" tabindex="-1" role="dialog" aria-labelledby="myUserLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" >
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myUserLabel" v-text="userTitle"></h4>
                    </div>
                    <div class="modal-body">
                        <div class="f-group">
                            <div class="form-inline ml-0-3 col-xs-12 col-sm-12 col-lg-12 col-md-12 f-item">
                                <label class="label label-success form-control col-lg-2 col-md-2 col-sm-2 col-xs-2 w10 f-lab">昵称</label>
                                <input type="text" name="nickName" v-model="user.nickName" :disabled="state.nickName"  placeholder="请输入用户昵称" class="form-control col-lg-10 col-md-10 col-sm-10 col-xs-10 w80">
                            </div>
                            <div class="form-inline ml-0-3 col-xs-12 col-sm-12 col-lg-12 col-md-12 f-item">
                                <label class="label label-success form-control col-lg-2 col-md-2 col-sm-2 col-xs-2 w10 f-lab">用户名</label>
                                <input type="text" name="username" v-model="user.username" :disabled="state.username" placeholder="请输入用户名" class="form-control w80">
                            </div>
                            <div class="form-inline ml-0-3 col-xs-12 col-sm-12 col-lg-12 col-md-12 f-item">
                                <label class="label label-success form-control col-lg-2 col-md-2 col-sm-2 col-xs-2 w10 f-lab">密码</label>
                                <input type="password" name="password" v-model="user.password" v-show="modalState=='ADD'" :disabled="state.password" placeholder="请输入密码" class="form-control w80">
                                <input type="password" value="*************"  name="password" v-show="modalState=='UPDATE'" :disabled="state.password" placeholder="请输入密码" class="form-control w80">
                            </div>
                            <div class="form-inline ml-0-3 col-xs-12 col-sm-12 col-lg-12 col-md-12 f-item">
                                <label class="label label-success form-control col-lg-2 col-md-2 col-sm-2 col-xs-2 w10 f-lab">邮箱</label>
                                <input type="email" name="email" v-model="user.email" :disabled="state.email"  placeholder="请输入邮箱" class="form-control w80">
                            </div>
                            <div class="form-inline ml-0-3 col-xs-12 col-sm-12 col-lg-12 col-md-12 f-item">
                                <label class="label label-success form-control col-lg-2 col-md-2 col-sm-2 col-xs-2 w10 f-lab">头像</label>
                                <input type="file" style="display: none" ref="fileUpload"/>
                                <img @click="fileUpload" style="margin-left: 30px;border-radius: 100%;border: none;padding: 0 !important;margin: 0 !important;width: 30px !important; height: 30px !important;" class="icon form-control" :src="user.icon" />
                            </div>
                            <div class="form-inline ml-0-3 col-xs-12 col-sm-12 col-lg-12 col-md-12 f-item">
                                <label class="label label-success form-control col-lg-2 col-md-2 col-sm-2 col-xs-2 f-lab w10">删除</label>
                                <input :checked="user.isDel" @click="user.isDel=true" class="col-lg-push-1 form-control w10" style="width: 15px !important;height: 15px !important;" value="true" name="isDel" type="radio"/>是
                                <input :checked="!user.isDel" @click="user.isDel=false" class="form-control w10" value="false" style="width: 15px !important;height: 15px !important;" name="isDel" type="radio"/>否
                            </div>
                            <div class="form-inline ml-0-3 col-xs-12 col-sm-12 col-lg-12 col-md-12">
                                <label class="label label-success form-control col-lg-2 col-md-2 col-sm-2 col-xs-2 f-lab w10">冻结</label>
                                <input :checked="user.isLock" @click="user.isLock=true" class="col-lg-push-1 form-control w10" style="width: 15px !important;height: 15px !important;" value="true" name="isLock" type="radio"/>是
                                <input :checked="!user.isLock" @click="user.isLock=false" class="form-control w10" value="false" style="width: 15px !important;height: 15px !important;" name="isLock" type="radio"/>否
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-primary" @click="downPage">下一页</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>

        <div class="modal fade" id="editRoleModal" style="display: none" tabindex="-1" role="dialog" aria-labelledby="myRoleLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" >
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myRoleLabel" v-text="roleTitle"></h4>
                    </div>
                    <div class="modal-body">
                        <div id="userForm" class="f-group">
                            <div class="col-xs-12 col-sm-12 col-lg-12 col-md-12 f-item">
                                <label class="f-vh-text-center label-success form-control col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin: 0;margin-bottom: 15px">当前角色</label>
                                <div class="f-scrollBar" style="border: 1px solid antiquewhite;border-radius: 5px">
                                    <div class="alert alert-success alert-dismissable" v-for="item,index in userRoles" :key="index">
                                        <button type="button" class="close" @click="remove(index)">
                                            &times;
                                        </button>
                                        {{item.logogramName}}
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-12 col-lg-12 col-md-12 f-item">
                                <label class="f-vh-text-center label-success form-control col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin: 0;margin-bottom: 15px">所有角色</label>
                                <div class="f-scrollBar" style="border: 1px solid antiquewhite;border-radius: 5px">
                                    <div @click="add(item,index)" class="alert alert-success alert-dismissable" style="cursor: pointer" v-for="item,index in roles" :key="index">
                                        {{item.logogramName}}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-default" onclick="$('#editRoleModal').modal('hide');$('#editUserModal').modal('show')">上一页</button>
                        <button type="button" class="btn btn-primary" @click="submit">提交</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
    </div>
    <!--                          update form                        -->




</body>
<script type="text/html" id="toolBar">
    <input type="text" id="searchText" class="layui-input layui-btn-sm" style="width: 300px;float: left;height: 30px;margin-right: 5px" placeholder="请输入昵称，用户名或者邮箱进行模糊查询"/>
    <button id="searchTime" class="layui-btn layui-btn-primary layui-btn-sm" onclick="searchTime()">选择时间区间</button>
    <button id="search" class="layui-btn layui-btn-normal layui-btn-sm" onclick="search()">搜索</button>
    <input class="layui-input" type="hidden" id="times"/>
    <a class="layui-btn layui-btn-sm" lay-event="add">新增</a>
    <a class="layui-btn layui-btn-danger layui-btn-sm" lay-event="delSelect">删除选中的行</a>
</script>
<script type="text/html" id="rightTool">
    <div class="edit-btns">
        <a class="layui-btn layui-btn-xs layui-btn-warm edit-btn" lay-event="edit">修改</a>
        <a class="layui-btn layui-btn-danger layui-btn-xs edit-btn" lay-event="del">删除</a>
    </div>
</script>
<script type="text/javascript" src="/js/user/list.js"></script>
</html>