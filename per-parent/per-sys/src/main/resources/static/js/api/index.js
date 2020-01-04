var vm = new Vue({
    el:"#app",
    data:function(){
        return {
            list:[],
            pager:{
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            api:{},
            options:{
                height: 500,
                dialogHeight:350,
                editBtn: true,
                excelBtn: true,
                searchShow: false,
                printBtn: true,
                delBtn: false,
                viewBtn: false,
                border:true,
                addBtn: false,
                align:'center',
                menuAlign:'center',
                column:[
                    {
                        label:'ID',
                        prop:'id',
                        hide: true,
                        disabled:true
                    },
                    {
                        label:'请求方法',
                        prop:'requestMethod',
                        slot: true,
                        disabled:true
                    }, {
                        label:'请求url',
                        prop:'pattenUrl',
                        slot: true,
                        type:"textarea",
                        disabled:true
                    }, {
                        label:'执行方法',
                        prop:'handlerMethod',
                        slot: true,
                        type:"textarea",
                        disabled:true
                    },{
                        label:'鉴权类型',
                        prop:'authorityType',
                        slot: true,
                        formslot:true
                    },{
                        label:'注释信息',
                        prop:'description'
                    }
                ]
            },
            dialogShowPermission: false,
            permission:{
                apiPermissions:[

                ],
                allPermissions:[

                ]
            },
            role:{
                apiRoles:[],
                allRoles:[]
            },
            dialogShowRole: false,
            apiInfoSearch:{
                requestMethod:"",
                pattenUrl:"",
                authorityType:-1,
                description:""
            }
        }
    },
    mounted:function () {
        this.loadData(this.apiInfoSearch);
        this.loadAllPermission();
        this.loadAllRoles();
    },
    created:function () {

    },
    methods:{
        reset:function(){
            this.apiInfoSearch = {
                requestMethod:"",
                pattenUrl:"",
                authorityType:-1,
                description:""
            }
        },
        searchChange:function(){
            this.loadData(this.apiInfoSearch)
        },
        showRole:function(row){
            this.api = row;
            this.dialogShowRole = true
            get({
                url:"/authority/roles?id=" + this.api.id
            }).then(function (value) {
                if(value.code == 200){
                    vm.role.apiRoles = value.data.map(function(item){
                        return item.id
                    })
                }else{
                    vm.$message.error(value.msg)
                }
            }).catch(function (reason) {
                vm.$message.error("服务器出现了异常")
            })
        },
        saveRole:function(){
            var apiRoles = this.role.apiRoles.join(",")
            post({
                url:"/authority/roles?ids=" + apiRoles + "&apiId=" + this.api.id
            }).then(function(res){
                if(res.code == 200){
                    vm.dialogShowRole = false;
                    vm.loadData(vm.apiInfoSearch)
                }else{
                    vm.$message.error(res.msg)
                }
            }).catch(function(err){
                vm.$message.error("服务器出现了异常")
            })
        },
        loadAllRoles:function(){
            get({
                url:"/role/all"
            }).then(function(res){
                if(res.code == 200){
                    vm.role.allRoles = res.data
                }
            })
        },
        savePermission:function(){
            var apiPermissionIds = this.permission.apiPermissions.join(",")
            post({
                url:"/authority/permissions?ids=" + apiPermissionIds + "&apiId=" + this.api.id
            }).then(function(res){
                if(res.code == 200){
                    vm.dialogShowPermission = false;
                    vm.loadData(vm.apiInfoSearch)
                }else{
                    vm.$message.error(res.msg)
                }
            }).catch(function(err){
                vm.$message.error("服务器出现了异常")
            })
        },
        loadAllPermission:function(){
            get({
                url:"/permission/all"
            }).then(function(res){
                if(res.code == 200){
                    vm.permission.allPermissions = res.data
                }
            }).catch(function(err){

            })
        },
        showPermission:function(row){
            this.dialogShowPermission = true;
            this.api = row;
            get({
                url:"/authority/permissions?id=" + this.api.id
            }).then(function (value) {
                if(value.code == 200){
                    vm.permission.apiPermissions = value.data.map(function(item){
                        return item.id
                    })
                }else{
                    vm.$message.error(value.msg)
                }
            }).catch(function (reason) {
                vm.$message.error("服务器出现了异常")
            })
        },
        update:function(em,index,done,loading){
            $.ajax({
                url:"/authority/authorityType/" + this.api.authorityType + "/" + this.api.id + "?description=" + this.api.description,
                dataType:"json",
                type:"post",
                success:function(res){
                    done();
                    if(res.code == 200){
                        vm.loadData(vm.apiInfoSearch);
                    }else{
                        vm.$message.error(res.msg)
                    }
                },
                error:function(err){
                    done();
                    vm.$message.error("服务器出现了异常")
                }
            })

        },
        refreshChange:function(){
            this.pager = {
                currentPage: 1,
                pageSize: 10,
                total: 0
            }
            this.loadData(this.apiInfoSearch)
        },
        sizeChange:function(size){
            this.pager.pageSize = size;
            this.loadData(this.apiInfoSearch)
        },
        currentChange:function(page){
            this.pager.currentPage = page;
            this.loadData(this.apiInfoSearch)
        },
        loadData:function(search){
            $.ajax({
                url:"/authority/list?page=" + this.pager.currentPage + "&size=" + this.pager.pageSize,
                type:"POST",
                data:JSON.stringify(search),
                contentType:"application/json;charset=utf-8",
                dataType:"JSON",
                success:function(res){
                    if(res.code == 200){
                        vm.list = res.data;
                        vm.pager.total = res.count;
                    }else{
                        vm.list = [];
                        vm.pager.total = 0
                    }
                },
                error:function(err){
                    vm.list = [];
                    vm.pager.total = 0
                }
            })
        }
    }
})