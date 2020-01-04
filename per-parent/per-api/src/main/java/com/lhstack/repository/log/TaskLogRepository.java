package com.lhstack.repository.log;

import com.lhstack.entity.log.TaskLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskLogRepository extends MongoRepository<TaskLog,String> {
    void deleteAllByIdIn(Iterable<String> ids);
}
