package com.lhstack.service.monitoring.remote;

import com.lhstack.entity.monitoring.MongoConnectionInfo;
import com.lhstack.service.IBaseService;

import java.util.List;
import java.util.Map;

public interface IMongoConnectionInfoService extends IBaseService<String, MongoConnectionInfo> {
    List<MongoConnectionInfo> findAll(String username);

    Map<String,Object> getState(String id) throws Exception;
}
