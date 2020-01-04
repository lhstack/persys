var vm = new Vue({
    el: "#app",
    data:function(){
        return {
            /**
             * 任务模型
             */
            task:{
                parameterTypes:[],
                parameters:[]
            },
            loading: true,
            /**
             * 表格数据
             */
            data:[],
            /**
             * 分页数据
             */
            pager:{
                currentPage: 1,
                pageSize: 10,
                total: 0
            },

            options:{
                height: 500,
                dialogHeight:350,
                filterBtn:false,
                editBtn: true,
                excelBtn: true,
                menu: true,
                selection: true,
                selectClearBtn: true,
                searchShow: false,
                printBtn: true,
                viewBtn: false,
                border:true,
                addBtn: false,
                align:'center',
                menuAlign:'center',
                column:[
                    {
                        label:'ID',
                        prop:'id',
                        slot: true,
                        hide: true,
                        disabled: true,
                        addDisplay: true
                    },
                    {
                        label:'bean名称',
                        prop:'beanName',
                        slot: true,
                        sortable: true,
                        rules: [{
                            required: true,
                            message: "请输入bean名称",
                            trigger: "blur"
                        }]
                    }, {
                        label:'bean方法',
                        prop:'method',
                        slot: true,
                        sortable: true,
                        rules: [{
                            required: true,
                            message: "请输入bean方法",
                            trigger: "blur"
                        }]
                    }, {
                        label:'调度器类型',
                        prop:'type',
                        slot: true,
                        sortable: true,
                        type:"select",
                        formslot:true
                    }, {
                        label:'任务状态',
                        prop:'state',
                        slot: true,
                        sortable: true,
                        editDisabled: true,
                        addDisplay: false
                    },{
                        label:'表达式',
                        prop:'express',
                        slot: true,
                        sortable: true,
                        formslot:true,
                        rules: [{
                            required: true,
                            message: "请输入任务调度表达式",
                            trigger: "blur"
                        }]
                    },{
                        label:'创建时间',
                        prop:'createTime',
                        slot: true,
                        type: "datetime",
                        sortable: true,
                        disabled: true,
                        addDisplay: false
                    },{
                        label:"任务注释",
                        prop: "description",
                        slot: true
                    },{
                        label:"任务名称",
                        prop: "jobName",
                        slot: true,
                        hide: true,
                        rules: [{
                            required: true,
                            message: "请输入任务名称",
                            trigger: "blur"
                        }],
                        addDisplay: false
                    },{
                        label:"任务组",
                        prop: "jobGroup",
                        slot: true,
                        hide: true,
                        rules: [{
                            required: true,
                            message: "请输入任务组",
                            trigger: "blur"
                        }],
                        addDisplay: false
                    },{
                        label:"触发器名",
                        prop: "triggerName",
                        slot: true,
                        hide: true,
                        rules: [{
                            required: true,
                            message: "请输入触发器名称",
                            trigger: "blur"
                        }],
                        addDisplay: false
                    },{
                        label:"触发器组",
                        prop: "triggerGroup",
                        slot: true,
                        hide: true,
                        rules: [{
                            required: true,
                            message: "请输入触发器组",
                            trigger: "blur"
                        }],
                        addDisplay: false
                    },{
                        label:"任务参数",
                        prop: "parameters",
                        slot: true,
                        hide: true,
                        formslot: true
                    }
                ]
            }
        }
    },
    filters:{
        /**
         * 类型过滤
         * @param old
         * @returns {string}
         */
        typeFilter:function(old){
            if(old && typeof(old) == 'string'){
                switch (old.toLowerCase()) {
                    case "calendar":{
                        return "日历"
                    }
                    case "simple":{
                        return "时间"
                    }
                    default:{
                        return old;
                    }
                }

            }
            return "NONE";
        }
    },
    methods:{

        /**
         * 触发一次任务
         * @param row
         */
        runOnce:function(row){
            get({
                url:"/quartz/runOnce/" + row.id
            }).then(function(res){
                if(res.code == 200){
                    vm.$message.success("触发任务成功");
                    vm.loadTask();
                }else{
                    vm.$message.error("触发任务失败")
                }
            }).catch(function(err){
                vm.$message.error("触发任务失败")
            })
        },
        /**
         * 暂停所有任务
         */
        pauseAll:function(){
            $.ajax({
                url:"/quartz/pauseAll",
                type:"PUT",
                dataType:"json",
                contentType:"application/json;charset=utf-8",
                success:function(res){
                    if(res.code == 200){
                        vm.$message.success("暂停任务成功");
                        vm.loadTask();
                    }else{
                        vm.$message.error("暂停任务失败");
                    }
                },
                error:function(err){
                    vm.$message.error("暂停任务失败");
                }
            });
        },
        /**
         * 运行所有任务
         */
        runAll:function(){
            $.ajax({
                url:"/quartz/runAll",
                type:"PUT",
                dataType:"json",
                contentType:"application/json;charset=utf-8",
                success:function(res){
                    if(res.code == 200){
                        vm.$message.success("运行任务成功");
                        vm.loadTask();
                    }else{
                        vm.$message.error("运行任务失败");
                    }
                },
                error:function(err){
                    vm.$message.error("运行任务失败");
                }
            });
        },
        /**
         * 显示添加表单
         */
        addForm:function(){
          this.task = {
              parameterTypes:[],
              parameters:[],
              type: "simple"
          };
          this.$refs.crud.rowAdd();
        },
        /**
         * 添加表单保存事件
         * @param row
         * @param done
         * @param loading
         */
        addRollback:function(row,done,loading){
           $.ajax({
               url:"/quartz/add",
               type:"POST",
               data:JSON.stringify(this.task),
               dataType:"json",
               contentType:"application/json;charset=utf-8",
               success:function(res){
                   if(res.code == 200){
                       vm.$message.success("任务添加成功");
                       vm.loadTask();
                       done();
                   }else{
                       vm.$message.error("任务添加失败");
                       loading();
                   }
               },
               error:function(err){
                   vm.$message.error("任务添加失败");
                   loading();
               }
           })
        },
        /**
         * 删除表单任务调度参数
         * @param index
         */
        removeParameter:function(index){
            this.task.parameters.splice(index,1);
            this.task.parameterTypes.splice(index,1);
        },
        /**
         * 表单添加参数按钮事件
         */
        addParameters:function(){
            if(!this.task.parameters){
                this.task.parameters = []
            }
            if(!this.task.parameterTypes){
                this.task.parameterTypes = []
            }
            this.task.parameters.push("NULL");
            this.task.parameterTypes.push("java.lang.String")
        },
        /**
         * 编辑表单修改按钮事件
         * @param row
         * @param index
         * @param done
         * @param loading
         */
        update:function(row,index,done,loading){
            $.ajax({
                url:"/quartz/update/" + this.task.id,
                type:"PUT",
                data:JSON.stringify(this.task),
                dataType:"json",
                contentType:"application/json;charset=utf-8",
                success:function(res){
                    if(res.code == 200){
                        vm.$message.success("更新任务成功");
                        vm.loadTask();
                        done();
                    }else{
                        vm.$message.error("更新任务失败");
                        loading();
                    }
                },
                error:function(err){
                    vm.$message.error("更新任务失败");
                    loading();
                }
            });

        },
        /**
         * 改变任务状态
         * @param task
         */
        updateState:function(task){
            console.log(task)
            this.confirm({
                title:task.state == 'NORMAL' ? '确认暂停任务吗?':"确认运行任务吗"
            },function(){
                update({
                    url:task.taskState ? "/quartz/pause/" + task.id : "/quartz/resume/" + task.id,
                }).then(function(res){
                    if(res.code == 200){
                        vm.$message.success("任务暂停");
                        vm.loadTask();
                    }else{
                        vm.$message.error("任务暂停失败");
                    }
                }).catch(function(err){
                    vm.$message.error("任务暂停失败");
                })
            })
        },
        /**
         * 表达式过滤
         * @param express
         * @returns {string}
         */
        getExpressToString:function(express){
            if(express && typeof(express) == 'string'){
                var exps = express.split("@");
                if(exps.length >= 2){
                    var msg = exps[0];
                    switch (exps[1].toLowerCase()) {
                        case "s":{
                            return msg + "秒" + (exps.length == 3 ? "执行" + exps[2] + '次':"1次");
                        }
                        case "m":{
                            return msg + "分钟" + (exps.length == 3 ? "执行" + exps[2] + '次':"1次");
                        }
                        case "h":{
                            return msg + "小时" + (exps.length == 3 ? "执行" + exps[2] + '次':"1次");
                        }
                        case "day":{
                            return msg + "天" + (exps.length == 3 ? "执行" + exps[2] + '次':"1次");
                        }
                        case "week":{
                            return msg + "周" + (exps.length == 3 ? "执行" + exps[2] + '次':"1次");
                        }
                        case "month":{
                            return msg + "月" + (exps.length == 3 ? "执行" + exps[2] + '次':"1次");
                        }
                        case "year":{
                            return msg + "年" + (exps.length == 3 ? "执行" + exps[2] + '次':"1次");
                        }
                        case "count_s":{
                            return "每秒执行" + msg + "次";
                        }
                        case "count_m":{
                            return "每秒执行" + msg + "次";
                        }
                        case "count_h":{
                            return "每秒执行" + msg + "次";
                        }
                        default:{
                            return express;
                        }
                    }
                }
            }
            return express;
        },
        /**
         * 返回任务状态按钮样式
         * @param state
         * @returns {*}
         */
        getBtnType:function(state){
            if(state && typeof(state) == 'string'){
                switch (state.toUpperCase()) {
                    case "NORMAL":{
                        return "success";
                    }
                    case "PAUSED":{
                        return "primary";
                    }
                    case "NONE":{
                        return "info";
                    }
                    case "ERROR":{
                        return "danger";
                    }
                    case "BLOCKED":{
                        return "warning";
                    }
                    default:{
                        return "text"
                    }
                }
            }
            return state;
        },
        confirm:function(options,fn,reject){
            this.$confirm(options.title || "确认删除吗?",options.msg || "警告",{
                type: options.type || "warning"
            }).then(fn).catch(reject || function(){})
        },
        //删除选中的行
        delSelect:function(){
            var ids = this.$refs.crud.tableSelect.map(function(item){
                return item.id;
            });
            if(ids.length < 1){
                this.$message.warning("请选择要删除的行");
                return ;
            }
            this.confirm({
                title:"确认批量删除吗?",
                msg:"危险",
                type:"error"
            },function(){
                var load = vm.$loading({text:"删除中..."});
                requestDel({
                    url:"/quartz/deletes/" + ids.join(",")
                }).then(function(res){
                    load.close();
                    if(res.code == 200){
                        vm.$message.success("删除成功");
                        vm.loadTask();
                    }else{
                        vm.$message.error("删除失败");
                    }
                }).catch(function(err){
                    load.close();
                    vm.$message.error("删除失败");
                })
            })
        },
        //删除按钮事件
        del:function(data,index){
            this.confirm({},function(){
                var id = data.id;
                var load = vm.$loading({text:"移除任务中..."});
                requestDel({
                    url:"/quartz/delete/" + id
                }).then(function(res){
                    load.close();
                    if(res.code == 200){
                        vm.$message.success("移除任务成功");
                        vm.loadTask();
                    }else{
                        vm.$message.error("移除任务失败");
                    }
                }).catch(function(err){
                    load.close();
                    vm.$message.error("移除任务失败");
                })
            })
        },
        //刷新按钮
        refreshChange:function(){
            var load = this.$loading({text:"刷新中..."});
            this.loadTask(function(){
                load.close();
            },function(){
                vm.$message.success("刷新成功")
            },function(){
                vm.$message.error("刷新失败")
            });
        },
        //分页显示条数改变时回调
        sizeChange:function(size){
            this.pager.pageSize = size;
            this.loadTask();
        },
        //分页改变页数时回调
        currentChange:function(page){
            this.pager.currentPage = page;
            this.loadTask();
        },
        //加载任务
        loadTask:function(fn,success,error){
            this.loading = true;
            $.ajax({
                url: "/quartz/list?page=" + this.pager.currentPage + "&size=" + this.pager.pageSize,
                type:"GET",
                dataType:"JSON",
                contentType:"application/json;charset=utf-8",
                success:function(res){
                    if(res.code == 200){
                        var d = res.data;
                        vm.pager.total = res.count;
                        vm.data = d;
                        if(success){
                            success();
                        }
                    }else{
                        if(error){
                            error();
                        }
                    }
                    if(fn){
                        fn();
                    }
                    vm.loading = false;
                },
                error:function(err){
                    if(fn){
                        fn();
                    }
                    if(error){
                        error();
                    }
                    vm.loading = false;
                }
            });
        }
    },
    mounted:function(){
        this.loadTask();
    }
});