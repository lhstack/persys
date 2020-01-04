package com.lhstack.common;

import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.Mongo;

import java.util.*;

public class MongoUtils {
    public static List<String> collectNames(Mongo mongo,String databaseNames){
        DB db = mongo.getDB(databaseNames);
        Set<String> collectionNames = db.getCollectionNames();
        return new ArrayList<>(collectionNames);
    }

    public static Map<String,Object> tableRowCounts(Mongo mongo,String databaseNames){
        DB db = mongo.getDB(databaseNames);
        Map<String,Object> map = new HashMap<>();

        Set<String> collectionNames = db.getCollectionNames();
        for (String collectionName : collectionNames) {
            map.put(collectionName,db.getCollection(collectionName).count());
        }
        return map;
    }

    public static Map<String,Object> commandResultToMap(CommandResult commandResult){
        Set<Map.Entry<String, Object>> entries = commandResult.entrySet();
        Map<String,Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : entries) {
            map.put(entry.getKey(),entry.getValue());
        }
        return map;
    }

    public static Map<String,Object> stats(Mongo mongo,String databaseName){
        DB db = mongo.getDB(databaseName);
        CommandResult stats = db.getStats();
        return commandResultToMap(stats);
    }

    public static Map<String,Object> globalServerInfo(Mongo mongo,String databaseName) {
        DB db = mongo.getDB(databaseName);
        CommandResult command = db.command("serverStatus");
        return commandResultToMap(command);
    }

    public static Map<String,Object> serverInfo(Mongo mongo,String databaseName) {
        DB db = mongo.getDB(databaseName);
        CommandResult command = db.command("hostInfo");
        return commandResultToMap(command);
    }
}
