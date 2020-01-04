package com.lhstack.repository.monitoring.remote;

import com.lhstack.entity.monitoring.remote.RemoteSqlMonitoring;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RemoteSqlMonitoringRepository extends MongoRepository<RemoteSqlMonitoring,String> {
    Page<RemoteSqlMonitoring> findAllByNamespace(String nameSpace, Pageable pageable);

    RemoteSqlMonitoring findByNamespaceAndSqlAndParameter(String namespace, String sql, String parameter);

    void deleteByNamespace(String group);
}
