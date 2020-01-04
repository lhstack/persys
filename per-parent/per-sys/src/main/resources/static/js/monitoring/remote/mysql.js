function randomColor() {
    return `rgb(${parseInt(Math.random() * 255)},${parseInt(Math.random() * 255)},${parseInt(Math.random() * 255)})`
}

var vm = new Vue({
    el: "#app",
    data: function () {
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
            pie:{
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
                        name:'系统信息',
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
            list: [],
            mysqlInfoList:[],
            select:"",
            mysqlInfo:{},
            dialogVisible:false,
            rules:{
                dbUrl:[
                    {
                        required: true,message:"请输入mysqlUrl",trigger:"blur"
                    }
                ],
                dbUsername:[
                    {
                        required: true,message:"请输入mysql用户名",trigger:"blur"
                    }
                ],
                dbPassword:[
                    {
                        required: true,message:"请输入mysql密码",trigger:"blur"
                    }
                ]
            },
            type:""
        }
    },
    created: function () {
        this.loadMysqlInfoList();
    },
    mounted: function () {

    },
    methods: {
        change:function(em){
            vm.loadData(em)
        },
        del:function(){
            this.$confirm("是否删除，删除之后无法恢复",{
                type:"warning"
            }).then(function () {
                requestDel({
                    url:"/remote/monitoring/mysql/del/" + vm.select
                }).then(function (value) {
                    if(value.code == 200){
                        vm.loadMysqlInfoList()
                    }else{
                        vm.$message.error(value.msg)
                    }
                }).catch(function (reason) {
                    vm.$message.error("服务器出现了异常")
                })
            }).catch()
        },
        update:function(){
            this.type = "update";
            get({
                url:"/remote/monitoring/mysql/find/" + this.select
            }).then(function (value) {
                if(value.code == 200){
                    vm.mysqlInfo = value.data;
                    vm.dialogVisible=true;
                    vm.select = value.data.id
                }else{
                    vm.$message.error(value.msg)
                }
            }).catch(function (reason) {
                vm.$message.error("请先添加连接信息再更新")
            })
        },
        save:function(){
            this.$refs.ruleForm.validate(function (res) {
                if(res){
                    $.ajax({
                        url:vm.type == "save" ? "/remote/monitoring/mysql/save" : "/remote/monitoring/mysql/update/" + vm.select,
                        type:vm.type == 'save' ? "POST" : "PUT",
                        dataType:"JSON",
                        contentType:"application/json;charset=utf-8",
                        data:JSON.stringify(vm.mysqlInfo),
                        success:function(res){
                            if(res.code == 200){
                                vm.dialogVisible = false
                                if(vm.type == 'save'){
                                    vm.mysqlInfoList.push(res.data);
                                    vm.loadData(res.data.id);
                                    vm.select = res.data.id
                                }else{
                                    vm.loadMysqlInfoList()
                                }
                            }else{
                                vm.$message.error(res.msg)
                            }
                        },
                        error:function(err){
                            vm.$message.error("服务器出现了异常")
                        }
                    })
                }
            })
        },
        loadMysqlInfoList:function(){
            get({
                url:"/remote/monitoring/mysql/list"
            }).then(function(res){
                if(res.code == 200){
                    vm.mysqlInfoList = res.data;
                    if(res.data.length > 0){
                        vm.select = res.data[0].id;
                        vm.mysqlInfo = res.data[0];
                        vm.loadData(vm.select)
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
                url: "/monitoring/mysql/state"
            }).then(function (res) {
                if (res.code == 200) {
                    vm.list = []
                    vm.initLine(res.data);
                    vm.initPie(res.data);
                    vm.initList("", res.data);
                }
            }).catch(function (err) {
                console.log(err)
            })
        },
        loadData: function (id) {
            get({
                url: "/remote/monitoring/mysql/state/" + id
            }).then(function (res) {
                if (res.code == 200) {
                    vm.list = []
                    vm.initLine(res.data);
                    vm.initPie(res.data);
                    vm.initList("", res.data);
                }
            }).catch(function (err) {
                console.log(err)
            })
        },
        initLine: function (data) {
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
            }
            for (key in data.tableRowCounts){
                vm.line.xAxis.data.push(key);
                vm.line.series[0].data.push(data.tableRowCounts[key])
            }
            var myChart = echarts.init(document.getElementById('line'));
            myChart.setOption(this.line);
        },
        initPie: function (data) {
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
                        name:'系统信息',
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
            var serverInfo = data.serverInfo;
            for (key in serverInfo){
                vm.pie.series[0].data.push({
                    value: serverInfo[key],
                    name: key + "-->" + serverInfo[key]
                });
                vm.pie.legend.data.push( key + "-->" + serverInfo[key])

            }
            var myChart = echarts.init(document.getElementById('pie'));
            myChart.setOption(this.pie);
        },
        initList: function (preKey, data) {
            for (key in data) {
                if (key != 'serverInfo' & key != 'tableRowCounts') {
                    if (data[key] instanceof Object) {
                        if (!preKey) {
                            vm.initList(key, data[key])
                        } else {
                            vm.initList(preKey + "." + key, data[key])
                        }
                    } else {
                        if (!preKey) {
                            vm.list.push({
                                key: key,
                                value: data[key]
                            })
                        } else {
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