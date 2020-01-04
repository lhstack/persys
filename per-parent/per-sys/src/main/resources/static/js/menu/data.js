var staticMenus = [{
    "id": 1,
    "menuName": "用户管理",
    "enable": true,
    "isParent": true,
    "icon": "layui-icon layui-icon-user i",
    "href": "javascript:;",
    "createTime": "2019-11-02 17:35:10",
    "parentId": 0,
    "sortBy": 0,
    "child": [{
        "id": 2,
        "menuName": "用户列表",
        "enable": true,
        "isParent": false,
        "href": "/page/user@list",
        "createTime": "2019-11-02 17:35:50",
        "parentId": 1,
        "sortBy": 0
    }, {
        "id": 3,
        "menuName": "角色列表",
        "enable": true,
        "isParent": false,
        "href": "/page/role@list",
        "createTime": "2019-11-02 17:36:13",
        "parentId": 1,
        "sortBy": 0
    }, {
        "id": 4,
        "menuName": "权限列表",
        "enable": true,
        "isParent": false,
        "href": "/page/permission@list",
        "createTime": "2019-11-02 17:36:39",
        "parentId": 1,
        "sortBy": 0
    }]
}, {
    "id": 20,
    "menuName": "菜单管理",
    "enable": true,
    "isParent": true,
    "icon": "layui-icon-list layui-icon i",
    "href": "javascript:;",
    "createTime": "2019-11-08 08:50:33",
    "parentId": 0,
    "sortBy": 1,
    "child": [{
        "id": 21,
        "menuName": "菜单列表",
        "enable": true,
        "isParent": false,
        "href": "/page/menu@list",
        "createTime": "2019-11-08 08:51:56",
        "parentId": 20,
        "sortBy": 1
    }]
}, {
    "id": 10,
    "menuName": "监控管理",
    "enable": true,
    "isParent": true,
    "icon": "layui-icon-loading layui-icon i",
    "href": "javascript:;",
    "createTime": "2019-11-02 17:38:50",
    "parentId": 0,
    "sortBy": 2,
    "child": [{
        "id": 11,
        "menuName": "应用监控",
        "enable": true,
        "isParent": false,
        "href": "http://localhost:9876",
        "createTime": "2019-11-02 17:39:07",
        "parentId": 10,
        "sortBy": 2
    }, {
        "id": 12,
        "menuName": "mysql",
        "enable": true,
        "isParent": false,
        "href": "/page/monitoring@mysql",
        "createTime": "2019-11-02 17:39:46",
        "parentId": 10,
        "sortBy": 2
    }, {
        "id": 13,
        "menuName": "redis",
        "enable": true,
        "isParent": false,
        "href": "/page/monitoring@redis",
        "createTime": "2019-11-02 17:39:49",
        "parentId": 10,
        "sortBy": 2
    }, {
        "id": 14,
        "menuName": "mongodb",
        "enable": true,
        "isParent": false,
        "href": "/page/monitoring@mongodb",
        "createTime": "2019-11-02 17:39:53",
        "parentId": 10,
        "sortBy": 2
    }]
}, {
    "id": 15,
    "menuName": "相关内容",
    "enable": true,
    "isParent": true,
    "icon": "layui-icon-list layui-icon i",
    "href": "javascript:;",
    "createTime": "2019-11-02 17:40:16",
    "parentId": 0,
    "sortBy": 3,
    "child": [{
        "id": 16,
        "menuName": "Layui",
        "enable": true,
        "isParent": false,
        "href": "https://www.layui.com/",
        "createTime": "2019-11-02 17:41:03",
        "parentId": 15,
        "sortBy": 3
    }, {
        "id": 17,
        "menuName": "Gitee",
        "enable": true,
        "isParent": false,
        "href": "https://gitee.com/myprofile",
        "createTime": "2019-11-02 17:41:05",
        "parentId": 15,
        "sortBy": 3
    }, {
        "id": 18,
        "menuName": "Spring",
        "enable": true,
        "isParent": false,
        "href": "https://spring.io",
        "createTime": "2019-11-02 17:41:07",
        "parentId": 15,
        "sortBy": 3
    }]
}, {
    "id": 5,
    "menuName": "日志管理",
    "enable": true,
    "isParent": true,
    "icon": "layui-icon-chart-screen layui-icon i",
    "href": "javascript:;",
    "createTime": "2019-11-02 17:36:54",
    "parentId": 0,
    "sortBy": 4,
    "child": [{
        "id": 6,
        "menuName": "debug日志",
        "enable": true,
        "isParent": false,
        "href": "/page/log@debug",
        "createTime": "2019-11-02 17:37:27",
        "parentId": 5,
        "sortBy": 4
    }, {
        "id": 7,
        "menuName": "info日志",
        "enable": true,
        "isParent": false,
        "href": "/page/log@info",
        "createTime": "2019-11-02 17:38:15",
        "parentId": 5,
        "sortBy": 4
    }, {
        "id": 8,
        "menuName": "warn日志",
        "enable": true,
        "isParent": false,
        "href": "/page/log@warn",
        "createTime": "2019-11-02 17:38:18",
        "parentId": 5,
        "sortBy": 4
    }, {
        "id": 9,
        "menuName": "error日志",
        "enable": true,
        "isParent": false,
        "href": "/page/log@error",
        "createTime": "2019-11-02 17:38:21",
        "parentId": 5,
        "sortBy": 4
    }]
}, {
    "id": 19,
    "menuName": "关于我",
    "enable": true,
    "isParent": false,
    "icon": "layui-icon-face-smile-fine layui-icon i",
    "href": "javascript:;",
    "createTime": "2019-11-02 17:41:38",
    "parentId": 0,
    "sortBy": 5
}];