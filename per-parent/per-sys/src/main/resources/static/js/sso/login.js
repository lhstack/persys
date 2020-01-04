var vm = new Vue({
    el:"#app",
    data:function(){
        return {
            code:"/code",
            user:{

            },
            strRegex: /^\w{5,18}$/,
            info:staticInfo
        }
    },
    methods:{
        toReset:function(){
            localStorage.setItem("loginPage",window.location.href);
            localStorage.setItem("registryPage","/sso/registry");
            window.location.href = "/page/reset@index";
        },
        submit:function(){

            if(this.validUsername() && this.validPassword() && this.validCodeValid()){
                this.$refs.form.submit();
                /*$.ajax({
                    url:"/sso/login",
                    type:"POST",
                    dataType:"json",
                    contentType:"application/json;charset=utf-8",
                    data:JSON.stringify(vm.user),
                    success:function(res){
                        if(res.code == 200){

                        }else{
                            vm.code = vm.code + "?random=" + Math.random() * 10000;
                            vm.info = res.msg;
                        }
                    },
                    error:function(err){
                        vm.code = vm.code + "?random=" + Math.random() * 10000;
                        vm.info = "服务器出现了异常，请稍后重新登录"
                    }
                })*/
            }

        },
        validCodeValid:function(){
            if(!this.user.validCode || this.user.validCode < 1){
                this.info = "请输入验证码";
                return false;
            }else{
                this.info = "";
                return true;
            }
        },
        validUsername:function(){
            if(!this.user.username || !this.strRegex.test(this.user.username)){
                this.info = "用户名输入有误，用户名长度应该是5-18位的字符";
                return false;
            }else{
                this.info = "";
                return true;
            }
        },
        validPassword:function(){
            if(!this.user.password || !this.strRegex.test(this.user.password)){
                this.info = "密码输入有误，密码长度应该是5-18位的字符";
                return false;
            }else{
                this.info = "";
                return true;
            }
        }
    },
    mounted:function(){

    }
});