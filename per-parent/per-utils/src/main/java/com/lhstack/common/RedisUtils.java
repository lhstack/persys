package com.lhstack.common;

import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.data.redis.connection.ReactiveServerCommands;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class RedisUtils {

    public static Map<String, String> getInfo(ReactiveServerCommands reactiveServerCommands, String command) {
        Properties stats = reactiveServerCommands.info(command).block();
        return propertiesToMap(stats);
    }

    public static Map<String, String> getInfo(RedisCommands<String, String> redisCommands, String command) throws IOException {
        String info = redisCommands.info(command);
        Properties properties = new Properties();
        properties.load(new StringReader(info));
        return propertiesToMap(properties);
    }

    private static Map<String, String> propertiesToMap(Properties properties) {
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<Object, Object> entry : entries) {
            map.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return map;
    }
}
