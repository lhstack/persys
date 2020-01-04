var vm = new Vue({
    el:"#app",
    data:function(){
        return {
            namespaceList:[],
            selector:"",
            pager:{
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
            list:[],
            option: {
                addBtn:false,
                editBtn: false,
                delBtn: false,
                viewBtn: true,
                printBtn: true,
                excelBtn: true,
                height:550,
                column:[
                    {
                        label:'ID',
                        prop:'id',
                        slot: true,
                        hide: true
                    },
                    {
                        label:'namespace',
                        prop:'namespace',
                    },
                    {
                        label:'sql',
                        prop:'sql',
                        type:"textarea",
                        slot: true
                    },
                    {
                        label:"次数",
                        prop:"count",
                        slot: true
                    },
                    {
                        label:'parameter',
                        prop:'parameter',
                        type:"textarea",
                        slot: true
                    }, {
                        label:'MaxTime',
                        prop:'maxExecuteTime',
                        slot: true
                    }, {
                        label:'MinTime',
                        prop:'minExecuteTime',
                        slot: true
                    }, {
                        label:'average',
                        prop:'average',
                        slot: true
                    }
                ]
            },
        }
    },
    created:function(){
        this.loadNameSpace()
    },
    mounted:function(){

    },
    methods:{
        del:function(){
           this.$confirm("确认删除吗，删除之后不可恢复",{
               type:"warning"
           }).then(function(){
               requestDel({
                   url:"/remote/sql/del/" + vm.selector
               }).then(function(res){
                   if(res.code == 200){
                       vm.loadNameSpace();
                   }else{
                       vm.$message.error(res.msg)
                   }
               }).catch(function(err){
                   vm.$message.error("删除失败，服务器出现了异常")
               })
           })
        },
        sizeChange:function(size){
            this.pager.pageSize = size;
            this.loadData()
        },
        refreshChange:function(){
            this.pager.currentPage = 1;
            this.pager.pageSize = 10;
            this.loadData()
        },
        currentChange:function(page){
            this.pager.currentPage = page;
            this.loadData()
        },
        change:function(change){
            this.pager.currentPage = 1;
            this.pager.pageSize = 10;
            this.loadData()
        },
        loadData:function(){
            if(this.selector){
                get({
                    url:"/remote/sql/list/" + this.selector + "?page=" + this.pager.currentPage + "&size=" + this.pager.pageSize
                }).then(function(res){
                    if(res.code == 200){
                        vm.pager.total = res.count;
                        vm.list = res.data
                    }else{
                        vm.list = []
                    }
                }).catch(function(err){
                    vm.list = []
                })
            }else{
                get({
                    url:"/remote/sql/list/?page=" + this.pager.currentPage + "&size=" + this.pager.pageSize
                }).then(function(res){
                    if(res.code == 200){
                        vm.pager.total = res.count;
                        vm.list = res.data
                    }
                }).catch(function(err){
                    vm.list = []
                })
            }
        },
        loadNameSpace:function(){
            get({
                url:"/remote/sql/namespaceList"
            }).then(function(res){
                if(res.code == 200){
                    vm.namespaceList = res.data
                    if(vm.namespaceList.length > 0){
                        vm.selector = vm.namespaceList[0]
                    }
                    vm.loadData();
                }else{
                    vm.namespaceList = []
                }
            }).catch(function(err){
                vm.namespaceList = []
            })
        }
    }
})