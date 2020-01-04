var vm = new Vue({
    el:"#app",
    data:function(){
        return {
            validText: "发送",
            email: "",
            validCode:"",
            msg:"",
            intervalId:"",
            page:0,
            newPass1:"",
            newPass2:""
        }
    },
    created:function(){

    },
    mounted:function(){

    },
    methods:{
        submit:function(){
            var passRegex = /^\w{5,18}$/
            if(!passRegex.test(this.newPass1)){
                this.msg = "密码格式不正确，密码应该是5-18位的字母数字"
                return ;
            }
            if(this.newPass1 != this.newPass2){
                this.msg = "两次密码不一致";
                return ;
            }
            $.ajax({
                url:"/user/reset/pass",
                dataType:"json",
                type:"post",
                contentType:"application/json;charset=utf-8",
                data:JSON.stringify({
                    email: vm.email,
                    validCode: vm.validCode,
                    newPass: vm.newPass1
                }),
                success:function(res){
                    if(res.code == 200){
                        vm.$message.success("重置密码成功，1s后跳转登陆页面");
                        setTimeout(function () {
                            var logPage = localStorage.getItem("loginPage");
                            window.location.href = logPage ? logPage : "/login.html";
                        },1000)
                    }else{
                        vm.$message.error(res.msg)
                    }
                },
                error:function(err){
                    vm.$message.error("重置密码出现异常，请稍后重试")
                }
            })
        },
        next:function(){
            var emailRegex = /^([0-9A-Za-z\-_.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g;
            if(!emailRegex.test(this.email)){
                this.msg = "邮箱格式不合法"
                return ;
            }
            if(!this.validCode || this.validCode.trim() == ''){
                this.msg = "请输入验证码"
                return ;
            }
            post({
                url:"/user/pass/reset/valid?to=" + this.email + "&validCode=" + this.validCode
            }).then(function(res){
                if(res.code == 200){
                    vm.page = 1
                }else{
                    vm.$message.error(res.msg);
                }
            }).catch(function(err){
                vm.$message.error("验证失败，请重新获取验证码");
            })
        },
        send:function(){
            var emailRegex = /^([0-9A-Za-z\-_.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g;
            if(!emailRegex.test(this.email)){
                this.msg = "邮箱格式不合法"
                return ;
            }
            if(!this.intervalId)
            {
                post({
                    url:"/mail/send/reset?to=" + this.email
                }).then(function(res){
                    if(res.code == 200){
                        vm.$message.success("验证码发送成功")
                        vm.validText = "59";
                        vm.intervalId = setInterval(function(){
                            if(vm.validText == 0){
                                vm.validText = "发送";
                                clearInterval(vm.intervalId)
                                vm.intervalId = ""
                            }else{
                                vm.validText --;
                            }
                        },1000)
                    }else{
                        vm.$message.error(res.msg)
                    }
                }).catch(function(err){
                    vm.$message.error("服务器出现异常");
                })
            }

        },
        toLogin:function(){
            var loginPage =  localStorage.getItem("loginPage");
            window.location.href = loginPage ? loginPage : "/login.html"
        },
        toRegistry:function(){
            var loginPage =  localStorage.getItem("registryPage");
            window.location.href = loginPage ? loginPage : "/sso/registry.html"
        }
    }
});