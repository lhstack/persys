package com.lhstack.repository.monitoring.remote;

import com.lhstack.entity.monitoring.MysqlConnectionInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MysqlConnectionInfoRepository extends MongoRepository<MysqlConnectionInfo,String> {
    List<MysqlConnectionInfo> findAllByUsername(String username);
}
