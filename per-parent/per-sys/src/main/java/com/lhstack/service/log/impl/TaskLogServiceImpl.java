package com.lhstack.service.log.impl;

import cn.hutool.core.date.DateUtil;
import com.lhstack.entity.log.TaskLog;
import com.lhstack.repository.log.TaskLogRepository;
import com.lhstack.service.log.ITaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lhstack.service.log.impl.LogExampleConst.*;
@Service("taskLogService")
public class TaskLogServiceImpl implements ITaskLogService {

    @Autowired
    private TaskLogRepository taskLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 复杂查询
     * @param example 查询条件
     *                beanName bean名称
     *                method bean方法
     *                state 执行状态
     *                times[] type java.util.Date[] 执行时间 times[0] startTime  <--> times[1] endTime
     *                sortField 排序字段 default process_date_time
     *                sortBy 排序规则 asc desc default desc
     * @param page
     * @param size
     * @return
     */
    @Override
    @Cacheable(cacheNames = "taskLog",key = "@genertorKey.getKey('findByExample',#p0,#p1,#p2)")
    public Page<TaskLog> findByExample(Map<String, Object> example,Integer page,Integer size) throws ParseException {
        Query query = new Query();
        String beanName = (String) example.get("beanName");
        like(BEAN_NAME,beanName,query);
        String method = (String) example.get("method");
        like(METHOD,method,query);
        Boolean state = (Boolean) example.get(STATE);
        if(null != state){
            query.addCriteria(Criteria.where(STATE).is(state));
        }
        if(example.containsKey(TIMES)){
            List dates = (List) example.get(TIMES);
            List<Date> dateList = (List<Date>) dates.stream().filter(item -> item instanceof Date || item instanceof String).map(item -> {
                if (item instanceof Date)
                    return (Date) item;
                return DateUtil.parse((String) item).toJdkDate();
            }).collect(Collectors.toList());
            if(dateList.size() == 2){
                timeBetween("process_date_time",dateList.get(0),dateList.get(1),query);
            }
        }
        Pageable pageable = pageAndSort("process_date_time", (String) example.get(SORT_FIELD), (String) example.get(SORT_BY), page, size);
        query.with(pageable);
        long count = mongoTemplate.count(query, TaskLog.class);
        List<TaskLog> taskLogs = mongoTemplate.find(query, TaskLog.class);
        return new PageImpl<>(taskLogs,pageable,count);
    }

    @Override
    @CacheEvict(cacheNames = "taskLog",allEntries = true)
    public void deleteAllById(List<String> ids) {
        taskLogRepository.deleteAllByIdIn(ids);
    }

    @Override
    @CacheEvict(cacheNames = "taskLog",allEntries = true)
    public TaskLog save(TaskLog entity) throws Exception {
        return taskLogRepository.save(entity);
    }

    @Override
    @CacheEvict(cacheNames = "taskLog",allEntries = true)
    public TaskLog update(String s, TaskLog entity) throws Exception {
        entity.setId(s);
        return taskLogRepository.save(entity);
    }

    @Override
    @CacheEvict(cacheNames = "taskLog",allEntries = true)
    public void delete(TaskLog entity) throws Exception {
        taskLogRepository.delete(entity);
    }

    @Override
    @CacheEvict(cacheNames = "taskLog",allEntries = true)
    public void deleteById(String s) throws Exception {
        taskLogRepository.deleteById(s);
    }

    @Override
    @Cacheable(cacheNames = "taskLog",key = "@genertorKey.getKey('findById',#p0)")
    public TaskLog findById(String s) throws Exception {
        return taskLogRepository.findById(s).orElse(null);
    }

    @Override
    @Cacheable(cacheNames = "taskLog",key = "@genertorKey.getKey('findAll')")
    public List<TaskLog> findAll() throws Exception {
        return taskLogRepository.findAll();
    }

    @Override
    @Cacheable(cacheNames = "taskLog",key = "@genertorKey.getKey('findAll',#p0,#p1)")
    public Page<TaskLog> findAll(Integer page, Integer size) throws Exception {
        if(page < 1){
            page = 1;
        }
        return taskLogRepository.findAll(PageRequest.of(page - 1,size, Sort.by(Sort.Order.desc("process_date_time"))));
    }
}
