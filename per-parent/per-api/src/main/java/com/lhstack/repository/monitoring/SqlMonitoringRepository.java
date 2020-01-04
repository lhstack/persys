package com.lhstack.repository.monitoring;

import com.lhstack.entity.monitoring.SqlMonitoring;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SqlMonitoringRepository extends MongoRepository<SqlMonitoring,String> {
    SqlMonitoring findBySqlAndParameter(String sql, String parameter);
}
