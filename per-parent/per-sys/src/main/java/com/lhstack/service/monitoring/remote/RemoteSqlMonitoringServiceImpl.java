package com.lhstack.service.monitoring.remote;

import com.alibaba.fastjson.JSONObject;
import com.lhstack.entity.monitoring.remote.RemoteSqlMonitoring;
import com.lhstack.repository.monitoring.remote.RemoteSqlMonitoringRepository;
import org.apache.commons.codec.binary.Base64;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RemoteSqlMonitoringServiceImpl implements IRemoteSqlMonitoringService {

    @Autowired
    private RemoteSqlMonitoringRepository remoteSqlMonitoringRepository;


    @Value("${remote.sql.monitory.aes.singingKey}")
    private String remoteSqlAesSinginKey;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public RemoteSqlMonitoring save(RemoteSqlMonitoring entity) throws Exception {
        return remoteSqlMonitoringRepository.save(entity);
    }

    @Override
    public RemoteSqlMonitoring update(String s, RemoteSqlMonitoring entity) throws Exception {
        entity.setId(s);
        return remoteSqlMonitoringRepository.save(entity);
    }

    @Override
    public void delete(RemoteSqlMonitoring entity) throws Exception {
        remoteSqlMonitoringRepository.delete(entity);
    }

    @Override
    public void deleteById(String s) throws Exception {
        remoteSqlMonitoringRepository.deleteById(s);
    }

    @Override
    public RemoteSqlMonitoring findById(String s) throws Exception {
        return remoteSqlMonitoringRepository.findById(s).orElseThrow(NullPointerException::new);
    }

    @Override
    public List<RemoteSqlMonitoring> findAll() throws Exception {
        return remoteSqlMonitoringRepository.findAll();
    }

    @Override
    public List<String> findAllApplicationName(){
        Document document = new Document();
        Document fields = new Document();
        fields.append("namespace",1);
        Query query = new BasicQuery(document,fields);
        return new ArrayList<>(mongoTemplate.find(query, String.class, "remote_sql").stream().map(item ->{
            JSONObject jsonObject = (JSONObject) JSONObject.parse(item);
            return jsonObject.getString("namespace");
        }).collect(Collectors.toSet()));
    }

    @Override
    public Page<RemoteSqlMonitoring> findAllByApplicationName(String applicationName, Integer page, Integer size) throws Exception {
        if(page < 1){
            page = 1;
        }
        return remoteSqlMonitoringRepository.findAllByNamespace(applicationName,PageRequest.of(page - 1,size));
    }

    /**
     * 保存远程提交的监控数据
     * @param content
     * @return
     * @throws Exception
     */
    @Override
    public Boolean saveSqlMonitoryData(String content) throws Exception {
        try{
            String json = decodeContent(content);
            RemoteSqlMonitoring remoteSqlMonitoring = JSONObject.parseObject(json, RemoteSqlMonitoring.class);
            RemoteSqlMonitoring dbRemoteSqlMonitoring = remoteSqlMonitoringRepository.findByNamespaceAndSqlAndParameter(remoteSqlMonitoring.getNamespace(),remoteSqlMonitoring.getSql(),remoteSqlMonitoring.getParameter());
            if(dbRemoteSqlMonitoring == null){
                remoteSqlMonitoring.setCount(1L)
                        .setMaxExecuteTime(remoteSqlMonitoring.getAverage())
                        .setMinExecuteTime(remoteSqlMonitoring.getAverage());
                remoteSqlMonitoringRepository.save(remoteSqlMonitoring);
            }else{
                dbRemoteSqlMonitoring.setCount(dbRemoteSqlMonitoring.getCount() + 1)
                        .setMinExecuteTime(remoteSqlMonitoring.getAverage() < dbRemoteSqlMonitoring.getMinExecuteTime() ? remoteSqlMonitoring.getAverage() : dbRemoteSqlMonitoring.getMinExecuteTime())
                        .setMaxExecuteTime(remoteSqlMonitoring.getAverage() > dbRemoteSqlMonitoring.getMaxExecuteTime() ? remoteSqlMonitoring.getAverage() : dbRemoteSqlMonitoring.getMaxExecuteTime())
                        .setAverage((dbRemoteSqlMonitoring.getMaxExecuteTime() + dbRemoteSqlMonitoring.getMinExecuteTime()) / 2);
                remoteSqlMonitoringRepository.save(dbRemoteSqlMonitoring);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void deleteByNamespace(String group) {
        remoteSqlMonitoringRepository.deleteByNamespace(group);
    }

    /**
     * 解密字符串，返回原始json数据
     * @param content
     * @return
     * @throws Exception
     */
    private String decodeContent(String content) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(remoteSqlAesSinginKey.getBytes(),"AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
        byte[] bytes = Base64.decodeBase64(content);
        return new String(cipher.doFinal(bytes));
    }

    @Override
    public Page<RemoteSqlMonitoring> findAll(Integer page, Integer size) throws Exception {
        if(page < 1){
            page = 1;
        }
        return remoteSqlMonitoringRepository.findAll(PageRequest.of(page - 1,size));
    }
}
