package com.lhstack.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlUtils {

    public static String version(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT VERSION()");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString(1);
    }

    public static Map<String,Object> serverInfo(Connection connection) throws SQLException {
        Map<String,Object> map = new HashMap<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SHOW VARIABLES LIKE '%max_connections%'");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            map.put(resultSet.getString(1),resultSet.getString(2));
        }
        preparedStatement = connection.prepareStatement("SHOW STATUS LIKE '%Connection%'");
        resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            String string = resultSet.getString(1);
            if("Max_used_connections_time".equals(string) || "Connection_errors_peer_address".equals(string))
                continue;
            map.put(resultSet.getString(1),resultSet.getString(2));
        }
        preparedStatement = connection.prepareStatement("SHOW STATUS like 'Threads%'");
        resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            map.put(resultSet.getString(1),resultSet.getString(2));
        }
        return map;
    }

    public static String currentUser(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT USER()");

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString(1);
    }

    public static Map<String,Object> mysqlSystemInfo(Connection connection) throws SQLException {
        Map<String,Object> map = new HashMap<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SHOW GLOBAL VARIABLES LIKE '%compile%'");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            map.put(resultSet.getString(1),resultSet.getString(2));
        }
        return map;
    }

    public static Map<String,Object> mysqlEngine(Connection connection) throws SQLException {
        Map<String,Object> map = new HashMap<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SHOW VARIABLES LIKE '%storage_engine%'");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            map.put(resultSet.getString(1),resultSet.getString(2));
        }
        return map;
    }

    public static Map<String,Long> tableRowCounts(String databaseName,Connection connection) throws SQLException {
        Map<String,Long> map = new HashMap<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT TABLE_NAME,TABLE_ROWS FROM information_schema.tables WHERE table_schema=?");
        preparedStatement.setObject(1,databaseName);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            map.put(resultSet.getString(1),resultSet.getLong(2));
        }
        return map;
    }

    public static List<String> tableNames(Connection connection) throws SQLException {
        List<String> list = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SHOW TABLES");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            list.add(resultSet.getString(1));
        }
        return list;
    }
}
