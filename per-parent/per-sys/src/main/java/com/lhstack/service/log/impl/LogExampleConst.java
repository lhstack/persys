package com.lhstack.service.log.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * 日志查询条件常量
 */
public interface LogExampleConst {

    static final String USERNAME = "username";
    static final String METHOD = "method";
    static final String IP = "ip";
    static final String SORT_FIELD = "sortField";
    static final String SORT_BY = "sortBy";
    static final String TIMES = "times";
    static final String STATE = "state";
    static final String BEAN_NAME = "beanName";

    static void like(String key, String value, Query query){
        if(StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)){
            Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
            Criteria criteria = Criteria.where(key).regex(pattern);
            query.addCriteria(criteria);
        }
    }

    static void timeBetween(String key, Date startTime,Date endTime,Query query){
        query.addCriteria(Criteria.where(key).gte(startTime).lte(endTime));
    }

    static Pageable pageAndSort(String defaultSortField,String sortField, String sortBy, Integer page, Integer size){
        Pageable pageable = null;
        if(page < 1){
            page = 1;
        }
        if(StringUtils.isNotEmpty(sortField) && StringUtils.isNotEmpty(sortBy)){
            Sort sort = Sort
                    .by(Sort.Order.by(sortField)
                            .with(Sort.Direction.fromString(sortBy)));
            pageable = PageRequest.of(page - 1,size,sort);
        }else if(StringUtils.isNotEmpty(sortField) && StringUtils.isEmpty(sortBy)){
            Sort sort = Sort
                    .by(Sort.Order.desc(sortField));
            pageable = PageRequest.of(page - 1,size,sort);
        }else if(StringUtils.isEmpty(sortField) && StringUtils.isNotEmpty(sortBy)){
            Sort sort = Sort
                    .by(Sort.Order.by(defaultSortField)
                            .with(Sort.Direction.fromString(sortBy)));
            pageable = PageRequest.of(page - 1,size,sort);
        }else{
            Sort sort = Sort
                    .by(Sort.Order.desc(defaultSortField));
            pageable = PageRequest.of(page - 1,size,sort);
        }
        return pageable;
    }

}
