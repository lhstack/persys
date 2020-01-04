package com.lhstack.service.monitoring.remote;

import com.lhstack.common.RedisUtils;
import com.lhstack.entity.PropertiesResponse;
import com.lhstack.entity.monitoring.RedisConnectionInfo;
import com.lhstack.repository.monitoring.remote.RedisConnectionInfoRepository;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class RedisConnectionInfoServiceImpl implements IRedisConnectionInfoService {

    @Autowired
    private RedisConnectionInfoRepository redisConnectionInfoRepository;


    @Override
    public RedisConnectionInfo save(RedisConnectionInfo entity) throws Exception {
        return redisConnectionInfoRepository.save(entity);
    }

    @Override
    public RedisConnectionInfo update(String s, RedisConnectionInfo entity) throws Exception {
        entity.setId(s);
        return redisConnectionInfoRepository.save(entity);
    }

    @Override
    public void delete(RedisConnectionInfo entity) throws Exception {
        redisConnectionInfoRepository.delete(entity);
    }

    @Override
    public void deleteById(String s) throws Exception {
        redisConnectionInfoRepository.deleteById(s);
    }

    @Override
    public RedisConnectionInfo findById(String s) throws Exception {
        return redisConnectionInfoRepository.findById(s).orElse(null);
    }

    @Override
    public List<RedisConnectionInfo> findAll() throws Exception {
        return redisConnectionInfoRepository.findAll();
    }

    @Override
    public List<RedisConnectionInfo> findAll(String username){
        return redisConnectionInfoRepository.findAllByUsername(username);
    }


    @Override
    public Map<String,Object> getState(String id) throws IOException {
        RedisConnectionInfo redisConnectionInfo = redisConnectionInfoRepository.findById(id).orElseThrow(RuntimeException::new);
        RedisURI.Builder builder = RedisURI.builder().withHost(StringUtils.isNotBlank(redisConnectionInfo.getDbHost()) ? redisConnectionInfo.getDbHost() : "localhost")
                .withPort(redisConnectionInfo.getDbPort() != null ? redisConnectionInfo.getDbPort() : 6379)
                .withDatabase(redisConnectionInfo.getDatabase());
        if(StringUtils.isNotBlank(redisConnectionInfo.getDbPassword())){
            builder.withPassword(redisConnectionInfo.getDbPassword());
        }
        RedisClient redisClient = RedisClient.create(builder.build());
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> sync = connect.sync();
        PropertiesResponse<String,Object> propertiesResponse = new PropertiesResponse<>();
        propertiesResponse.put("stats", RedisUtils.getInfo(sync,"stats"))
                .put("cpu",RedisUtils.getInfo(sync,"cpu"))
                .put("persistence",RedisUtils.getInfo(sync,"Persistence"))
                .put("memory",RedisUtils.getInfo(sync,"memory"))
                .put("clients",RedisUtils.getInfo(sync,"clients"))
                .put("server",RedisUtils.getInfo(sync,"server"))
                .put("keyspace",RedisUtils.getInfo(sync,"Keyspace"))
                .put("dbsize", sync.dbsize());
        connect.close();
        return propertiesResponse.getMap();
    }

    @Override
    public Page<RedisConnectionInfo> findAll(Integer page, Integer size) throws Exception {
        if(page < 1){
            page = 1;
        }
        return redisConnectionInfoRepository.findAll(PageRequest.of(page - 1,size));
    }
}
