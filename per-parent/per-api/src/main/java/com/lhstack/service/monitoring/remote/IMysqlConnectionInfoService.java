package com.lhstack.service.monitoring.remote;

import com.lhstack.entity.monitoring.MysqlConnectionInfo;
import com.lhstack.service.IBaseService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IMysqlConnectionInfoService extends IBaseService<String,MysqlConnectionInfo> {
    List<MysqlConnectionInfo> findAll(String username);

    Map<String,Object> getState(String id) throws SQLException, Exception;
}
