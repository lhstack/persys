package com.lhstack.repository.task;

import com.lhstack.entity.task.TaskEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<TaskEntity,String> {
}
