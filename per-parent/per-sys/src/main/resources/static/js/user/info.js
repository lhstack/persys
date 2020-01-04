var vm = new Vue({
    el: "#app",
    data: function () {
        return {
            form: {},
            isEmailDialogVisible: false,
            editTitle: "修改邮箱",
            email: {
                newEmail1: "",
                newEmail2: "",
                valid: "",
                intervalId: ""
            },
            pass: {
                oldPass: "",
                newPass1: "",
                newPass2: "",
                valid: "",
                intervalId: ""
            },
            validText: "获取验证码",
            isPasswordDialogVisible: false
        }
    },
    methods: {

        onSubmitPass:function(){
            if(!this.checkPass()){
               return ;
            }
            if(!this.pass.valid ){
                this.$message.warning("请填写验证码");
                return;
            }

            $.ajax({
                url:"/user/editPass",
                dataType:"JSON",
                data:JSON.stringify({
                    validCode: vm.pass.valid,
                    oldPass: vm.pass.oldPass,
                    newPass: vm.pass.newPass1
                }),
                contentType:"application/json;charset=utf-8",
                type:"POST",
                success:function(res){
                    if(res.code == 200){
                        window.parent.location.reload();
                    }else{
                        vm.$message.error(res.msg)
                    }
                },
                error:function(err){
                    if (err.responseJSON) {
                        vm.$message.error(err.responseJSON.msg)
                    } else {
                        vm.$message.error("修改密码出现异常")
                    }
                }
            })
        },
        checkPass: function () {
            if (!/^\w{6,15}$/g.test(this.pass.oldPass)) {
                this.$message.warning("请正确填写原始密码(6-15)位数字英文字符")
                return false;
            }
            if (!/^\w{6,15}$/g.test(this.pass.newPass1)) {
                this.$message.warning("请正确填新密码(6-15)位数字英文字符")
                return false;
            }
            if (this.pass.newPass1 != this.pass.newPass2) {
                this.$message.warning("两次密码输入不一致")
                return false;
            }
            return true;
        },
        /**
         * 发送修改密码验证码信息
         */
        sendEditPassValidCode: function () {
            if (!this.email.intervalId) {
                if (this.checkPass()) {
                    post({
                        url: "/user/changePassValid?to=" + this.form.email
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
        },
        cancelPassDialog: function () {
            this.$confirm("是否取消修改密码，取消之后不可更改", "警告", {
                type: "warning"
            }).then(function () {
                vm.isPasswordDialogVisible = false;
                if (vm.pass.intervalId) {
                    clearInterval(vm.pass.intervalId);
                    vm.pass.intervalId = "";
                }
                vm.validText = "获取验证码";
                vm.pass = {
                    oldPass: "",
                    newPass1: "",
                    newPass2: "",
                    valid: ""
                }
            }).catch(function () {

            })
        },
        /**
         * 更新邮箱
         */
        onSubmitEmail: function () {
            if (!this.email.valid && !this.checkEmail()) {
                this.$message.warning("请填写验证码");
                return;
            } else {
                $.ajax({
                    url: "/user/changeEmail",
                    type: "POST",
                    data: JSON.stringify({
                        validCode: vm.email.valid,
                        newEmail: vm.email.newEmail1
                    }),
                    dataType: "JSON",
                    contentType: "application/json;charset=utf-8",
                    success: function (res) {
                        if (res.code == 200) {
                            vm.$message.success("邮箱更新成功");
                            vm.form = res.data;
                            vm.isEmailDialogVisible = false;
                            if (vm.email.intervalId) {
                                clearInterval(vm.email.intervalId);
                                vm.email.intervalId = "";
                            }
                            vm.validText = "获取验证码";
                            vm.email = {
                                newEmail1: "",
                                newEmail2: "",
                                valid: ""
                            }
                        } else {
                            vm.$message.error(res.msg)
                        }
                    },
                    error: function (err) {
                        if (err.responseJSON) {
                            vm.$message.error(err.responseJSON.msg)
                        } else {
                            vm.$message.error("邮箱更新失败")
                        }

                    }
                })
            }
        },
        /**
         * 检查邮箱
         */
        checkEmail: function () {
            if (this.email.newEmail1 == this.form.email) {
                this.$message.warning("请填写新的邮箱");
                return false;
            }
            if (this.email.newEmail1.trim() == '') {
                this.$message.warning("请填写邮箱");
                return false;
            }
            var emailRegex = /^([0-9A-Za-z\-_.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g;
            if (!emailRegex.test(this.email.newEmail1)) {
                this.$message.warning("邮箱不合法");
                return false;
            }
            if (this.email.newEmail1 != this.email.newEmail2) {
                this.$message.warning("两次的邮箱不一致");
                return false;
            }
            return true;
        },
        changeValidText: function () {
            this.validText = 60;
            this.email.intervalId = setInterval(function () {
                if (vm.validText == 1) {
                    clearInterval(vm.email.intervalId);
                    vm.email.intervalId = "";
                    vm.validText = "获取验证码"
                } else {
                    vm.validText -= 1;
                }
            }, 1000);
        },
        /**
         * 发送验证码按钮
         */
        sendValidCode: function () {
            if (!this.email.intervalId) {
                if (this.checkEmail()) {
                    post({
                        url: "/user/changeSendValid?to=" + this.email.newEmail1
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
        },
        /**
         * 取消更新邮箱按钮事件
         */
        cancelEmailDialog: function () {
            this.$confirm("是否取消更新邮箱，取消之后不可更改", "警告", {
                type: "warning"
            }).then(function () {
                vm.isEmailDialogVisible = false;
                if (vm.email.intervalId) {
                    clearInterval(vm.email.intervalId);
                    vm.email.intervalId = "";
                }
                vm.validText = "获取验证码";
                vm.email = {
                    newEmail1: "",
                    newEmail2: "",
                    valid: ""
                }
            }).catch(function () {

            })
        },
        /**
         * 保存用户信息
         */
        onSubmit: function () {
            if(this.form.nickName && this.form.nickName.length > 1){
                this.$confirm("确认更新用户信息吗?","提示",{
                    type:"success"
                }).then(function(){
                    $.ajax({
                        url:"/user/update/info",
                        dataType:"json",
                        type:"PUT",
                        data:JSON.stringify({
                            nickName: vm.form.nickName,
                            icon: vm.form.icon
                        }),
                        contentType:"application/json;charset=utf-8",
                        success:function(res){
                            if(res.code == 200){
                                vm.$message.success("用户信息更新成功");
                                window.parent.$$vm.loadUserInfo();
                            }else{
                                vm.$message.error("用户信息更新失败");
                            }
                        },
                        error:function(err){
                            vm.$message.error("用户信息更新失败");
                        }
                    })
                })
            }else{
                this.$message.success("请正确填写昵称");
            }
        },
        /**
         * 加载用户信息
         */
        loadUserInfo: function () {
            get({
                url: "/user/info"
            }).then(function (res) {
                if (res.code == 200) {
                    vm.form = res.data
                } else {
                    vm.$message.error("加载用户信息失败");
                }
            }).catch(function (err) {
                vm.$message.error("加载用户信息失败");
            })
        },
        /**
         * 上传头像
         */
        upload: function () {
            this.$refs.upload.click();
            this.$refs.upload.onchange = function (item) {
                var file = item.target.files[0];
                if (vm.check(file)) {
                    var load = vm.$loading({target: "#icon"});
                    var formData = new FormData();
                    formData.append("file", file);
                    $.ajax({
                        url: "/file/upload",
                        data: formData,
                        processData: false, // 使数据不做处理
                        contentType: false,
                        type: "POST",
                        dataType: "json",
                        success: function (res) {
                            vm.form.icon = res.path;
                            $("#fileUpload").val("");
                            load.close();
                        },
                        error: function (err) {
                            vm.$message.error("上传头像失败");
                            $("#fileUpload").val("");
                            load.close();
                        }
                    })
                }
            }
        },
        //检查文件是否符合要求
        check: function (file) {
            var size = file.size / 1024;
            if (size > 5000) {
                this.$message({
                    message: '上传文件大小不能超过5MB',
                    type: 'warning'
                });
                return false;
            }
            var extName = file.name.substr(file.name.lastIndexOf('.') + 1);
            extName = extName.toLowerCase();
            switch (extName) {
                case "png":
                    return true;
                case "jpg":
                    return true;
                case "jpeg":
                    return true;
                case "gif":
                    return true;
                default: {
                    this.$message({
                        message: '上传文件不是图片格式',
                        type: 'warning'
                    });
                    return false;
                }
            }
        },
        sendMail: function () {
            post({
                url: "/mail/send/valid?to=" + this.form.email
            }).then(function (res) {
                console.log(res)
            }).catch(function (err) {
                console.log(err)
            })
        }
    },
    mounted: function () {
        this.loadUserInfo();
    }
});