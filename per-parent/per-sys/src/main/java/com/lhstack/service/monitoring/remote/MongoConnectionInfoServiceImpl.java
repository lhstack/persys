package com.lhstack.service.monitoring.remote;

import com.lhstack.common.MongoUtils;
import com.lhstack.entity.PropertiesResponse;
import com.lhstack.entity.monitoring.MongoConnectionInfo;
import com.lhstack.repository.monitoring.remote.MongoConnectionInfoRepository;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.MongoURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MongoConnectionInfoServiceImpl implements IMongoConnectionInfoService {

    @Autowired
    private MongoConnectionInfoRepository mongoConnectionInfoRepository;

    @Override
    public MongoConnectionInfo save(MongoConnectionInfo entity) throws Exception {
        return mongoConnectionInfoRepository.save(entity);
    }

    @Override
    public MongoConnectionInfo update(String s, MongoConnectionInfo entity) throws Exception {
        entity.setId(s);
        return mongoConnectionInfoRepository.save(entity);
    }

    @Override
    public void delete(MongoConnectionInfo entity) throws Exception {
        mongoConnectionInfoRepository.delete(entity);
    }

    @Override
    public void deleteById(String s) throws Exception {
        mongoConnectionInfoRepository.deleteById(s);
    }

    @Override
    public MongoConnectionInfo findById(String s) throws Exception {
        return mongoConnectionInfoRepository.findById(s).orElse(null);
    }

    @Override
    public List<MongoConnectionInfo> findAll() throws Exception {
        return mongoConnectionInfoRepository.findAll();
    }

    @Override
    public List<MongoConnectionInfo> findAll(String username){
        return mongoConnectionInfoRepository.findAllByUsername(username);
    }


    @Override
    public Map<String,Object> getState(String id) throws Exception {
        MongoConnectionInfo mongoConnectionInfo = mongoConnectionInfoRepository.findById(id).orElseThrow(RuntimeException::new);
        MongoURI mongoURI = new MongoURI(mongoConnectionInfo.getMongoUrl());
        Mongo mongo = new Mongo(mongoURI);
        PropertiesResponse<String,Object> propertiesResponse = new PropertiesResponse<>();
        initMongoOperator(propertiesResponse,mongoURI);
        initMongoSystemInfo(propertiesResponse,mongo,mongoURI.getDatabase());
        mongo.close();
        return propertiesResponse.getMap();
    }


    private void initMongoSystemInfo(PropertiesResponse<String, Object> propertiesResponse,Mongo mongo,String databaseName) {
        propertiesResponse.put("tableRowCounts", MongoUtils.tableRowCounts(mongo,databaseName));
        propertiesResponse.put("serverInfo",MongoUtils.serverInfo(mongo,databaseName));
        propertiesResponse.put("stats",MongoUtils.stats(mongo,databaseName));
        propertiesResponse.put("globalServerInfo",MongoUtils.globalServerInfo(mongo,databaseName));
    }

    private void initMongoOperator(PropertiesResponse<String,Object> propertiesResponse,MongoURI mongo) {
        MongoOptions options = mongo.getOptions();
        propertiesResponse.put("description",options.getDescription())
                .put("maxConnection",options.getConnectionsPerHost())
                .put("maxWaitTime",options.getMaxWaitTime() + "ms")
                .put("connectTimeout",options.getConnectTimeout() + "ms")
                .put("socketTimeout",options.getSocketTimeout() + "ms")
                .put("host",mongo.getHosts());
    }


    @Override
    public Page<MongoConnectionInfo> findAll(Integer page, Integer size) throws Exception {
        if(page < 1){
            page = 1;
        }
        return mongoConnectionInfoRepository.findAll(PageRequest.of(page - 1,size));
    }
}
