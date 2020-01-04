var vm = new Vue({
    el:"#app",
    data:function(){

        var urlRegex = /^(?:http(s)?:\/\/)([\w.-]+(?:\.[\w\.-]+)+|localhost)[\w\-\._~:/?#[\]@!\$&'\*\+,;=.]+$/;
        var validUrl = function(rule, value, callback){
            if(!urlRegex.test(value)){
                callback(new Error("首页请以http://或者https://开头"))
            }else{
                if(value.split(",").length > 1){
                    callback(new Error("首页只能是一个url"))
                }else{
                    callback();
                }
            }
        };
        var validUrls = function(rule, value, callback){
            if(!value || typeof(value) == 'undefined' || value.length == 0){
                callback(new Error("回调url请以http://或者https://开头，多个用,隔开"))
            }else{
                var urls = [];
                if(typeof(value) == 'string'){
                    urls = value.split(',')
                }else{
                    urls = value;
                }
                for(var i = 0;i < urls.length ;i++){
                    if(!urlRegex.test(urls[i])){
                        callback(new Error("回调url请以http://或者https://开头，多个用,隔开"));
                        return ;
                    }
                }
                callback();
            }
        };
        return {
            pager:{
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            myApps:[],
            option:{
                addBtn: false,
                page:true,
                align:'center',
                menuAlign:'center',
                height:400,
                column:[
                    {
                        label:'应用名称',
                        prop:'appName',
                        editDisabled: true
                    }, {
                        label:'应用注释',
                        prop:'appDescription',
                        hide: true,
                        type:"textarea"
                    },{
                        label:'创建时间',
                        prop:'createTime',
                        hide: true,
                        type:"datetime",
                        editDisabled: true
                    },{
                        label:'应用首页',
                        prop:'index',
                        hide: true
                    },{
                        label:'回调地址',
                        prop:'service',
                        hide: true,
                        type:"textarea"
                    },{
                        label:'类型',
                        prop:'type',
                        hide: true,
                        formslot: true
                    },{
                        label:'签名key',
                        prop:'jwtSignKey',
                        hide: true,
                        formslot: true
                    },{
                        label:'token',
                        prop:'token',
                        editDisabled: true
                    },
                    {
                        label:"权限角色",
                        prop:"isGeneratorPermissionAndRole",
                        hide: true,
                        formslot: true
                    }
                ]
            },
            sso:{
                type:"UUID",
                service:[],
                appDescription:"",
                appName:"",
                isGeneratorPermissionAndRole: false
            },
            rules: {
                appName: [
                    { required: true, message: '请输入应用名称', trigger: 'change' },
                    { min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' }
                ],
                appDescription: [
                    { required: true, message: '请输入应用描述', trigger: 'change' }
                ],
                index: [
                    { validator:validUrl,required: true, trigger: 'change' }
                ],
                service: [
                    {validator: validUrls,required: true, trigger: 'change' }
                ]
            },
            apps:[
                {
                    simpleName:"我的应用",
                    icon:"el-icon-loading",
                    name:"myApp"
                },
                {
                    simpleName:"使用说明",
                    icon:"el-icon-s-order",
                    name:"declare"
                }
            ],
            showName: "myApp"
        }
    },
    mounted:function(){
      this.loadApps();
    },
    methods:{
        refresh:function(){
          this.pager = {
              currentPage: 1,
              pageSize: 10,
              total: 0
          };
          this.loadApps();
        },
        del:function(row){
          this.$confirm("确认删除吗？删除之后无法恢复。","危险",{
              type:"error"
          }).then(function(){
              requestDel({
                  url:"/sso/del/" + row.id
              }).then(function(res){
                  if(res.code == 200){
                      vm.$message.success("删除成功");
                      vm.refresh()
                  }else{
                      vm.$message.error("删除失败");
                  }
              }).catch(function(){
                  vm.$message.error("删除失败");
              })
          }).catch(function(){

          })
        },
        update:function(row,index,done,loading){
            if(typeof(vm.sso.service) == 'string'){
                vm.sso.service = vm.sso.service.split(",");
            }
            vm.sso.service = vm.sso.service.filter(function(ele, index, array) {
                return array.indexOf(ele) === index
            });
            $.ajax({
                url:"/sso/update",
                type:"PUT",
                dataType:"JSON",
                contentType:"application/json;charset=utf-8",
                data:JSON.stringify(vm.sso),
                success:function(res){
                    if(res.code == 200){
                        vm.$message.success("更新应用成功")
                        vm.loadApps();
                    }else{
                        vm.$message.error("更新应用失败")
                    }
                    done();
                },
                error:function(err){
                    vm.$message.error("更新应用失败")
                    done();
                }
            })
        },
        currentChange:function(page){
            this.pager.currentPage = page;
            this.loadApps();
        },
        sizeChange:function(size){
            this.pager.pageSize = size;
            this.loadApps();
        },
        /**
         * 加载app
         */
        loadApps:function(){
            get({
                url:"/sso/page?page=" + this.pager.currentPage + "&size=" + this.pager.pageSize
            }).then(function(res){
                if(res.code == 200){
                    vm.pager.total = res.count;
                    vm.myApps = res.data;
                }
            }).catch(function(err){
                console.log(err)
            })
        },
        onSubmit:function(){
            this.$refs.sso.validate(function(valid){
                if(valid){
                    if(typeof(vm.sso.service) == 'string'){
                        vm.sso.service = vm.sso.service.split(",");
                    }
                    vm.sso.service = vm.sso.service.filter(function(ele, index, array) {
                        return array.indexOf(ele) === index
                    });
                    $.ajax({
                        url:"/sso/add",
                        dataType:"JSON",
                        contentType:"application/json;charset=utf-8",
                        data:JSON.stringify(vm.sso),
                        type:"POST",
                        success:function(res){
                            if(res.code == 200){
                                vm.$message.success("添加应用成功")
                                vm.showName = "myApp";
                                vm.loadApps();
                            }else{
                                vm.$message.error("添加应用失败,应用已存在")
                            }
                        },
                        error:function(err){
                            vm.$message.error("添加应用失败,应用已存在")
                        }
                    })
                }else{

                }
            })
        },
        handleClick:function(item) {
            this.showName = item.name;
        }
    }
});