package com.lhstack.config.monitoring;

import com.lhstack.common.ApplicationContextHolder;
import com.lhstack.config.proxy.observer.AbstractObServerConsumer;
import com.lhstack.config.proxy.observer.ObServer;
import com.lhstack.entity.monitoring.SqlMonitoring;
import com.lhstack.service.monitoring.ISqlMonitoringService;
import com.mysql.cj.BindValue;
import com.mysql.cj.QueryBindings;
import com.mysql.cj.jdbc.ClientPreparedStatement;

import java.sql.SQLException;
import java.sql.Statement;

public class SqlConsumerObServer extends AbstractObServerConsumer {

    private ISqlMonitoringService sqlMonitoringService;
    @Override
    public Object apply(ObServer obServer) throws Exception {
        long createTime = System.currentTimeMillis();
        try{
            return obServer.apply();
        }
        finally {
            try{
                storeSqlMonitoring(createTime);
            }catch (Exception e){

            }
        }
    }

    private void storeSqlMonitoring(long createTime) throws Exception {
        sqlMonitoringService = getSqlMonitoringService();
        long endTime = System.currentTimeMillis();
        SqlMonitoring sqlMonitoring = new SqlMonitoring();
        sqlMonitoring.setAverage(endTime - createTime);
        ClientPreparedStatement clientPreparedStatement = getClientPareStatement();
        if(clientPreparedStatement != null){
            sqlMonitoring.setSql(getSql(clientPreparedStatement))
            .setParameter(getParameter(clientPreparedStatement));
            sqlMonitoringService.save(sqlMonitoring);
        }
    }

    private String getParameter(ClientPreparedStatement clientPreparedStatement) {
        QueryBindings<?> queryBindings = clientPreparedStatement.getQueryBindings();
        BindValue[] bindValues = queryBindings.getBindValues();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bindValues.length; i++) {
            stringBuilder.append(new String(bindValues[i].getByteValue()) + "--->");
        }
        return stringBuilder.length() > 4 ? stringBuilder.substring(0,stringBuilder.length() - 4) : stringBuilder.toString();
    }

    private String getSql(ClientPreparedStatement clientPreparedStatement) {
        return clientPreparedStatement.getPreparedSql().replaceAll("(\r|\n)","");
    }

    private ClientPreparedStatement getClientPareStatement() throws SQLException {

        Statement statement = (Statement) metaData.get("statement");
        if(statement instanceof ClientPreparedStatement){
            return (ClientPreparedStatement) statement;
        }else if(statement.isWrapperFor(ClientPreparedStatement.class)){
            return statement.unwrap(ClientPreparedStatement.class);
        }
        return null;
    }

    private ISqlMonitoringService getSqlMonitoringService() {
        return ApplicationContextHolder.getApplicationContext().getBean(ISqlMonitoringService.class);
    }
}
