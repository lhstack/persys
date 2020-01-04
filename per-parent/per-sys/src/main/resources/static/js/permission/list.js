var vm = new Vue({
    el:"#app",
    data:function(){
        return {
            //卡片参数
            option: {
                addBtn:true,
                props: {
                    img: 'img',
                    title: 'title',
                    info: 'text'
                },
                span:6,
                gutter:10
            },
            //所有角色
            data:[],
            //分页参数
            pager:{
                //每页显示条数
                size: 11,
                //总记录数
                total: 0,
                //当前页
                page: 1
            },
            //第一个对话框状态: true显示: false不显示
            dialogVisible:false,
            //对话框绑定的当前操作的角色
            currentData:{
            },
            //对话框标题
            title:"修改",
            //第二个对话框状态: true显示: false不显示
            isInitPager: false,
            currentDataRef:{}
        }
    },
    methods:{
        //加载所有角色
        loadPermissions:function(){
            get({
                url:"/permission/list",
                data:{
                    page: this.pager.page,
                    size: this.pager.size
                }
            }).then(function(res){
                if(res.code == 200){
                   vm.data = res.data.map(function(item){
                        return {
                            id: item.id,
                            img: item.icon,
                            title: item.permissionName,
                            text: item.logogramName
                        };
                    });
                    vm.pager.total = res.count;
                    if(!this.isInitPager){
                        vm.initPager();
                        this.isInitPager = true
                    }
                }
            }).catch(function(err){
                console.log(err)
            })
        },
        //删除角色
        del:function(row, index) {
            this.$confirm("确认删除吗?","警告",{
                type: "warning"
            }).then(function(){
                requestDel({
                    url:"/permission/del/" + row.id
                }).then(function(res){
                    if(res.code == 200){
                        vm.data.splice(index,1);
                        vm.$message.success("删除成功")
                        vm.pager.total -= 1
                    }else{
                        vm.$message.error(res.msg)
                    }
                }).catch(function(err){
                    vm.$message.error("删除失败")
                })
            })
        },
        //修改角色
        edit:function(row,index){
            vm.title = "修改";
            this.currentDataRef = row;
            vm.currentData = {
                id: row.id,
                title: row.title,
                img: row.img,
                text: row.text
            };
            vm.dialogVisible = true;
        },
        //添加权限
        add:function(row,index){
            vm.title = "添加";
            vm.dialogVisible = true;
            vm.currentData = {
                img: "/images/err.jpg"
            };
        },
        checkPermission:function(permission){
            if(!/^[A-Z]*_?[A-Z]([A-Z]|_)*$/g.test(permission.permissionName)){
                this.$message.error("请按规则填写权限名称");
                return false;
            }
            if(!permission.logogramName || permission.logogramName.trim().length < 1){
                this.$message.error("请填写权限名称介绍或者简写");
                return false;
            }
            return true;
        },
        //改变角色状态，更新成功不走数据库，动态更新本地缓存
        changePermissionState:function(){
            this.currentDataRef.title = this.currentData.title;
            this.currentDataRef.id = this.currentData.id;
            this.currentDataRef.text = this.currentData.text;
            this.currentDataRef.img = this.currentData.img;
        },
        //提交
        submit:function(){
          var permission = {
              permissionName: this.currentData.title,
              icon: this.currentData.img,
              logogramName: this.currentData.text
          };
          var id = this.currentData.id;
          if(this.checkPermission(permission)){
              permission.id = id;
              var load = this.$loading({text:"加载中..."});
              post({
                  url:this.title == "修改" ? "/permission/update" : "/permission/add",
                  data:permission
              }).then(function(res){
                  load.close();
                  if(res.code == 200){
                      vm.$message.success(vm.title + "权限信息成功");
                      if(vm.title == "添加"){
                          vm.loadPermissions();
                      }else{
                          vm.changePermissionState();
                      }
                      vm.dialogVisible = false;
                  }else{
                      vm.$message.error(vm.title + res.msg)
                  }
              }).catch(function(err){
                  load.close();
                  vm.$message.error(vm.title + "修改权限信息失败")
              })
          }
        },
        //初始化分页
        initPager:function(){
            layui.use('laypage', function(){
                var laypage = layui.laypage;

                //执行一个laypage实例
                laypage.render({
                    elem: 'pager' //注意，这里的 test1 是 ID，不用加 # 号
                    ,count: vm.pager.total //数据总数，从服务端得到
                    ,limit:vm.pager.size
                    , prev:"<"
                    , next:">"
                    ,layout:[
                        "prev",
                        "page",
                        "next",
                        "skip",
                        "count",
                        "limit"
                    ]
                    ,jump:function(object,first){
                        if(!first){
                            vm.pager.page = object.curr;
                            vm.pager.size = object.limit;
                            vm.loadPermissions();
                        }
                    }
                });
            });
        },
        //执行上传文件点击事件
        processUpload:function(){
            $("#fileUpload")[0].click();
        },
        //上传文件事件
        fileUpload:function(f) {
            var file = $("#fileUpload")[0].files[0];
            if(this.check(file)){
                var load = this.$loading({target:"#currentImg"});
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
                        vm.currentData.img = res.path;
                        $("#fileUpload").val("");
                        load.close();
                    },
                    error:function(err){
                        vm.$message.error("上传头像失败");
                        $("#fileUpload").val("");
                        load.close();
                    }
                })
            }
        },
        //检查文件是否符合要求
        check:function(file){
            var size = file.size / 1024;
            if(size > 5000){
                this.$message({
                    message: '上传文件大小不能超过5MB',
                    type: 'warning'
                });
                return false;
            }
            var extName = file.name.substr(file.name.lastIndexOf('.') + 1);
            extName = extName.toLowerCase();
            switch (extName) {
                case "png":return true;
                case "jpg":return true;
                case "jpeg":return true;
                case "gif":return true;
                default:{
                    this.$message({
                        message: '上传文件不是图片格式',
                        type: 'warning'
                    });
                    return false;
                }
            }
        }

    },
    mounted:function(){

    },
    created:function(){
        this.loadPermissions();
    }
});
