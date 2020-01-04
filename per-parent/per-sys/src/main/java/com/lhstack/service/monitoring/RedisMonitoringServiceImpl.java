package com.lhstack.service.monitoring;

import com.lhstack.common.RedisUtils;
import com.lhstack.entity.PropertiesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveServerCommands;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class RedisMonitoringServiceImpl implements IRedisMonitoringService {

    @Autowired
    private ReactiveRedisConnectionFactory redisProperties;

    @Override
    @PostConstruct
    public Map<String, Object> getState() {
        ReactiveServerCommands reactiveServerCommands = redisProperties.getReactiveConnection().serverCommands();
        PropertiesResponse<String,Object> propertiesResponse = new PropertiesResponse<>();
        propertiesResponse.put("stats", RedisUtils.getInfo(reactiveServerCommands,"stats"))
                .put("cpu",RedisUtils.getInfo(reactiveServerCommands,"cpu"))
                .put("persistence",RedisUtils.getInfo(reactiveServerCommands,"Persistence"))
                .put("memory",RedisUtils.getInfo(reactiveServerCommands,"memory"))
                .put("clients",RedisUtils.getInfo(reactiveServerCommands,"clients"))
                .put("server",RedisUtils.getInfo(reactiveServerCommands,"server"))
                .put("keyspace",RedisUtils.getInfo(reactiveServerCommands,"Keyspace"));
        return propertiesResponse.getMap();
    }
}
