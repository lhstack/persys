var vm = new Vue({
    el:"#app",
    data:function(){
        return {
            pager:{
                currentPage: 1,
                pageSize: 10,
                total: 0
            },
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
            data:[]
        }
    },
    created:function(){
        this.loadMonitoringData();
    },
    methods:{
        refreshChange:function(){
            this.loadMonitoringData();
        },
        sizeChange:function(size){
            this.pager.pageSize = size;
            this.loadMonitoringData();
        },
        currentChange:function(page){
            this.pager.currentPage = page;
            this.loadMonitoringData();
        },
        loadMonitoringData:function(){
            get({
                url:"/monitoring/mysql/list/" + this.pager.currentPage + "/" + this.pager.pageSize,
            }).then(function(res) {
                if(res.code == 200){
                    vm.data = res.data;
                    vm.pager.total = res.count
                }else{
                    vm.$message.error(res.msg)
                }
            }).catch(function(err){
                vm.$message.error("服务器出现了异常")
            })
        }
    }
});