var vm = new Vue({
    el:"#app",
    data:function(){
        return {
            //菜单所属表单
            form: {
                createTime: "",
                enable: true,
                href: "",
                icon: "i",
                id: "",
                isParent: true,
                menuName: "",
                parentId: 0,
                sortBy: 0,
                child:[]
            },
            //菜单数据
            data: [],
            //tree组件所需参数
            defaultProps: {
                children: 'child',
                label: 'menuName'
            },
            //父级菜单集合
            pList:[],
            //静态图标
            icons: staticIcons,
            //dialog弹出框状态
            dialogVisible: false,
            //权限标签
            permissionTags: [],

            //管理员以外的所有权限
            allPermission:[],
            //控制权限弹出框状态
            permissionState: false,
            //权限转换成map
            perMap:{}
        }
    },
    filters:{
        tagFilter:function(old,val){
            return old.length > 9 ? old.substr(0,9) : old;
        }
    },
    methods:{

        closePermissionTags:function(per){
            var index = -1;
            for(var i = 0;i < this.permissionTags.length;i ++){
                if(per.logogramName === this.permissionTags[i].logogramName){
                    index = i;
                    break;
                }
            }
            this.permissionTags.splice(index,1)
        },

        //修改状态
        checkTagState:function(per){
            if(vm.perMap[per.logogramName]){
                delete vm.perMap[per.logogramName];
                this.$forceUpdate();
                this.closePermissionTags(per);
            }else{
                vm.perMap[per.logogramName] = per;
                this.permissionTags.unshift(per);
            }
        },
        active:function(per){
            if(vm.perMap[per.logogramName]){
                return "background: red;color:white;"
            }
            return "";
        },
        //加载除管理员以外的所有权限
        loadPermissions:function(){
            get({
                url:"/menu/pmsList"
            }).then(function(res){
                if(res.code == 200){
                    vm.allPermission = res.data
                }
            })
        },
        //根据菜单id加载权限
        loadPermissionByMid:function(id){
          get({
              url:"/menu/pms/" + id,
          }).then(function(res){
              if(res.code == 200){
                  vm.permissionTags = res.data
                  if(vm.permissionTags.length > 0){
                      vm.permissionTags.forEach(function(item){
                          vm.perMap[item.logogramName] = item;
                      });
                  }else{
                      vm.permissionTags = [];
                      vm.perMap = {};
                  }
              }
          }).catch(function(err){
              vm.permissionTags = []
              vm.perMap = {};
          })
        },
        //当排序字段失去焦点时回调
        checkSortBy:function(){
            if(this.form.sortBy < 1){
                this.form.sortBy = 0;
            }
        },
        //权限标签删除回调事件
        tagClose:function(index,permission){
            delete vm.perMap[permission.logogramName];
            this.$forceUpdate();
            this.permissionTags.splice(index, 1);
        },
        //点击图标显示选择图标组件
        iconClick:function(){
          this.dialogVisible = true;
        },
        //点击选择图标组件的确认按钮关闭组件
        resultIcon:function(icon){
            this.dialogVisible = false;
        },
        //图标点击回调方法
        handlerIconClick:function(icon){
            this.form.icon = icon;
        },
        //关闭选择图标组件方法
        close:function(){
          this.dialogVisible = false;
        },
        //菜单节点点击事件
        handleNodeClick:function(data) {
            this.form = data;
            this.loadPermissionByMid(data.id);
        },
        //添加菜单按钮
        add:function(data,index){
            if(data.isParent){
                vm.form = {
                    parentId: data.id,
                    enable: true,
                    sortBy: 0,
                    isParent: false
                };
                vm.permissionTags = []
                vm.perMap = {}
            }
        },
        //修改菜单按钮
        edit:function(data,index){
            this.form = data;
        },
        //删除菜单按钮
        remove:function(data,index){
            this.$confirm("是否删除?","警告",{
                type:"warning"
            }).then(function(){
                var str = data.id;
                requestDel({
                    url: "/menu/delete/" + str,
                }).then(function(res){
                    if(res.code == 200){
                        vm.$message.success("删除菜单成功");
                        vm.loadMenu();
                        vm.loadParentMenu();
                        vm.loadPermissions();
                    }else{
                        vm.$message.error("删除菜单失败");
                    }
                }).catch(function(err){
                    vm.$message.error("删除菜单失败");
                })
            })
        },
        //表单提交按钮
        onSubmit:function() {
            if(!this.form.menuName || this.form.menuName.length < 1){
                this.$message.error("请填写菜单名称");
                return ;
            }
            var pids = this.permissionTags.map(function(item){
                return item.id;
            });
            var load = this.$loading({text:"加载中..."});
            $.ajax({
                url:this.form.id ? "/menu/update?pids=" + pids.join(",") : "/menu/add?pids=" + pids.join(","),
                dataType:"json",
                data:JSON.stringify(this.form),
                contentType:"application/json;charset=utf-8",
                type:"POST",
                success:function(res){
                    load.close();
                    if(res.code == 200){
                        vm.$message.success("保存成功");
                        vm.loadMenu();
                        vm.loadParentMenu();
                        vm.loadPermissions();
                        window.parent.$$vm.loadMenu();
                    }else{
                        vm.$message.error("保存失败")
                    }
                },
                error:function(err){
                    load.close();
                    vm.$message.error("保存失败")
                }
            })
        },
        //加载所有菜单
        loadMenu:function(){
            get({
                url:"/menu/preList",
                method:"get"
            }).then(function(res){
                if(res.code == 200){
                    vm.data = res.data;
                    if(res.data.length > 0){
                        vm.form = res.data[0];
                        vm.loadPermissionByMid(vm.form.id)
                    }
                }
            }).catch(function(err){
                vm.menus = staticMenu;
            })
        },
        //加载所有父级菜单
        loadParentMenu:function(){
            get({
                url:"/menu/plist",
                method:"get"
            }).then(function(res){
                if(res.code == 200){
                    vm.pList = res.data;
                }
            }).catch(function(err){

            })
        }
    },
    created:function(){
        this.loadMenu();
        this.loadParentMenu();
        this.loadPermissions();
    },
    components:{
        "icons-template" : iconsTemplate
    }
});