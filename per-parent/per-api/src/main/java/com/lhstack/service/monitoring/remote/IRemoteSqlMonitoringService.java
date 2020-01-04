package com.lhstack.service.monitoring.remote;

import com.lhstack.entity.monitoring.remote.RemoteSqlMonitoring;
import com.lhstack.service.IBaseService;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IRemoteSqlMonitoringService extends IBaseService<String, RemoteSqlMonitoring> {

    List<String> findAllApplicationName();

    Page<RemoteSqlMonitoring> findAllByApplicationName(String applicationName, Integer page, Integer size) throws Exception;

    Boolean saveSqlMonitoryData(String content) throws Exception;

    void deleteByNamespace(String group);
}
