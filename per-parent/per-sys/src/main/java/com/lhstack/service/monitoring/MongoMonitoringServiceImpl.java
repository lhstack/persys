package com.lhstack.service.monitoring;

import com.lhstack.common.MongoUtils;
import com.lhstack.entity.PropertiesResponse;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class MongoMonitoringServiceImpl implements IMongoMonitoringService {

    @Autowired
    private Mongo mongo;

    @Autowired
    private MongoProperties mongoProperties;


    @Autowired
    private MongoClient mongoClient;

    @Override
    @PostConstruct
    public Map<String, Object> getState() {
        PropertiesResponse<String,Object> propertiesResponse = new PropertiesResponse<>();
        initMongoOperator(propertiesResponse);
        initMongoSystemInfo(propertiesResponse);
        return propertiesResponse.getMap();
    }

    private void initMongoSystemInfo(PropertiesResponse<String, Object> propertiesResponse) {
        propertiesResponse.put("tableRowCounts", MongoUtils.tableRowCounts(mongo,propertiesResponse.getMap().get("database").toString()));
        propertiesResponse.put("serverInfo",MongoUtils.serverInfo(mongo,propertiesResponse.getMap().get("database").toString()));
        propertiesResponse.put("stats",MongoUtils.stats(mongo,propertiesResponse.getMap().get("database").toString()));
        propertiesResponse.put("globalServerInfo",MongoUtils.globalServerInfo(mongo,propertiesResponse.getMap().get("database").toString()));
    }

    private void initMongoOperator(PropertiesResponse<String,Object> propertiesResponse) {
        MongoClientOptions mongoClientOptions = mongoClient.getMongoClientOptions();
        propertiesResponse.put("description",mongoClientOptions.getDescription())
                .put("applicationName",mongoClientOptions.getApplicationName())
                .put("maxConnection",mongoClientOptions.getConnectionsPerHost())
                .put("minConnection",mongoClientOptions.getMinConnectionsPerHost())
                .put("maxConnectionIdleTime",mongoClientOptions.getMaxConnectionIdleTime() + "ms")
                .put("minHeartbeatFrequency",mongoClientOptions.getMinHeartbeatFrequency())
                .put("maxConnectionLifeTime",mongoClientOptions.getMaxConnectionLifeTime() + "ms")
                .put("maxWaitTime",mongoClientOptions.getMaxWaitTime() + "ms")
                .put("connectTimeout",mongoClientOptions.getConnectTimeout() + "ms")
                .put("heartbeatConnectTimeout",mongoClientOptions.getHeartbeatConnectTimeout() + "ms")
                .put("heartbeatSocketTimeout",mongoClientOptions.getHeartbeatSocketTimeout() + "ms")
                .put("serverSelectionTimeout",mongoClientOptions.getServerSelectionTimeout() + "ms")
                .put("socketTimeout",mongoClientOptions.getSocketTimeout() + "ms")
                .put("database",mongoProperties.getMongoClientDatabase())
                .put("host",new MongoClientURI(mongoProperties.determineUri()).getHosts())
                .put("username",mongoProperties.getUsername());
    }
}
