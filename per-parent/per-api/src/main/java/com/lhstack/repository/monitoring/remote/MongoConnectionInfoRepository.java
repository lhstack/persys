package com.lhstack.repository.monitoring.remote;

import com.lhstack.entity.monitoring.MongoConnectionInfo;
import com.lhstack.entity.monitoring.MysqlConnectionInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoConnectionInfoRepository extends MongoRepository<MongoConnectionInfo,String> {
    List<MongoConnectionInfo> findAllByUsername(String username);
}
