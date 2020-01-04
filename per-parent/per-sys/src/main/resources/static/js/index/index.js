//JavaScript代码区域
function logout(){
    post({
        url:"/logout"
    }).then(function(res){
        if(res.code == 200){
            layer.msg(res.msg,{icon: 6});
            setTimeout(function(){
                window.location.reload()
            },1000)
        }else{
            layer.msg("退出登录失败",{icon: 5});
        }
    }).catch(function (err) {
        layer.msg("退出登录失败",{icon: 5});
    })
}

$(function(){
    layui.use('element', function(){
        var element = window.$$element = layui.element;

    });
    layui.use("layer",function(){
        var layer = layui.layer;
    });
});
var vm = window.$$vm = new Vue({
    el:"#app",
    data:function(){
        return {
            menus: [],
            crumbs:[],
            crumbsIndex:0,
            crumbsLength: 5,
            user:{}
        }
    },
    mounted:function(){
        this.loadMenu();
        this.loadUserInfo();
    },
    methods:{
        loadUserInfo:function(){
            get({
                url:"/user/info"
            }).then(function(res){
                if(res.code == 200){
                    vm.user = res.data
                }
            })
        },
        loadMenu:function(){
            get({
                url:"/menu/list",
                method:"get"
            }).then(function(res){
                vm.menus = res.data;
                setTimeout(function(){
                    $$element.init();
                },200)
            }).catch(function(err){
                vm.menus = staticMenu;
                setTimeout(function(){
                    $$element.init();
                },200)
            })
        },
        toBody:function(item){
            this.addCrumbs(item);
            var load = vm.$loading({target:"#loading_body",text:"加载中..."});
            var iframe = $("#body")[0];
            iframe.src=item.href;
            iframe.addEventListener( "load", function(){
                load.close();
                this.removeEventListener( "load", arguments.call, false);
            }, false);
            setTimeout(function(){
                load.close();
            },3000)
        },
        crumbClick:function(item){
            this.crumbs.forEach(function(e,v){
                if(e.menuName == item.menuName){
                    vm.crumbsIndex = v
                }
            });
            var load = vm.$loading({target:"#loading_body",text:"加载中..."});
            var iframe = $("#body")[0];
            iframe.src=item.href;
            iframe.addEventListener( "load", function(){
                load.close();
                this.removeEventListener( "load", arguments.call, false);
            }, false);
            setTimeout(function(){
                load.close();
            },3000)
        },
        addCrumbs:function(item){
            var exist = this.crumbs.filter(function(em){
                if(em.menuName == item.menuName){
                    return true;
                }
                return false;
            });
            if(exist.length == 0){
                if(this.crumbs.length >= this.crumbsLength){
                    this.crumbs.splice(0,1);
                    this.crumbs.push({
                        menuName:item.menuName,
                        href:item.href
                    });
                    this.crumbsIndex = this.crumbsLength - 1
                }else{
                    this.crumbs.push({
                        menuName:item.menuName,
                        href:item.href
                    });
                    this.crumbsIndex = this.crumbs.length - 1
                }
            }else{
                this.crumbs.forEach(function(e,v){
                    if(e.menuName == item.menuName){
                        vm.crumbsIndex = v
                    }
                })
            }
        },
        closeTab:function(index){
            this.crumbsIndex = index;
            this.crumbs.splice(index,1)
        }
    }
})
