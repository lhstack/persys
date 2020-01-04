<!DOCTYPE html>
<html lang="en">
<head>
    <script type="text/javascript" src="/js/FileSaver.min.js"></script>
    <script type="text/javascript" src="/js/xlsx.full.min.js"></script>
    <#include "/common/header.ftl"/>
    <#include "/common/avue-common.ftl"/>
    <link href="/css/permission/index.css" type="text/css" rel="stylesheet"/>
    <title>Sql List</title>
</head>
<body>
    <div id="app">
        <avue-crud
                :data="data"
                :option="option"
                :page="pager"
                @current-change="currentChange"
                @refresh-change="refreshChange"
                @size-change="sizeChange"
        >
            <template slot="id" slot-scope="scope">
                <span :title="scope.row.id">{{scope.row.id}}</span>
            </template>
            <template slot="sql" slot-scope="scope">
                <div v-text="scope.row.sql ? scope.row.sql.length > 35 ? (scope.row.sql.substr(0,35) + '...') : scope.row.sql : ''" :title="scope.row.sql"></div>
            </template>
            <template slot="count" slot-scope="scope">
                <span :title="scope.row.count">{{scope.row.count}}</span>
            </template>
            <template slot="parameter" slot-scope="scope">
                <div v-text="scope.row.parameter ? (scope.row.parameter.length > 35 ? (scope.row.parameter.substr(0,35) + '...') : scope.row.parameter) : ''" :title="scope.row.parameter"></div>
            </template>
            <template slot="maxExecuteTime" slot-scope="scope">
                <span :title="scope.row.maxExecuteTime">{{scope.row.maxExecuteTime + 'ms'}}</span>
            </template>
            <template slot="minExecuteTime" slot-scope="scope">
                <span :title="scope.row.minExecuteTime">{{scope.row.minExecuteTime + 'ms'}}</span>
            </template>
            <template slot="average" slot-scope="scope">
                <span :title="scope.row.average">{{scope.row.average + 'ms'}}</span>
            </template>
        </avue-crud>
    </div>
<script type="application/javascript" src="/js/monitoring/sql.js"></script>
</body>
</html>