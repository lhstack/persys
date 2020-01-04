var vm = new Vue({
    el:"#app",
    data:function(){
        return {
            oss:{
                ipWhiteList:[]
            },
            list:[],
            pager:{
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            options:{
                height: 500,
                dialogHeight:350,
                editBtn: true,
                excelBtn: true,
                searchShow: false,
                printBtn: true,
                delBtn: true,
                viewBtn: true,
                border:true,
                addBtn: true,
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
                        label:'keyId',
                        prop:'accessKeyId',
                        slot: true,
                        width: 350,
                        rules: [{
                            required: true,
                            message: "请输入accessKeyId",
                            trigger: "blur"
                        }]
                    }, {
                        label:'secret',
                        prop:'accessKeySecret',
                        slot: true,
                        width: 350,
                        rules: [{
                            required: true,
                            message: "请输入accessKeySecret",
                            trigger: "blur"
                        }]
                    }, {
                        label:'endPoint',
                        prop:'endPoint',
                        slot: true,
                        width: 250,
                        rules: [{
                            required: true,
                            message: "请输入endPoint",
                            trigger: "blur"
                        }]
                    },{
                        label:'桶名称',
                        prop:'bucketName',
                        slot: true,
                        width: 200,
                        rules: [{
                            required: true,
                            message: "请输入bucketName",
                            trigger: "blur"
                        }]
                    },{
                        label:'schema',
                        prop:'schema',
                        slot: true,
                        width: 100,
                        rules: [{
                            required: true,
                            message: "请输入schema,http | https ...",
                            trigger: "blur"
                        }]
                    },{
                        label:'类型',
                        prop:'type',
                        slot: true,
                        formslot: true,
                        width: 150,
                        rules: [{
                            required: true,
                            message: "请选择oss类型",
                            trigger: "blur"
                        }]
                    },{
                        label:'token',
                        prop:'token',
                        slot: true,
                        width: 350,
                        disabled: true
                    },{
                        label:'白名单',
                        prop:'ipWhiteList',
                        slot: true,
                        formslot: true,
                        width: 350,
                        rules: [{
                            required: true,
                            message: "请输入Ip白名单，0.0.0.0为所有ip可用",
                            trigger: "blur"
                        }]
                    }
                ]
            }
        }
    },
    created:function(){
        this.loadData();
    },
    mounted:function(){

    },
    methods:{
        save:function(row,done,loading){
            if(typeof(this.oss.ipWhiteList) == 'string'){
                this.oss.ipWhiteList = this.oss.ipWhiteList.split(",")
            }
            $.ajax({
                url:"/oss/add",
                dataType:"JSON",
                contentType:"application/json;charset=utf-8",
                type:"post",
                data:JSON.stringify(this.oss),
                success:function(res){
                    done();
                    if(res.code == 200){
                        vm.loadData();
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
        del:function(row){
            console.log(row)
            this.$confirm("是否删除吗,删除之后不可恢复",{
                type:"warning"
            }).then(function (value) {
                requestDel({
                    url:"/oss/del/" + row.id
                }).then(function(res){
                    if(res.code == 200){
                        vm.loadData();
                    }else{
                        vm.$message.error(res.msg)
                    }
                }).catch(function (reason) {
                    vm.$message.error("服务器出现了异常")
                })
            })
        },
        loadData:function(){
            get({
                url:"/oss/list",
                data:{
                    page: this.pager.currentPage,
                    size: this.pager.pageSize
                }
            }).then(function(res){
                if(res.code == 200){
                    vm.list = res.data;
                    vm.pager.total = res.count
                }
            }).catch(function(err){
                console.log(err)
            })
        },
        update:function(row,index,done,loading){
            if(typeof(this.oss.ipWhiteList) == 'string'){
                this.oss.ipWhiteList = this.oss.ipWhiteList.split(",")
            }
            $.ajax({
                url:"/oss/update/" + this.oss.id,
                dataType:"JSON",
                contentType:"application/json;charset=utf-8",
                type:"PUT",
                data:JSON.stringify(this.oss),
                success:function(res){
                    done();
                    if(res.code == 200){
                        vm.loadData();
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
            this.loadData()
        },
        sizeChange:function(size){
            this.pager.pageSize = size;
            this.loadData();
        },
        currentChange:function(page){
            this.pager.currentPage = page
            this.loadData()
        }
    }
})