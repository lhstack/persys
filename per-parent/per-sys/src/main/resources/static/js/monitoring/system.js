function randomColor(){
    return `rgb(${parseInt(Math.random() * 255)},${parseInt(Math.random() * 255)},${parseInt(Math.random() * 255)})`
}

var vm = new Vue({
    el:"#app",
    data:function(){
        return {
            line: {
                title: {
                    text: '系统信息'
                },
                tooltip: {},
                legend: {
                    data:['剩余空间',"已用空间","最大空间"]
                },
                xAxis: {
                    data: []
                },
                yAxis: {},
                series: [{
                    name: '剩余空间',
                    type: 'line',
                    data: [],
                    animation: true,
                    smooth: true
                },
                    {
                        name: '已用空间',
                        type: 'line',
                        data: [],
                        animation: true,
                        smooth: true
                    },
                    {
                        name: '最大空间',
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
                        name:'磁盘情况',
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
            list:[]
        }
    },
    created:function(){
        this.loadData();
    },
    mounted:function(){

    },
    methods:{
      loadData:function(){
          get({
              url:"/monitoring/system/state"
          }).then(function(res){
              if(res.code == 200){
                  vm.initLine(res.data);
                  vm.initPie(res.data);
                  vm.initList("",res.data);
              }
          }).catch(function(err){
              console.log(err)
          })
      },
      initLine:function (data) {
          for (key in data.deskInfo){
              vm.line.xAxis.data.push(key);
              vm.line.series[0].data.push(data.deskInfo[key].freeSpace)
              vm.line.series[1].data.push(data.deskInfo[key].totalSpace - data.deskInfo[key].usableSpace)
              vm.line.series[2].data.push(data.deskInfo[key].totalSpace)
          }
          var myChart = echarts.init(document.getElementById('line'));
          myChart.setOption(this.line);
      },
      initPie:function(data){
          var systemInfo = data.systemInfo;
          vm.pie.legend.data.push("已用物理空间")
          vm.pie.series[0].data.push({
              name:"已用物理空间",
              value: systemInfo.totalPhysicalMemorySize - systemInfo.freePhysicalMemorySize
          });
          vm.pie.legend.data.push("最大物理空间")
          vm.pie.series[0].data.push({
              name:"最大物理空间",
              value: systemInfo.totalPhysicalMemorySize
          });
          vm.pie.legend.data.push("最大虚拟空间")
          vm.pie.series[0].data.push({
              name:"最大虚拟空间",
              value: systemInfo.totalSwapSpaceSize
          });
          vm.pie.legend.data.push("已用虚拟空间")
          vm.pie.series[0].data.push({
              name:"已用虚拟空间",
              value: systemInfo.totalSwapSpaceSize - systemInfo.freeSwapSpaceSize
          });
          var myChart = echarts.init(document.getElementById('pie'));
          myChart.setOption(this.pie);
      },
      initList:function(preKey,data){
          for(key in data){
              if(key != 'deskInfo' & key != 'totalPhysicalMemorySize' & key != 'freePhysicalMemorySize' & key != 'totalSwapSpaceSize' & key != 'freeSwapSpaceSize'){
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