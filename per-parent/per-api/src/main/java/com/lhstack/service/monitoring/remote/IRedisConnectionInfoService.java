package com.lhstack.service.monitoring.remote;

import com.lhstack.entity.monitoring.RedisConnectionInfo;
import com.lhstack.service.IBaseService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IRedisConnectionInfoService extends IBaseService<String, RedisConnectionInfo> {
    List<RedisConnectionInfo> findAll(String username);

    Map<String,Object> getState(String id) throws IOException;
}
