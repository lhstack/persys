package com.lhstack.repository.log;

import com.lhstack.entity.log.SysLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

public interface SysLogRepository extends MongoRepository<SysLog,String> {
    void deleteByOperationTimeLessThan(Date date);
    void deleteAllByIdIn(Iterable<String> ids);
}
