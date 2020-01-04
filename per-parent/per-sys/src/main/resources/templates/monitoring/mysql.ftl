<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/common/header.ftl"/>
    <script type="application/javascript" src="/js/echarts.min.js"></script>
    <#include "/common/avue-common.ftl"/>
    <link href="/css/permission/index.css" type="text/css" rel="stylesheet"/>
    <title>Mysql List</title>
    <style>
        body,html{
            padding: 0;
            margin:0;
            overflow-x: hidden;
        }
        .box{
            display: flex;
            flex-wrap: wrap;
        }
        .item{
            width: 20%;
            margin: 2%;
            min-height: 100px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            box-sizing: border-box;
        }
        .item:hover{
            cursor: pointer;
            transition: 0.5s all;
            box-shadow: 0 0 3px 3px #00FFFF;
            background: gray;
            color: #fff;
        }
        .item > span:nth-child(1){
            font-size: 14px;
            font-family: "微软雅黑";
            font-weight: bold;
            display: block;
            text-align: center;
            margin-bottom: 10px;
            margin-top: 10px;
        }
        .item > div:nth-child(2){
            overflow-x: auto;
            min-height: 60px;
            text-align: center;
        }
    </style>
</head>
<body>
    <div id="app">
       <div style="width:100%;display: flex;overflow-x: hidden;overflow-y: hidden;margin-bottom: 50px">
           <div style="width: 60%">
               <div id="line" style="width: 100%;height:400px;"></div>
           </div>
           <div style="width: 40%">
               <div id="pie" style="width: 100%;height:400px;"></div>
           </div>
       </div>
        <div class="box">
            <div v-for="(item,index) in list" :key="index" class="item">
                <span v-text="item.key">

                </span>
                <div v-text="item.value">

                </div>
            </div>
        </div>
    </div>
<script type="application/javascript" src="/js/monitoring/mysql.js"></script>
</body>
</html>