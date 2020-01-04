package com.lhstack.service.mail;

public interface MailConst {
    String VALID_CODE_HTML_TEMPLATE = " <style type='text/css'>\n" +
            "            #title{\n" +
            "                width: 100%;\n" +
            "                text-align: center;\n" +
            "                color:#83dcd8;\n" +
            "                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
            "            }\n" +
            "            #content{\n" +
            "                width: 600px;\n" +
            "                margin: auto;\n" +
            "    \n" +
            "            }\n" +
            "            #message{\n" +
            "                width: 100%;\n" +
            "                text-align: center;\n" +
            "                color:#83dcd8;\n" +
            "                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
            "            }\n" +
            "            #message > span{\n" +
            "                color: #4d7eb1;\n" +
            "            }\n" +
            "            #message > span:hover{\n" +
            "                color: black;\n" +
            "                cursor: pointer;\n" +
            "            }\n" +
            "        </style>\n" +
            "    <h1 id='title'>\n" +
            "        欢迎使用PerSys系统\n" +
            "    </h1>\n" +
            "    <div id='content'>\n" +
            "        <h3 id='message'>\n" +
            "            你的邮箱验证码是:<span>@</span>\n" +
            "        </h3>\n" +
            "    </div>";


    String MAIL_PREFIX = "email:valid:code:";

    String EDIT_MAIL_PREFIX = "email:edit:valid:code:";

    String MAIL_RESET = "email:reset:valid:code:";
}
