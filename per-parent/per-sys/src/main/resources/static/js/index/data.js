const staticMenu = [
    {
        id:0,
        href:"",
        isParent: true,
        menuName:"用户管理",
        icon:'layui-icon layui-icon-user i',
        child:[
            {
                id:1,
                href:"/page/user@list",
                menuName: "用户列表",
                isParent: false
            },
            {
                id:2,
                href:"/page/role@list",
                menuName: "角色列表",
                isParent: false
            },{
                id:3,
                href:"/page/permission@list",
                menuName: "权限列表",
                isParent: false
            }
        ]
    },
    {
        id:4,
        href:"",
        isParent: true,
        menuName:"日志管理",
        icon:'layui-icon-chart-screen layui-icon i',
        child:[
            {
                id:5,
                href:"/page/log@debug",
                menuName: "debug日志",
                isParent: false
            },
            {
                id:6,
                href:"/page/log@info",
                menuName: "info日志",
                isParent: false
            },{
                id:7,
                href:"/page/log@warn",
                menuName: "warn日志",
                isParent: false
            },{
                id:8,
                href:"/page/log@error",
                menuName: "error日志",
                isParent: false
            }
        ]
    },
    {
        id:9,
        href:"",
        isParent: true,
        menuName:"监控管理",
        icon:'layui-icon-loading layui-icon i',
        child:[
            {
                id:10,
                href:"http://localhost:9876",
                menuName: "应用监控",
                isParent: false
            },
            {
                id:11,
                href:"/page/monitoring@mysql",
                menuName: "mysql",
                isParent: false
            },{
                id:12,
                href:"/page/monitoring@redis",
                menuName: "redis",
                isParent: false
            },{
                id:13,
                href:"/page/monitoring@mongodb",
                menuName: "mongodb",
                isParent: false
            }
        ]
    },
    {
        id:14,
        href:"",
        isParent: true,
        menuName:"相关内容",
        icon:'layui-icon-list layui-icon i',
        child:[
            {
                id:15,
                href:"https://www.layui.com/",
                menuName: "Layui",
                isParent: false
            },
            {
                id:16,
                href:"https://gitee.com/myprofile",
                menuName: "Gitee",
                isParent: false
            },{
                id:17,
                href:"https://spring.io",
                menuName: "Spring",
                isParent: false
            }
        ]
    },
    {
        id:18,
        href:"javascript:;",
        isParent: false,
        menuName:"关于我",
        icon:'layui-icon-face-smile-fine layui-icon i',
        child:[]
    }
];