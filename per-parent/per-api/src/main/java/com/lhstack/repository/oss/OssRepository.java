package com.lhstack.repository.oss;

import com.lhstack.entity.oss.OssEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OssRepository extends MongoRepository<OssEntity,String> {
    OssEntity findByToken(String token);

}
