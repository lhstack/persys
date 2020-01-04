function randomColor() {
    return `rgb(${parseInt(Math.random() * 255)},${parseInt(Math.random() * 255)},${parseInt(Math.random() * 255)})`
}

var vm = new Vue({
    el: "#app",
    data: function () {
        return {
            line: {
                title: {
                    text: '状态信息统计'
                },
                tooltip: {},
                legend: {
                    data:['统计数']
                },
                xAxis: {
                    data: []
                },
                yAxis: {},
                series: [{
                    name: '统计数',
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
                        name:'连接信息',
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
            redisList:[],
            select:"",
            dialogVisible: false,
            redisInfo:{
                dbHost:""
            },
            rules: {
                dbHost: [
                    { required: true, message: '请输入RedisHost地址', trigger: 'blur' }
                ]
            },
            type:""
        }
    },
    created: function () {
        this.loadRedisInfoList()
    },
    mounted: function () {

    },
    methods: {
        del:function(){
            this.$confirm("是否删除",{
                type:"warning"
            }).then(function(){
                requestDel({
                    url:"/remote/monitoring/redis/del/" + vm.select
                }).then(function(res){
                    if(res.code == 200){
                        vm.$message.success("删除成功");
                        vm.loadRedisInfoList();
                    }else{
                        vm.$message.error(res.msg)
                    }
                }).catch(function(err){
                    vm.$message.error("删除失败")
                })
            }).catch(function(err){

            })
        },
        save:function(){
           this.$refs.ruleForm.validate(function(res){
               if(res){
                   $.ajax({
                       url:vm.type == "save" ?"/remote/monitoring/redis/save" : "/remote/monitoring/redis/update/" + vm.redisInfo.id,
                       contentType:"application/json;charset=utf-8",
                       data:JSON.stringify(vm.redisInfo),
                       type:vm.type == "save" ? "POST" : "PUT",
                       dataType:"JSON",
                       success:function(res){
                           if(res.code == 200){
                              if(vm.type == 'save'){
                                  vm.redisList.push(res.data)
                                  vm.dialogVisible=false;
                                  vm.loadData(res.data.id);
                              }else{
                                  vm.dialogVisible=false;
                                  vm.loadRedisInfoList();
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
        update:function(){
            this.type='update';
            get({
                url:"/remote/monitoring/redis/find/" + this.select
            }).then(function (res) {
                if(res.code == 200){
                    vm.dialogVisible=true;
                    vm.redisInfo = res.data;
                    vm.select = res.data.id
                }else{
                    vm.$message.error(res.msg)
                }
            }).catch(function (err) {
                vm.$message.error("请先添加连接信息再更新")
            })
        },
        change:function(em){
            this.line = {
                title: {
                    text: '状态信息统计'
                },
                tooltip: {},
                legend: {
                    data:['统计数']
                },
                xAxis: {
                    data: []
                },
                yAxis: {},
                series: [{
                    name: '统计数',
                    type: 'line',
                    data: [],
                    animation: true,
                    smooth: true
                }]
            };
            this.pie = {
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
                        name:'连接信息',
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
            this.list = []
            this.loadData(em);
        },
        loadRedisInfoList:function(){
            get({
                url:"/remote/monitoring/redis/list"
            }).then(function(res){
                if(res.code == 200){
                    vm.redisList = res.data;
                    if(res.data.length > 0){
                        vm.loadData(vm.redisList[0].id);
                        vm.select = vm.redisList[0].id;
                    }else{
                        vm.loadMyData()
                    }
                }else{
                    vm.loadMyData();
                }
            }).catch(function(err){

            })

        },
        loadMyData:function(){
            get({
                url: "/monitoring/redis/state"
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
                url: "/remote/monitoring/redis/state/" + id
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
            for (key in data.stats){
                vm.line.xAxis.data.push(key);
                vm.line.series[0].data.push(data.stats[key])
            }
            var myChart = echarts.init(document.getElementById('line'));
            myChart.setOption(this.line);
        },
        initPie: function (data) {
            var clients = data.clients;
            for (key in clients) {
                vm.pie.series[0].data.push({
                    value: clients[key],
                    name: key + "-->" + clients[key]
                });
                vm.pie.legend.data.push(key + "-->" + clients[key])
            }
            var myChart = echarts.init(document.getElementById('pie'));
            myChart.setOption(this.pie);
        },
        initList: function (preKey, data) {
            for (key in data) {
                if (key != 'stats' & key != 'clients') {
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