var vm = new Vue({
    el:"#app",
    data:function(){
        return {
            user:{},
            email:{
                intervalId:"",
                valid:""
            },
            validText:"获取"
        }
    },
    methods:{
        toReset:function(){
            localStorage.setItem("registryPage",window.location.href);
            localStorage.setItem("loginPage",this.$refs.href.href);
            window.location.href = "/page/reset@index";
        },
        checkEmail:function(){
            var emailRegex = /^([0-9A-Za-z\-_.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g;
            if(emailRegex.test(this.user.email)){
                return true;
            }
            this.$message.error("邮箱格式有误，请重新输入");
            return false;
        },

        /**
         * 修改获取验证码按钮
         */
        changeValidText: function () {
            this.validText = 60;
            this.email.intervalId = setInterval(function () {
                if (vm.validText == 1) {
                    clearInterval(vm.email.intervalId);
                    vm.email.intervalId = "";
                    vm.validText = "获取"
                } else {
                    vm.validText -= 1;
                }
            }, 1000);
        },

        checkUserInfo:function(){
            var strRegex = /^[\w]{5,18}$/;
            var emailRegex = /^([0-9A-Za-z\-_.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g;
            if(!this.user.nickName || this.user.nickName < 1){
                this.$message.warning("请填写昵称");
                return false;
            }

            if(!this.user.username || !strRegex.test(this.user.username)){
                this.$message.warning("请填写用户名(5-18)字符数字");
                return false;
            }

            if(!this.user.password || !strRegex.test(this.user.password)){
                this.$message.warning("请填写密码(5-18)字符数字");
                return false;
            }

            if(!emailRegex.test(this.user.email)){
                this.$message.warning("邮箱格式有误，请重新输入");
                return false;
            }

            if(!this.email.valid){
                this.$message.warning("请填写验证码");
                return false;
            }

            return true;
        },
        /**
         * 注册按钮
         */
        submit:function(){
            if(this.checkUserInfo()){
                $.ajax({
                    url:"/user/reg?code=" + this.email.valid,
                    method:"POST",
                    dataType:"JSON",
                    contentType:"application/json;charset=utf-8",
                    data:JSON.stringify(this.user),
                    success:function(res){
                        if(res.code == 200){
                            vm.$message.success(res.msg + "\n3秒之后跳转到登陆页面");
                            setTimeout(function(){
                                window.location.href = vm.$refs.href.href
                            },3000)
                        }else{
                            vm.$message.error(res.msg)
                        }
                    },
                    error:function(err){
                        vm.$message.error("注册失败")
                    }
                })
            }
        },
        /**
         * 发送邮箱验证码
         */
        sendEmailValid:function(){
            if (!this.email.intervalId) {
                if (this.checkEmail()) {
                    post({
                        url: "/user/changeSendValid?to=" + this.user.email
                    }).then(function (res) {
                        if (res.code == 200) {
                            vm.$message.success("邮件发送成功，请登录邮箱进行查看");
                            vm.changeValidText();
                        } else {
                            vm.$message.error(res.msg)
                        }
                    }).catch(function (err) {
                        vm.$message.error("邮件发送失败")
                    })
                }
            }
        }
    }
});