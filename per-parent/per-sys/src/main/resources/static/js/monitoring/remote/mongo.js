function randomColor(){
    return `rgb(${parseInt(Math.random() * 255)},${parseInt(Math.random() * 255)},${parseInt(Math.random() * 255)})`
}

var vm = new Vue({
    el:"#app",
    data:function(){
        return {
            line: {
                title: {
                    text: '表数据条数'
                },
                tooltip: {},
                legend: {
                    data:['条数']
                },
                xAxis: {
                    data: []
                },
                yAxis: {},
                series: [{
                    name: '条数',
                    type: 'line',
                    data: [],
                    animation: true,
                    smooth: true
                }]
            },
            pie: {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data:[]
                },
                series: [
                    {
                        name:'系统状态',
                        type:'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false,
                                position: 'inside'
                            },
                            emphasis: {
                                show: true,
                                textStyle: {
                                    fontSize: '10',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data:[

                        ]
                    }
                ]
            },
            connections:  {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data:[]
                },
                series: [
                    {
                        name:'连接情况',
                        type:'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false,
                                position: 'inside'
                            },
                            emphasis: {
                                show: true,
                                textStyle: {
                                    fontSize: '10',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data:[

                        ]
                    }
                ]
            },
            pieOp: {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data:[]
                },
                series: [
                    {
                        name:'操作次数',
                        type:'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false,
                                position: 'inside'
                            },
                            emphasis: {
                                show: true,
                                textStyle: {
                                    fontSize: '10',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data:[

                        ]
                    }
                ]
            },
            tcmalloc: {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data:[]
                },
                series: [
                    {
                        name:'内存情况',
                        type:'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false,
                                position: 'inside'
                            },
                            emphasis: {
                                show: true,
                                textStyle: {
                                    fontSize: '10',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data:[

                        ]
                    }
                ]
            },
            list:[],
            mongoInfoList:[],
            mongoInfo:{},
            dialogVisible:false,
            select:"",
            rules:{
                mongoUrl:[
                    {required: true,message:"请输入mongoUrl地址",trigger:"blur"}
                ]
            },
            type:""
        }
    },
    created:function(){
        this.loadMongoInfoList();
    },
    mounted:function(){

    },
    methods:{
        save:function(){
            this.$refs.ruleForm.validate(function(res){
                if(res){
                    $.ajax({
                        url:vm.type == 'save' ? "/remote/monitoring/mongo/save" : "/remote/monitoring/mongo/update/" + vm.select,
                        contentType:"application/json;charset=utf-8",
                        dataType:"json",
                        type:vm.type == 'save' ? "POST" : "PUT",
                        data:JSON.stringify(vm.mongoInfo),
                        success:function(res){
                            vm.dialogVisible = false
                            if(res.code == 200){
                                if(vm.type == 'save'){
                                    vm.mongoInfoList.push(res.data);
                                }else{
                                    vm.loadMongoInfoList();
                                }
                            }else{
                                vm.$message.error(res.msg)
                            }
                        },
                        error:function(err){
                            vm.$message.error("服务器出现了异常，请稍后重试")
                        }
                    })
                }
            })
        },
        change:function(em){
            this.loadData(em);
        },
        update:function(){
            this.type = "update";
            get({
                url:"/remote/monitoring/mongo/find/" + this.select
            }).then(function(res){
                if(res.code == 200){
                    vm.dialogVisible = true;
                    vm.mongoInfo = res.data
                    vm.select = vm.mongoInfo.id
                }else{
                    vm.$message.error(res.msg)
                }
            }).catch(function(err){
                console.log(err)
                vm.$message.error("请先添加连接信息再更新")
            })
        },
        del:function(){
            this.$confirm("是否删除，删除之后不可更改",{
                type:"warning"
            }).then(function () {
                requestDel({
                    url:"/remote/monitoring/mongo/del/" + vm.select
                }).then(function (value) {
                    if(value.code == 200){
                        vm.loadMongoInfoList()
                    }else{
                        vm.$message.error(value.msg)
                    }
                }).catch(function (reason) {
                    vm.$message.error("服务器出现了异常")
                })
            }).catch(function (reason) {

            })
        },
        loadMongoInfoList:function(){
            get({
                url:"/remote/monitoring/mongo/list"
            }).then(function(res){
                if(res.code == 200){
                    vm.mongoInfoList = res.data;
                    if(res.data.length > 0){
                        vm.select = res.data[0].id;
                        vm.mongoInfo = res.data[0];
                        vm.loadData(vm.select);
                    }else{
                        vm.loadMyData();
                    }
                }
            }).catch(function(err){
                console.log(err)
            })
        },
        loadMyData:function(){
            get({
                url:"/monitoring/mongo/state"
            }).then(function(res){
                if(res.code == 200){
                    vm.list = []
                    vm.initLine(res.data);
                    vm.initPie(res.data);
                    vm.initPieOp(res.data);
                    vm.initConnections(res.data);
                    vm.initTcmalloc(res.data)
                    vm.initList("",res.data);
                }
            }).catch(function(err){
                console.log(err)
            })
        },
        loadData:function(id){
            get({
                url:"/remote/monitoring/mongo/state/" + id
            }).then(function(res){
                if(res.code == 200){
                    vm.list = []
                    vm.initLine(res.data);
                    vm.initPie(res.data);
                    vm.initPieOp(res.data);
                    vm.initConnections(res.data);
                    vm.initTcmalloc(res.data)
                    vm.initList("",res.data);
                }
            }).catch(function(err){
                console.log(err)
            })
        },
        initConnections:function(data){
            vm.connections = {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data:[]
                },
                series: [
                    {
                        name:'连接情况',
                        type:'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false,
                                position: 'inside'
                            },
                            emphasis: {
                                show: true,
                                textStyle: {
                                    fontSize: '10',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data:[

                        ]
                    }
                ]
            };
            var connections = data.globalServerInfo.connections;
            for (key in connections) {
                try {
                    parseInt(connections[key]);
                    vm.connections.series[0].data.push({
                        value: connections[key],
                        name: key + "->" + connections[key]
                    });
                    vm.connections.legend.data.push(key + "->" + connections[key])
                } catch (e) {

                }
            }
            var myChart = echarts.init(document.getElementById('connections'));
            myChart.setOption(this.connections);

        },
        initTcmalloc:function(data){
            vm.tcmalloc = {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data:[]
                },
                series: [
                    {
                        name:'内存情况',
                        type:'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false,
                                position: 'inside'
                            },
                            emphasis: {
                                show: true,
                                textStyle: {
                                    fontSize: '10',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data:[

                        ]
                    }
                ]
            }
            var tcmalloc = data.globalServerInfo.tcmalloc.tcmalloc;
            for (key in tcmalloc) {
                try {
                    parseInt(tcmalloc[key]);
                    vm.tcmalloc.series[0].data.push({
                        value: tcmalloc[key],
                        name: key
                    });
                    vm.tcmalloc.legend.data.push(key)
                } catch (e) {

                }
            }
            var myChart = echarts.init(document.getElementById('tcmalloc'));
            myChart.setOption(this.tcmalloc);
        },
        initLine:function (data) {
            vm.line = {
                title: {
                    text: '表数据条数'
                },
                tooltip: {},
                legend: {
                    data:['条数']
                },
                xAxis: {
                    data: []
                },
                yAxis: {},
                series: [{
                    name: '条数',
                    type: 'line',
                    data: [],
                    animation: true,
                    smooth: true
                }]
            };
            for (key in data.tableRowCounts){
                vm.line.xAxis.data.push(key);
                vm.line.series[0].data.push(data.tableRowCounts[key])
            }
            var myChart = echarts.init(document.getElementById('line'));
            myChart.setOption(this.line);
        },
        initPieOp:function(data){
            vm.pieOp =  {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data:[]
                },
                series: [
                    {
                        name:'操作次数',
                        type:'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false,
                                position: 'inside'
                            },
                            emphasis: {
                                show: true,
                                textStyle: {
                                    fontSize: '10',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data:[

                        ]
                    }
                ]
            }
            var opcounters = data.globalServerInfo.opcounters;
            for (key in opcounters) {
                try {
                    parseInt(opcounters[key]);
                    vm.pieOp.series[0].data.push({
                        value: opcounters[key],
                        name: key + "->" + opcounters[key]
                    });
                    vm.pieOp.legend.data.push(key + "->" + opcounters[key])
                } catch (e) {

                }
            }
            var myChart = echarts.init(document.getElementById('pieOp'));
            myChart.setOption(this.pieOp);
        },
        initPie:function(data){
            vm.pie = {
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b}: {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    x: 'left',
                    data:[]
                },
                series: [
                    {
                        name:'系统状态',
                        type:'pie',
                        radius: ['50%', '70%'],
                        avoidLabelOverlap: false,
                        label: {
                            normal: {
                                show: false,
                                position: 'inside'
                            },
                            emphasis: {
                                show: true,
                                textStyle: {
                                    fontSize: '10',
                                    fontWeight: 'bold'
                                }
                            }
                        },
                        labelLine: {
                            normal: {
                                show: false
                            }
                        },
                        data:[

                        ]
                    }
                ]
            }
            var stats = data.stats;
            for (key in stats){
                try{
                    parseInt(stats[key]);
                    vm.pie.series[0].data.push({
                        value: stats[key],
                        name: key + "->" + stats[key]
                    });
                    vm.pie.legend.data.push(key + "->" + stats[key])
                }catch (e) {

                }
            }
            var myChart = echarts.init(document.getElementById('pie'));
            myChart.setOption(this.pie);
        },
        initList:function(preKey,data){
            for(key in data){
                if(key != 'stats' & key != 'tableRowCounts' & key != 'opcounters'  & key != 'connections' & key != 'tcmalloc'){
                    if(data[key] instanceof Object){
                        if(!preKey){
                            vm.initList(key,data[key])
                        }else{
                            vm.initList(preKey + "." + key,data[key])
                        }
                    }else{
                        if(!preKey){
                            vm.list.push({
                                key: key,
                                value: data[key]
                            })
                        }else{
                            vm.list.push({
                                key: preKey + "." + key,
                                value: data[key]
                            })
                        }

                    }
                }
            }
        }
    }
});