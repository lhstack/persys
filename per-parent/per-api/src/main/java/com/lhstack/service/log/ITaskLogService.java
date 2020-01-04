package com.lhstack.service.log;

import com.lhstack.entity.log.TaskLog;
import com.lhstack.service.IBaseService;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ITaskLogService extends IBaseService<String, TaskLog> {

    Page<TaskLog> findByExample(Map<String,Object> example,Integer page,Integer size) throws ParseException;

    void deleteAllById(List<String> ids);
}
