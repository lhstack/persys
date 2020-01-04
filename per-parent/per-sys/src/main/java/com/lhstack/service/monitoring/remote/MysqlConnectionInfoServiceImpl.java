package com.lhstack.service.monitoring.remote;

import com.lhstack.common.MysqlUtils;
import com.lhstack.entity.PropertiesResponse;
import com.lhstack.entity.monitoring.MysqlConnectionInfo;
import com.lhstack.repository.monitoring.remote.MysqlConnectionInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class MysqlConnectionInfoServiceImpl implements IMysqlConnectionInfoService {

    @Autowired
    private MysqlConnectionInfoRepository mysqlConnectionInfoRepository;

    @Override
    public MysqlConnectionInfo save(MysqlConnectionInfo entity) throws Exception {
        return mysqlConnectionInfoRepository.save(entity);
    }

    @Override
    public MysqlConnectionInfo update(String s, MysqlConnectionInfo entity) throws Exception {
        entity.setId(s);
        return mysqlConnectionInfoRepository.save(entity);
    }

    @Override
    public void delete(MysqlConnectionInfo entity) throws Exception {
        mysqlConnectionInfoRepository.delete(entity);
    }

    @Override
    public void deleteById(String s) throws Exception {
        mysqlConnectionInfoRepository.deleteById(s);
    }

    @Override
    public MysqlConnectionInfo findById(String s) throws Exception {
        return mysqlConnectionInfoRepository.findById(s).orElse(null);
    }

    @Override
    public List<MysqlConnectionInfo> findAll() throws Exception {
        return mysqlConnectionInfoRepository.findAll();
    }

    @Override
    public List<MysqlConnectionInfo> findAll(String username){
        return mysqlConnectionInfoRepository.findAllByUsername(username);
    }

    @Override
    public Map<String,Object> getState(String id) throws Exception {
        try{
            MysqlConnectionInfo mysqlConnectionInfo = mysqlConnectionInfoRepository.findById(id).orElseThrow(RuntimeException::new);
            Connection connection = DriverManager.getConnection(mysqlConnectionInfo.getDbUrl(), mysqlConnectionInfo.getDbUsername(), mysqlConnectionInfo.getDbPassword());
            PropertiesResponse<String,Object> propertiesResponse = new PropertiesResponse<>();
            initMysqlServerInfo(propertiesResponse,connection);
            connection.close();
            return propertiesResponse.getMap();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取数据库信息
     * @param propertiesResponse
     * @param connection
     * @throws SQLException
     */
    private void initMysqlServerInfo(PropertiesResponse<String, Object> propertiesResponse, Connection connection) throws SQLException {
        propertiesResponse.put("version", MysqlUtils.version(connection))
                .put("serverInfo",MysqlUtils.serverInfo(connection))
                .put("currentUser",MysqlUtils.currentUser(connection))
                .put("mysqlSystemInfo",MysqlUtils.mysqlSystemInfo(connection))
                .put("mysqlEngine",MysqlUtils.mysqlEngine(connection))
                .put("tableRowCounts",MysqlUtils.tableRowCounts(connection.getCatalog(),connection));
    }

    @Override
    public Page<MysqlConnectionInfo> findAll(Integer page, Integer size) throws Exception {
        if(page < 1){
            page = 1;
        }
        return mysqlConnectionInfoRepository.findAll(PageRequest.of(page - 1,size));
    }
}
