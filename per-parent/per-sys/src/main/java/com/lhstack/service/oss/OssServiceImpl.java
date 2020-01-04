package com.lhstack.service.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.lhstack.config.security.holder.SecurityContextHolder;
import com.lhstack.entity.fastdfs.FastResult;
import com.lhstack.entity.oss.OssEntity;
import com.lhstack.repository.oss.OssRepository;
import com.lhstack.utils.FileUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OssServiceImpl implements IOssService {

    @Autowired
    private OssRepository repository;
    @Override
    public OssEntity save(OssEntity entity) throws Exception {
        entity.setToken(UUID.randomUUID().toString().replace("-",""));
        entity.setId(null);
        return repository.save(entity);
    }

    /**
     * 删除多个文件
     * @param token
     * @param keys
     * @return
     * @throws Exception
     */
    @Override
    public Boolean deleteObjects(String token, List<String> keys) throws Exception{
        List<String> listKeys = keys.stream().map(item -> {
            if(item.startsWith("http://") || item.startsWith("https://")){
                item = item.replaceAll("(http://|https://)?", "");
                return item.substring(item.indexOf("/") + 1);
            }
            return item.charAt(0) == '/' || item.charAt(0) == '\\' ? item.substring(1) : item;
        }).collect(Collectors.toList());
        OssEntity ossEntity = repository.findByToken(token);
        if(ObjectUtils.isEmpty(ossEntity)){
            throw new RuntimeException("token不存在");
        }
        Set<String> ipWhiteList = ossEntity.getIpWhiteList();
        if(!ipWhiteList.contains("0.0.0.0") && !ipWhiteList.contains(SecurityContextHolder.getIp())){
            throw new RuntimeException("请确认ip白名单存在你的ip");
        }
        String type = ossEntity.getType();
        if(type.toLowerCase().equals("ali_yun")){
            OSS oss = new OSSClientBuilder().build(ossEntity.getEndPoint(),ossEntity.getAccessKeyId(),ossEntity.getAccessKeySecret());
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(ossEntity.getBucketName());
            deleteObjectsRequest.setKeys(listKeys);
            oss.deleteObjects(deleteObjectsRequest);
            return true;
        }else if(type.toLowerCase().equals("qiniu_yun")){
            Auth auth = Auth.create(ossEntity.getAccessKeyId(),ossEntity.getAccessKeySecret());
            BucketManager bucketManager = new BucketManager(auth,new Configuration(Region.autoRegion()));
            listKeys.forEach(item ->{
                try {
                    bucketManager.delete(ossEntity.getBucketName(),item);
                } catch (QiniuException e) {

                }
            });
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteObject(String token, String key) throws Exception{
        if(key.startsWith("http://") || key.startsWith("https://")){
            key = key.replaceAll("(http://|https://)?", "");
            key =  key.substring(key.indexOf("/") + 1);
        }else{
            key = key.charAt(0) == '/' || key.charAt(0) == '\\' ? key.substring(1) : key;
        }
        OssEntity ossEntity = repository.findByToken(token);
        if(ObjectUtils.isEmpty(ossEntity)){
            throw new RuntimeException("token不存在");
        }
        Set<String> ipWhiteList = ossEntity.getIpWhiteList();
        if(!ipWhiteList.contains("0.0.0.0") && !ipWhiteList.contains(SecurityContextHolder.getIp())){
            throw new RuntimeException("请确认ip白名单存在你的ip");
        }
        String type = ossEntity.getType();
        if(type.toLowerCase().equals("ali_yun")){
            OSS oss = new OSSClientBuilder().build(ossEntity.getEndPoint(),ossEntity.getAccessKeyId(),ossEntity.getAccessKeySecret());
            oss.deleteObject(ossEntity.getBucketName(),key);
            return true;
        }else if(type.toLowerCase().equals("qiniu_yun")){
            Auth auth = Auth.create(ossEntity.getAccessKeyId(),ossEntity.getAccessKeySecret());
            BucketManager bucketManager = new BucketManager(auth,new Configuration(Region.autoRegion()));
            bucketManager.delete(ossEntity.getBucketName(),key);
            return true;
        }
        return false;
    }

    @Override
    public OssEntity update(String s, OssEntity entity) throws Exception {
        OssEntity ossEntity = repository.findById(s).orElseThrow(RuntimeException::new);
        entity.setId(s)
                .setToken(ossEntity.getToken());
        return repository.save(entity);
    }

    @Override
    public void delete(OssEntity entity) throws Exception {
        repository.delete(entity);
    }

    @Override
    public void deleteById(String s) throws Exception {
        repository.deleteById(s);
    }

    @Override
    public OssEntity findById(String s) throws Exception {
        return repository.findById(s).orElseThrow(NullPointerException::new);
    }

    @Override
    public List<OssEntity> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Page<OssEntity> findAll(Integer page, Integer size) throws Exception {
        if(page < 1){
            page = 1;
        }
        return repository.findAll(PageRequest.of(page - 1,size));
    }


    /**
     * 上传多文件
     * @param generatorType
     * @param mappingContents
     * @param token
     * @return
     */
    @Override
    public List<FastResult> uploadMultiFile(String generatorType, Map<String, byte[]> mappingContents, String token, String directory) {
        OssEntity ossEntity = repository.findByToken(token);
        if(ObjectUtils.isEmpty(ossEntity)){
            throw new RuntimeException("token不存在");
        }
        Set<String> ipWhiteList = ossEntity.getIpWhiteList();
        if(!ipWhiteList.contains("0.0.0.0") && !ipWhiteList.contains(SecurityContextHolder.getIp())){
            throw new RuntimeException("请确认ip白名单存在你的ip");
        }
        String type = ossEntity.getType();
        switch (type.toLowerCase()){
            case "ali_yun":{
                return aliYunUpload(ossEntity,mappingContents,generatorType, directory);
            }
            case "qiniu_yun":{
                return qiniuYunUpload(ossEntity,mappingContents,generatorType,directory);
            }
        }
        return null;
    }

    @Override
    public FastResult upload(String generatorType, byte[] filePartToBytes, String filename, String token, String directory) throws Exception {
        OssEntity ossEntity = repository.findByToken(token);
        if(ObjectUtils.isEmpty(ossEntity)){
            throw new RuntimeException("token不存在");
        }
        Set<String> ipWhiteList = ossEntity.getIpWhiteList();
        if(!ipWhiteList.contains("0.0.0.0") && !ipWhiteList.contains(SecurityContextHolder.getIp())){
            throw new RuntimeException("请确认ip白名单存在你的ip");
        }
        String type = ossEntity.getType();
        switch (type.toLowerCase()){
            case "ali_yun":{
                return aliYunUpload(ossEntity,filePartToBytes, generatorName(directory,FileUtils.generatorFileName(generatorType,filename)));
            }
            case "qiniu_yun":{
                return qiniuYunUpload(ossEntity,filePartToBytes,generatorName(directory,FileUtils.generatorFileName(generatorType,filename)));
            }
        }
        return null;
    }

    private String generatorName(String directory, String generatorFileName) {
        if(StringUtils.isBlank(directory)){
            return generatorFileName;
        }
        directory = directory.charAt(0) == '/' || directory.charAt(0) == '\\' ? directory.substring(1) : directory;
        if(directory.charAt(directory.length() - 1) == '/' || directory.charAt(directory.length() - 1) == '\\'){
            return directory + generatorFileName;
        }

        return directory + "/" + generatorFileName;
    }


    /**
     * 上传多文件
     * @param ossEntity
     * @param mappingContents
     * @return
     */
    private List<FastResult> qiniuYunUpload(OssEntity ossEntity, Map<String, byte[]> mappingContents,String generatorType, String directory) {
        List<FastResult> resultList = mappingContents.entrySet().stream().map(item -> {
            try {
                Auth auth = Auth.create(ossEntity.getAccessKeyId(), ossEntity.getAccessKeySecret());
                String generatorFileName = FileUtils.generatorFileName(generatorType, item.getKey());
                String token = auth.uploadToken(ossEntity.getBucketName(), generatorName(directory,generatorFileName));
                Configuration configuration = new Configuration(Region.autoRegion());
                UploadManager uploadManager = new UploadManager(configuration);
                uploadManager.put(item.getValue(), generatorName(directory,generatorFileName), token);
                return buildQiNiuYunResult(ossEntity, generatorName(directory,generatorFileName), item.getValue().length);
            } catch (QiniuException e) {
                return null;
            }
        }).filter(Objects::nonNull)
                .collect(Collectors.toList());
        return resultList;
    }

    /**
     * 上传多文件
     * @param ossEntity
     * @param mappingContents
     * @return
     */
    private List<FastResult> aliYunUpload(OssEntity ossEntity, Map<String, byte[]> mappingContents,String generatorType, String directory) {
        List<FastResult> resultList = mappingContents.entrySet().stream().map(item -> {
            OSS oss = new OSSClientBuilder().build(ossEntity.getEndPoint(), ossEntity.getAccessKeyId(), ossEntity.getAccessKeySecret());
            String generatorFileName = FileUtils.generatorFileName(generatorType, item.getKey());
            oss.putObject(ossEntity.getBucketName(), generatorName(directory,generatorFileName), new ByteArrayInputStream(item.getValue()));
            return buildAliYunResult(generatorName(directory,generatorFileName), ossEntity, item.getValue().length);
        }).collect(Collectors.toList());
        return resultList;
    }

    private FastResult qiniuYunUpload(OssEntity ossEntity, byte[] content, String generatorFileName) throws QiniuException {
        Auth auth = Auth.create(ossEntity.getAccessKeyId(),ossEntity.getAccessKeySecret());
        String token = auth.uploadToken(ossEntity.getBucketName(), generatorFileName);
        Configuration configuration = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(configuration);
        uploadManager.put(content,generatorFileName,token);
        return buildQiNiuYunResult(ossEntity,generatorFileName,content.length);
    }

    private FastResult buildQiNiuYunResult(OssEntity ossEntity, String generatorFileName,long size) {
        FastResult fastResult = new FastResult();
        fastResult.setFileName(generatorFileName.substring(generatorFileName.lastIndexOf("/") + 1))
                .setPath(ossEntity.getSchema() + "://" + ossEntity.getEndPoint() + "/" + generatorFileName)
                .setSize(size);
        return fastResult;
    }


    private FastResult aliYunUpload(OssEntity ossEntity, byte[] content, String generatorFileName) {
        OSS oss = new OSSClientBuilder().build(ossEntity.getEndPoint(),ossEntity.getAccessKeyId(),ossEntity.getAccessKeySecret());
        oss.putObject(ossEntity.getBucketName(), generatorFileName, new ByteArrayInputStream(content));
        return buildAliYunResult(generatorFileName,ossEntity,content.length);
    }

    private FastResult buildAliYunResult(String fileName,OssEntity ossEntity,long size){
        FastResult fastResult = new FastResult();
        fastResult.setFileName(fileName.substring(fileName.lastIndexOf("/") + 1))
                .setPath(ossEntity.getSchema() + "://" + ossEntity.getBucketName() + "." + ossEntity.getEndPoint() + "/" + fileName)
                .setSize(size);
        return fastResult;
    }
}
