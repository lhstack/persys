package com.lhstack.repository.monitoring.remote;

import com.lhstack.entity.monitoring.RedisConnectionInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RedisConnectionInfoRepository extends MongoRepository<RedisConnectionInfo,String> {
    List<RedisConnectionInfo> findAllByUsername(String username);

}
