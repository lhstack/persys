package com.lhstack.config.proxy.observer;

import com.alibaba.fastjson.JSONObject;
import com.lhstack.config.proxy.encry.DataDecodePlatform;
import com.lhstack.config.proxy.entity.SqlMonitory;
import com.lhstack.config.proxy.properties.GlobalConfigProperties;
import com.mysql.cj.BindValue;
import com.mysql.cj.QueryBindings;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.net.URI;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 默认ObServer接口实现类
 */
public class DefaultObServerConsumer extends AbstractObServerConsumer implements ObServerConsumer {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * remote.sql.monitory.enable通过application.properties设置此属性可以关闭远程监控
     * @param obServer 实际执行sql的对象
     * @return
     * @throws Exception
     */
    @Override
    public Object apply(ObServer obServer) throws Exception {
        long createTime = System.currentTimeMillis();
        try{
            return obServer.apply();
        }finally {
            try{
                Boolean enable = GlobalConfigProperties.getENABLE();
                if(enable){
                    storeSqlMonitoring(createTime);
                }
            }catch (Exception e){

            }
        }
    }

    /**
     * 存储监控数据
     * @param createTime
     * @throws Exception
     */
    private void storeSqlMonitoring(long createTime) throws Exception { ;
        long endTime = System.currentTimeMillis();
        SqlMonitory sqlMonitoring = new SqlMonitory();
        sqlMonitoring.setAverage(endTime - createTime);
        ClientPreparedStatement clientPreparedStatement = getClientPareStatement();
        if(clientPreparedStatement != null){
            String applicationName = GlobalConfigProperties.getApplicationName();
            sqlMonitoring.setSql(getSql(clientPreparedStatement))
                    .setParameter(getParameter(clientPreparedStatement))
                    .setNamespace(applicationName);
            executorService.execute(() ->{
                try {
                    push(sqlMonitoring);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /** remote.sql.monitory.aes.singingKey 服务端提供的私钥，通过在application.properties中可以进行配置
     *  monitoring.sql.uri 服务端接收监控数据的url地址通过application.properties进行配置
     *  spring.application.name 监控数据的namespace，没有设置，默认default
     * 发送监控数据到服务端
     * @param sqlMonitoring
     * @throws Exception
     */
    private void push(SqlMonitory sqlMonitoring) throws Exception {
        DataDecodePlatform dataDecodePlatform = new DataDecodePlatform();
        /*
         * 获取配置文件里面的私钥进行加密，此私钥对应PerSys系统设置的私钥
         */
        String c = dataDecodePlatform.encode(JSONObject.toJSONString(sqlMonitoring), GlobalConfigProperties.getSingingKey());
        HttpPost httpPost = new HttpPost();
        /**
         * 获取远程监控数据提交的url
         */
        String uri = GlobalConfigProperties.getMonitorySqlUri();
        httpPost.setURI(URI.create(uri + "?c=" + c));
        httpPost.setConfig(RequestConfig.DEFAULT);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        closeableHttpClient.execute(httpPost).close();
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


}
