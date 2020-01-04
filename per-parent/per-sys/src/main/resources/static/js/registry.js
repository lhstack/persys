!(function(){
    $(".bg-img").animate({"translate":'scale(1.2)'},{
        speed:40000
    });
   animation();
})();
layui.use("layer",function(){
    var layer = layui.layer;
});
function animation(){
    var count = 1;
    setInterval(function(){
        var bg = $(".bg-img");
        switch (count) {
            case 0:{
                bg.animate({"translate":'scale(1.2)'},{
                    speed:40000
                })
                count = 1;
            }break;
            case 1:{
                bg.animate({"translate":'scale(1)'},{
                    speed:40000
                })
                count = 0;
            }break;
        }
    },40000)
}
$(function(){
    keyDownLogin()
});

function keyDownLogin(){
    $(".keyDownLogin").keydown(function(e) {
        if(e.keyCode == 13){
            registryBtn()
        }
    })
}

function getUser(){
    var user = {};
    user.username = $("#username").val();
    user.password = $("#password").val();
    user.nickName = $("#nickname").val();
    user.email = $("#email").val();
    user.code = $("#code").val();
    console.log(user);
    return user;
}

function registryBtn(){
    var user = getUser();
    if(checkInfo(user)){
      if("" != user.code.trim()){
          post({
              url: "/user/registry",
              data:user
          }).then(function(res){
              if(res.code == 200){
                  layer.msg("用户注成功，3秒后跳转登录界面",{icon:6});
                  setTimeout(function(){
                      window.location.href="/login.html";
                  },3000)
              }else{
                  $("#info").text(res.msg);
                  $("#info").fadeIn(100);
                  $("#codeImg")[0].src = "/code?random=" + Math.random() * 99999;
              }
          }).catch(function (err) {
              if(err.status == 400){
                  $("#codeImg")[0].src = "/code?random=" + Math.random() * 99999;
                  $("#info").text(err.responseJSON.msg);
                  $("#info").fadeIn(100);
              }else{
                  $("#info").text("注册用户失败");
                  $("#info").fadeIn(100);
              }
          })
      }else{
          $("#info").text("请填写验证码");
          $("#info").fadeIn(100);
          return ;
      }
    }
}

function checkInfo(user) {
    var strRegex = /^[\w]{5,18}$/;
    var emailRegex = /^([0-9A-Za-z\-_.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g;
    if(user.nickName.length < 5 || user.nickName.length > 18){
        $("#info").text("昵称长度必须为5-18");
        $("#info").fadeIn(100);
        return false;
    }
    if(!strRegex.test(user.username)){
        $("#info").text("用户名必须为5-18之间的字母和数字");
        $("#info").fadeIn(100);
        return false;
    }
    if(!strRegex.test(user.password)){
        $("#info").text("密码必须为5-18之间的字母和数字");
        $("#info").fadeIn(100);
        return false;
    }
    if(!emailRegex.test(user.email)){
        $("#info").text("邮箱格式不合法");
        $("#info").fadeIn(100);
        return false;
    }
    return true;
}
