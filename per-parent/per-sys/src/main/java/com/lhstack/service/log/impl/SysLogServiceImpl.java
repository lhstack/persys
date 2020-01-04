package com.lhstack.service.log.impl;

import cn.hutool.core.date.DateUtil;
import com.lhstack.entity.log.SysLog;
import com.lhstack.repository.log.SysLogRepository;
import com.lhstack.service.log.ISysLogService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lhstack.service.log.impl.LogExampleConst.*;

@Service("sysLogService")
public class SysLogServiceImpl implements ISysLogService {

    @Autowired
    private SysLogRepository sysLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 复杂查询
     * @param example
     *          username 操作用户
     *          ip       操作ip
     *          method   操作方法
     *          times[] type java.util.Date[] 执行时间 times[0] startTime  <--> times[1] endTime
     *          sortField 排序字段 default operation_time
     *          sortBy 排序规则 asc desc default desc
     * @param page
     * @param size
     * @return
     * @throws ParseException
     */
    @Override
    @Cacheable(cacheNames = "sysLog",key = "@genertorKey.getKey('findAllByExample',#p0,#p1,#p2)")
    public Page<SysLog> findAllByExample(Map<String, Object> example, Integer page, Integer size) throws ParseException {
        Query query = new Query();
        String username = (String) example.get(USERNAME);
        like(USERNAME,username,query);
        String ip = (String) example.get(IP);
        like(IP,ip,query);
        String method = (String) example.get(METHOD);
        like(METHOD,method,query);
        if(example.containsKey(TIMES)){
            List dates = (List) example.get(TIMES);
            List<Date> dateList = (List<Date>) dates.stream().filter(item -> item instanceof Date || item instanceof String).map(item -> {
                if (item instanceof Date)
                    return (Date) item;
                return DateUtil.parse((String) item).toJdkDate();
            }).collect(Collectors.toList());
            if(dateList.size() == 2){
                timeBetween("operation_time",dateList.get(0),dateList.get(1),query);
            }
        }

        if(page < 1){
            page = 1;
        }
        Pageable pageable = pageAndSort("operation_time", (String) example.get(SORT_FIELD), (String) example.get(SORT_BY), page, size);
        query.with(pageable);
        long count = mongoTemplate.count(query, SysLog.class);
        List<SysLog> list = mongoTemplate.find(query, SysLog.class);
        return new PageImpl<>(list,pageable,count);
    }

    @Override
    @CacheEvict(cacheNames = "sysLog",allEntries = true)
    public SysLog save(SysLog entity) {
        return sysLogRepository.save(entity);
    }

    @Override
    @CacheEvict(cacheNames = "sysLog",allEntries = true)
    public SysLog update(String id, SysLog entity) {
        entity.setId(id);
        return sysLogRepository.save(entity);
    }

    @Override
    @CacheEvict(cacheNames = "sysLog",allEntries = true)
    public void delete(SysLog entity) {
        sysLogRepository.delete(entity);
    }

    @Override
    @CacheEvict(cacheNames = "sysLog",allEntries = true)
    public void deleteById(String id) {
        sysLogRepository.deleteById(id);
    }

    @Override
    @Cacheable(cacheNames = "sysLog",key = "@genertorKey.getKey('findById',#p0)")
    public SysLog findById(String id) {
        return sysLogRepository.findById(id).get();
    }

    @Override
    @Cacheable(cacheNames = "sysLog",key = "@genertorKey.getKey('findAll')")
    public List<SysLog> findAll() {
        return sysLogRepository.findAll();
    }

    @Override
    @Cacheable(cacheNames = "sysLog",key = "@genertorKey.getKey('findAll',#p0,#p1)")
    public Page<SysLog> findAll(Integer page, Integer size) {
        if(page < 1){
            page = 1;
        }
        return sysLogRepository.findAll(PageRequest.of(page - 1,size));
    }

    /**
     * 删除指定时间之前的日志
     * @param day
     */
    @Override
    @CacheEvict(cacheNames = "sysLog",allEntries = true)
    public void deleteByDateForDayBefore(Integer day){
        sysLogRepository.deleteByOperationTimeLessThan(DateUtils.addDays(new Date(),-day));
    }

    @Override
    @CacheEvict(cacheNames = "sysLog",allEntries = true)
    public void deleteAllById(List<String> ids) {
        sysLogRepository.deleteAllByIdIn(ids);
    }
}
