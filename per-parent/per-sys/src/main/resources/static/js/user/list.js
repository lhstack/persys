var globalTableUrl = "/user/page";
layui.use(["table","laydate"],function(){
   var table = window.$$table = layui.table;
    var laydate = window.$$date = layui.laydate;
    /*laydate.render({
        elem: '#times', //指定元素
        type:"datetime",
        range:"@",
        format:"yyyy-MM-dd HH:mm:ss",
        show: true,
        closeStop: '#searchTime'
    });*/
    //第一个实例
    table.render({
        elem: '#list'
        ,height: 600
        , width: "100vw"
        ,url: globalTableUrl //数据接口
        ,method:"post"
        ,page: true //开启分页
        ,skin: true
        ,even: true
        ,size: "sm"
        ,toolbar: "#toolBar"
        ,limit:20
        ,id: "tableList"
        ,cols: [[
            {type:"checkbox",width:50,title:"选中", fixed: 'left'}
            ,{field: 'id', title: 'ID', width:80, sort: true}
            ,{field: 'nickName', title: '昵称', width:120, sort: true}
            ,{field: 'username', title: '用户名', width:160, sort: true}
            ,{field: 'email', title: '邮箱', width:200, sort: true}
            ,{field: 'icon', title: '头像', width:300,hide: true}
            ,{field: 'createTime', title: '创建时间', width: 260,sort: true}
            , {
                title: '是否删除',
                width: 160,
                sort: true,
                field: "isDel",
                hide: true
            },{
                title: '是否冻结',
                width: 160,
                sort: true,
                field: "isLock",
                hide: true
            },
            {
                toolbar:"#rightTool",title:"操作",width:120,fixed: 'right'
            }
        ]]
    });

    table.on('toolbar(table)',function(obj){
        if(obj.event == 'delSelect'){
            layer.confirm('确认删除吗?', {icon: 3, title:'警告'}, function(index){
                var status = table.checkStatus('tableList');
                var ids = "";
                status.data.forEach(function(item){
                    ids += item.id + ",";
                });
                ids = ids.substr(0,ids.length - 1);
                if(ids.length > 0){
                    requestDel({
                        url:"/user/del/all/" + ids
                    }).catch(function(err){
                        layer.msg('删除失败', {icon: 5});

                    }).then(function(res){
                        if(res.code == 200){
                            layer.msg(res.msg, {icon: 6});
                            table.reload("tableList")
                        }else{
                            layer.msg('删除失败', {icon: 5});
                        }
                    })
                }else{
                    layer.msg("请选择要批量删除的行",{icon:5})
                }
                layer.close(index);
            });

        }
        else if(obj.event == 'add'){
            popupAddComponent();
        }
    });
    //监听事件
    table.on('tool(table)', function(obj){
        if(obj.event == 'del'){
            layer.confirm('确认删除吗?', {icon: 3, title:'警告'}, function(index){
                layer.close(index);
                var id = obj.data.id;
                requestDel({
                    url:"/user/del/" + id,
                }).then(function(res) {
                    if(res.code == 200){
                        obj.del();
                        layer.msg(res.msg, {icon: 6});
                    }else{
                        layer.msg(res.msg, {icon: 5});
                    }


                }).catch(function(res){
                    layer.msg('删除失败', {icon: 5});
                });

            });
        }else if(obj.event == 'edit'){
            popupEditComponent(obj);
        }
    });
});

function initParameter(searchText,times){
    var parameters = {};
    if(searchText.trim().length > 0){
        parameters.search = searchText;
    }else{
        parameters.search = ""
    }
    if(times.trim().length > 0){
        var timesArr = times.split("@");
        var startTime = timesArr[0].trim();
        var endTime = timesArr[1].trim();
        parameters.startTime = startTime;
        parameters.endTime = endTime;
    }else{
        parameters.startTime = "";
        parameters.endTime = "";
    }
   return parameters;
}

/**
 * 搜索按钮点击事件
 */
