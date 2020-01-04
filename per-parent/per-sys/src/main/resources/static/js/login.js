!(function(){
    $(".bg-img").animate({"translate":'scale(1.2)'},{
        speed:40000
    })
   animation();
})();
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
function toReset() {
    localStorage.setItem("loginPage",window.location.href);
    localStorage.setItem("registryPage","/sso/registry?token=admin");
    window.location.href = "/page/reset@index";
}
function keyDownLogin(){
    $(".keyDownLogin").keydown(function(e) {
        if(e.keyCode == 13){
            loginBtn()
        }
    })
}

function loginBtn(){
    var username = $("#username").val();
    var password = $("#password").val();
    var code = $("#code").val();
    if(checkInfo(username,password)){
      if("" != code.trim()){
          post({
              url: "/login.html",
              data:{
                  username: username,
                  password: password,
                  code: code
              }
          }).then(function(res){
              if(res.status == 200 && res.state){
                  window.location.href="/";
              }else{
                  $("#info").text("用户名密码输入有误");
                  $("#codeImg")[0].src = "/code?random=" + Math.random() * 99999;
                  $("#info").fadeIn(100);
              }
          }).catch(function (err) {
              if(err.status == 400){
                  $("#codeImg")[0].src = "/code?random=" + Math.random() * 99999;
                  $("#info").text(err.responseJSON.msg);
                  $("#info").fadeIn(100);
              }else{
                  $("#info").text("用户名密码输入有误");
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

function checkInfo(username,password) {
    if(/^[\w]{5,18}$/.test(username) || /^([0-9A-Za-z\-_\.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g.test(username)){
        if(/^[\w]{5,18}$/.test(password)){
            return true;
        }else{
            $("#info").text("密码必须为5-18之间的字母和数字");
            $("#info").fadeIn(100);
            return false;
        }
    }
    $("#info").text("用户名必须为5-18之间的字母和数字");
    $("#info").fadeIn(100);
    return false;
}

function registry(){

}