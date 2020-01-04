package com.lhstack.service.monitoring;

import com.lhstack.common.MysqlUtils;
import com.lhstack.config.proxy.ProxyDataSource;
import com.lhstack.entity.PropertiesResponse;
import com.lhstack.entity.monitoring.SqlMonitoring;
import com.lhstack.repository.monitoring.SqlMonitoringRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class SqlMonitoringServiceImpl implements ISqlMonitoringService {

    @Autowired
    private SqlMonitoringRepository sqlMonitoringRepository;

    @Autowired
    private DataSource dataSource;

    @Override
    public SqlMonitoring save(SqlMonitoring entity) throws Exception {
        SqlMonitoring sqlMonitoring = sqlMonitoringRepository.findBySqlAndParameter(entity.getSql(),entity.getParameter());
        if(sqlMonitoring != null){
            /**
             * average暂代执行时间
             */
            Long executeTime = entity.getAverage();
            sqlMonitoring.setMaxExecuteTime(executeTime > sqlMonitoring.getMaxExecuteTime() ? executeTime : sqlMonitoring.getMaxExecuteTime());
            sqlMonitoring.setMinExecuteTime(executeTime < sqlMonitoring.getMinExecuteTime() ? executeTime : sqlMonitoring.getMinExecuteTime());
            sqlMonitoring.setAverage((sqlMonitoring.getMinExecuteTime() + sqlMonitoring.getMaxExecuteTime()) / 2 );
            sqlMonitoring.setCount(sqlMonitoring.getCount() != null ? sqlMonitoring.getCount() + 1 : 1);
        }else{
            sqlMonitoring = entity;
            sqlMonitoring.setMaxExecuteTime(sqlMonitoring.getAverage())
                    .setMinExecuteTime(sqlMonitoring.getAverage())
                    .setCount(1L);
        }
        return sqlMonitoringRepository.save(sqlMonitoring);
    }

    @Override
    public SqlMonitoring update(String s, SqlMonitoring entity) throws Exception {
        return null;
    }

    @Override
    public void delete(SqlMonitoring entity) throws Exception {

    }

    @Override
    public void deleteById(String s) throws Exception {
        sqlMonitoringRepository.deleteById(s);
    }

    @Override
    public SqlMonitoring findById(String s) throws Exception {
        return sqlMonitoringRepository.findById(s).orElse(null);
    }

    @Override
    public List<SqlMonitoring> findAll() throws Exception {
        return sqlMonitoringRepository.findAll();
    }

    @Override
    public Page<SqlMonitoring> findAll(Integer page, Integer size) throws Exception {
        if(page < 1){
            page = 1;
        }
        return sqlMonitoringRepository.findAll(PageRequest.of(page - 1,size));
    }

    @Override
    public Map<String,Object> getMysqlState() throws Exception {
        return buildMap();
    }

    /**
     * 获取连接池信息
     * @return
     * @throws SQLException
     */
    private Map<String,Object> buildMap() throws SQLException {
        PropertiesResponse<String,Object> propertiesResponse = new PropertiesResponse<>();
        HikariDataSource hikariDataSource = dataSource instanceof ProxyDataSource ? (HikariDataSource) ((ProxyDataSource)dataSource).getOriginDataSource() : dataSource instanceof HikariDataSource ? (HikariDataSource) dataSource : null;
        if(hikariDataSource != null){
            propertiesResponse.put("loginTimeOut",hikariDataSource.getLoginTimeout() + "s")
                    .put("timeout",hikariDataSource.getConnectionTimeout() + "ms")
                    .put("initSql",hikariDataSource.getConnectionInitSql())
                    .put("testSql",hikariDataSource.getConnectionTestQuery())
                    .put("idleTimeout",hikariDataSource.getIdleTimeout() + "ms")
                    .put("initializationFailTimeout",hikariDataSource.getInitializationFailTimeout() + "ms")
                    .put("jdbcUrl",hikariDataSource.getJdbcUrl())
                    .put("maxPoolSize",hikariDataSource.getMaximumPoolSize())
                    .put("maxLifetime",hikariDataSource.getMaxLifetime() + "ms")
                    .put("minimumIdle",hikariDataSource.getMinimumIdle())
                    .put("poolName",hikariDataSource.getPoolName())
                    .put("validationTimeout",hikariDataSource.getValidationTimeout() + "ms");
            Connection connection = hikariDataSource.getConnection();
            propertiesResponse.put("autoCommit",connection.getAutoCommit())
                    .put("database",connection.getCatalog())
                    .put("networkTimeout",connection.getNetworkTimeout() + "s")
                    .put("schema",connection.getSchema());
            initMysqlServerInfo(propertiesResponse,connection);
            connection.close();
            return propertiesResponse.getMap();
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
        propertiesResponse.put("version",MysqlUtils.version(connection))
                .put("serverInfo",MysqlUtils.serverInfo(connection))
                .put("currentUser",MysqlUtils.currentUser(connection))
                .put("mysqlSystemInfo",MysqlUtils.mysqlSystemInfo(connection))
                .put("mysqlEngine",MysqlUtils.mysqlEngine(connection))
                .put("tableRowCounts",MysqlUtils.tableRowCounts(propertiesResponse.getMap().get("database").toString(),connection));
    }
}
