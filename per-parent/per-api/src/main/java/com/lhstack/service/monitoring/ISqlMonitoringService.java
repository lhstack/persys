package com.lhstack.service.monitoring;

import com.lhstack.entity.monitoring.SqlMonitoring;
import com.lhstack.service.IBaseService;

import java.sql.SQLException;
import java.util.Map;

public interface ISqlMonitoringService extends IBaseService<String, SqlMonitoring> {
    Map<String,Object> getMysqlState() throws SQLException, Exception;
}