function search(){
    var searchText = $("#searchText").val();
    var times = $("#times").val();
    var parameters = initParameter(searchText,times);
    $$table.reload("tableList",{
        url:globalTableUrl,
        method:"post",
        where:parameters,
        done:function(a,b,c){
            //数据回显
            $("#searchText").val(searchText);
            $("#times").val(times);
            if(times.length > 0){
                $("#searchTime").text(times)
            }

        }
    })
}


function searchTime(){
    $("#times").val("");
    $$date.render({
        elem: '#times', //指定元素
        type:"datetime",
        range:"@",
        format:"yyyy-MM-dd HH:mm:ss",
        show: true,
        closeStop:"#searchTime",
        done: function(value, date, endDate){
            $("#times").val(value);
            if(value.length < 1){
                $("#searchTime").text("点击选择时间")
            }else{
                $("#searchTime").text(value)
            }
        }
    });
}

window.vm = new Vue({
    el:"#userFormBody",
    data: function(){
        return {
            user: {},
            userTitle: "",
            roleTitle: "",
            state: {},
            userRoles: [],
            roles: [],
            modalState:""
        }
    },
    methods:{
        fileUpload:function(){
            var upload = this.$refs.fileUpload;
            upload.click();
            upload.onchange = function(item){
                var file = item.target.files[0];
                var size = file.size / 1024;
                var extension = file.name.substr(file.name.lastIndexOf('.') + 1);
                if(size <= 5000){
                    if(vm.isPhoto(extension)){
                        vm.upload(file);
                    }else{
                        layer.msg('请上传图片文件', {icon: 3});
                    }
                }else{
                    layer.msg('文件最大不能超过5000KB,你的文件大小为' + size + "KB", {icon: 6});
                }
            }
        },
        isPhoto:function(extName){
            extName = extName.toLowerCase();
            switch (extName) {
                case "png":return true;
                case "jpg":return true;
                case "jpeg":return true;
                case "gif":return true;
                default:
                    return false;
            }
        },
        upload:function(file){
            var formData = new FormData();
            formData.append("file",file);
            $.ajax({
                url:"/file/upload",
                data:formData,
                processData : false, // 使数据不做处理
                contentType : false,
                type: "POST",
                dataType:"json",
                success:function(res){
                    vm.$data.user.icon = res.path;
                },
                error:function(err){
                    layer.msg("头像上传失败",{icon:3})
                }
            })
        },
        downPage:function(){
            $("#editUserModal").modal("hide");
            this.loadRole();
            setTimeout(function(){
                $("#editRoleModal").modal("show");
            },500)
        },
        submit:function(){
            if(this.validUserInfo() && this.validRoleInfo()){
                if(this.modalState == "UPDATE"){
                    $.ajax({
                        url:"/user/update/?rids=" + this.userRoles.map(function(item){
                            return item.id;
                        }).join(","),
                        type:"POST",
                        data:this.user,
                        dataType:"json",
                        success:function(res){
                            if(res.code == 200){
                                layer.msg(res.msg,{icon:6});
                                $("#editRoleModal").modal("hide");
                                $$table.reload("tableList")
                            }else{
                                layer.msg(res.msg,{icon:5})
                            }
                        },
                        error:function(err){
                            layer.msg(err.responseJSON.msg,{icon:5})
                        }
                    });
                }else if(this.modalState == 'ADD'){
                    $.ajax({
                        url:"/user/add/?rids=" + this.userRoles.map(function(item){
                            return item.id;
                        }).join(","),
                        type:"POST",
                        data:this.user,
                        dataType:"json",
                        success:function(res){
                            if(res.code == 200){
                                layer.msg(res.msg,{icon:6});
                                $("#editRoleModal").modal("hide");
                                $$table.reload("tableList")
                            }else{
                                layer.msg(res.msg,{icon:5})
                            }
                        },
                        error:function(err){
                            layer.msg(err.responseJSON.msg,{icon:5})
                        }
                    });
                }
            }

        },
        validUserInfo:function(){
            var strNameRegex = /^[\w]{5,18}$/g;
            var emailRegex = /^([0-9A-Za-z\-_\.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g;
            if(this.modalState == "ADD"){
                if(this.user.nickName.trim().length < 5 || this.user.nickName.trim().length > 18){
                    layer.msg("昵称长度必须在5-18位之间",{icon:5});
                    return false;
                }
                if(!strNameRegex.test(this.user.username)){
                    layer.msg("用户名必须为长度5-18位之间的数字和字母",{icon:5});
                    return false;
                }
                if(!/^[a-zA-Z0-9]{5,12}$/g.test(this.user.password)){
                    console.log(this.user.password)
                    layer.msg("密码必须为长度5-18位之间的数字和字母",{icon:5});
                    return false;
                }
                if(!emailRegex.test(this.user.email)){
                    layer.msg("邮箱不合法",{icon:5});
                    return false;
                }
            }else if(this.modalState == 'UPDATE'){
                if(this.user.nickName.length < 5 || this.user.nickName.length > 12){
                    layer.msg("昵称长度必须在5-12位之间",{icon:5});
                    return false;
                }
                if(!emailRegex.test(this.user.email)){
                    layer.msg("邮箱不合法",{icon:5});
                    return false;
                }
            }
            return true;
        },
        validRoleInfo:function(){
          if(this.userRoles.length < 1){
              layer.msg("用户最少需要一个角色",{icon:5})
              return false;
          }
          return true;
        },
        loadRole:function(){
            if(this.modalState == "UPDATE"){
                get({
                    url:"/user/role/" + vm.$data.user.id
                }).then(function(res){
                    if(res.code == 0){
                        vm.$data.userRoles = res.data || [];
                    }else{
                        layer.msg(res.msg,{icon:5})
                    }
                    get({
                        url:"/user/roles"
                    }).then(function(res){
                        if(res.code == 0){
                            vm.$data.roles = res.data.filter(function(item){
                                var arr = vm.$data.userRoles.filter(function(em){
                                    return item.roleName == em.roleName;
                                });
                                return arr.length == 0;
                            });
                        }else{
                            layer.msg(res.msg,{icon:5})
                        }
                    }).catch(function(err){
                        console.log(err,"err")
                    });
                }).catch(function(err){
                    console.log(err,"err")
                });
            }else{
                get({
                    url:"/user/roles"
                }).then(function(res){
                    if(res.code == 0){
                        vm.$data.roles = res.data;
                    }else{
                        layer.msg(res.msg,{icon:5})
                    }
                }).catch(function(err){
                    console.log(err,"err")
                });
            }

        },
        add:function(item,index){
            var flag = false;
            this.$data.userRoles.forEach(function(role){
                if(!flag){
                    if(role.roleName == item.roleName){
                        flag = true;
                    }
                }
            });
            if(!flag){
                this.$data.userRoles.unshift(item);
                this.$data.roles.splice(index,1)
            }
        },
        remove:function(index){
            var arr = this.roles.filter(function(item){
                return item.roleName == vm.$data.userRoles[index].roleName;
            });
            if(arr.length == 0){
                this.roles.unshift(this.userRoles[index])
            }
            this.userRoles.splice(index,1);
        }
    }
});
function popupEditComponent(obj){
    vm.user = obj.data;
    vm.user.password = "**********";
    vm.state = {
        nickName: false,
        username: true,
        password: true,
        email: false,
    };
    vm.userRoles = []
    vm.modalState = "UPDATE"
    vm.userTitle = "更新用户";
    vm.roleTitle = "更新角色";
    $("#editUserModal").modal("show");
}
function popupAddComponent() {
    vm.user = {
        icon:"/images/err.jpg",
        password:"",
        nickName:"",
        username:"",
        email:"",
        isDel: false,
        isLock: false
    };
    vm.userRoles= []
    vm.state = {
        nickName: false,
        username: false,
        password: false,
        email: false,
    };
    vm.modalState = "ADD"
    vm.userTitle = "添加用户";
    vm.roleTitle = "添加角色";
    $("#editUserModal").modal("show");
}