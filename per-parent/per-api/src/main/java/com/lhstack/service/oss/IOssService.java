package com.lhstack.service.oss;

import com.lhstack.entity.fastdfs.FastResult;
import com.lhstack.entity.oss.OssEntity;
import com.lhstack.service.IBaseService;

import java.util.List;
import java.util.Map;

public interface IOssService extends IBaseService<String, OssEntity> {

    Boolean deleteObjects(String token, List<String> keys) throws Exception;

    Boolean deleteObject(String token, String key) throws Exception;

    List<FastResult> uploadMultiFile(String generatorType, Map<String, byte[]> mappingContents, String token, String directory);

    FastResult upload(String generatorType, byte[] filePartToBytes, String filename, String token, String directory) throws Exception;
}
