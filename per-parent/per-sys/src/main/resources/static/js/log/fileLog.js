var vm = new Vue({
    el:"#app",
    data:function(){
        return {
            data:[],
            root:[],
            directIcon:"/images/director.png",
            fileIcon:"/images/logFile.png",
            history:[{
                name: "root",
                title: "/",
                method: "loadRoot",
                fullPath: "/",
                args: ""
            }],
            content: "",
            dialogVisible: false,
            title: ""
        }
    },
    methods:{
        loadDirectorChild:function(item){
            var flag = true;
            var index = -1;
            this.history.forEach(function(i,dex){
                if(i.name == item.fileName){
                    flag = false;
                    if(dex){
                        index = dex;
                    }
                }
            });
            if(flag){
                this.history.push({
                    name: item.fileName,
                    fullPath: item.fullPath,
                    method: "loadChild",
                    title: item.fileName,
                    args: item
                });
            }else{
                this.history.splice(index + 1);
            }
            var load = this.$loading({text:"加载中..."});
            $.ajax({
                url:"/log/file/childDirector",
                method:"POST",
                data: JSON.stringify(item),
                dataType: "json",
                contentType:"application/json;charset=utf-8",
                success:function(res){
                    if(res.code == 200){
                        var data = res.data.map(function(item){
                            if(item.fileType == "DIRECTOR"){
                                item.icon = vm.directIcon;
                            }else{
                                item.icon = vm.fileIcon
                            }
                            return item;
                        });
                        vm.data = data;
                    }else{
                        vm.$message.error("加载失败")
                    }
                    load.close();
                },
                error:function(err){
                    load.close();
                    vm.$message.error("加载失败")
                }
            });
        },
        loadLogFile:function(item){
            if(item.size >= 3050944){
                this.$confirm("此文件太大，要查看只有下载","提示",{
                    type:"warn"
                }).then(function(){
                    window.location.href = "/log/file/download/?fullPath=" + item.fullPath.replace(/(\/|\\)/g,"@");
                }).catch(function(){

                })

                return ;
            }
            this.content = "";
            this.dialogVisible = true;
            this.title = item.fileName;
            var load = this.$loading({text:"加载中..."});
            axios.post("/log/file/childFile",item,{
                timeout: 6000
            }).then(function(res){
                if(res.data.code == 200){
                    vm.content = res.data.data.map(function(iem){
                        if(item.fileName.indexOf("debug") >= 0){
                            iem = "<span class='log_hover' style='color: #1b6d85;padding-bottom: 1px;display: block;width: 90%;margin: auto'>" + iem + "</span>"
                        }
                        if(item.fileName.indexOf("info") >= 0){
                            iem = "<span class='log_hover' style='color: #01C27B;padding-bottom: 1px;display: block;width: 90%;margin: auto'>" + iem + "</span>"
                        }
                        if(item.fileName.indexOf("warn") >= 0){
                            iem = "<span class='log_hover' style='color: #D3D60B;padding-bottom: 1px;display: block;width: 90%;margin: auto'>" + iem + "</span>"
                        }
                        if(item.fileName.indexOf("error") >= 0){
                            iem = "<span class='log_hover' style='color: #DC201A;padding-bottom: 1px;display: block;width: 90%;margin: auto'>" + iem + "</span>"
                        }
                        if(item.fileName.indexOf("all") >= 0){
                            iem = "<span class='log_hover' style='color: #a6a9ad;padding-bottom: 1px;display: block;width: 90%;margin: auto'>" + iem + "</span>"
                        }
                        return iem;
                    }).join();
                    vm.$message.success("加载成功");
                }
                load.close();
            }).catch(function(err){
                load.close();
            })
        },
        //加载子目录
        loadChild:function(item){
          if(item.fileType == "DIRECTOR"){
                this.loadDirectorChild(item);
          }else{
              this.loadLogFile(item);
          }
        },
        loadMethod:function(method,args){
          var m = this[method];
          m(args);
        },
        loadRoot:function(){
            this.history.splice(1)
            if(this.root.length > 0){
                this.data = this.root;
            }else{
                get({
                    url:"/log/file/root"
                }).then(function(res){
                    if(res.code == 200){
                        var data = res.data.map(function(item){
                            if(item.fileType == "DIRECTOR"){
                                item.icon = vm.directIcon;
                            }else{
                                item.icon = vm.fileIcon
                            }
                            return item;
                        });
                        vm.root = data;
                        vm.data = data;
                    }
                }).catch(function(err){

                })
            }
        }
    },
    mounted:function(){
        this.loadRoot();
    }
});