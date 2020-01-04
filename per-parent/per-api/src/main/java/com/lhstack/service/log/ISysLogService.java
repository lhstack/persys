package com.lhstack.service.log;

import com.lhstack.entity.log.SysLog;
import com.lhstack.service.IBaseService;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ISysLogService extends IBaseService<String, SysLog> {
    Page<SysLog> findAllByExample(Map<String, Object> map, Integer page, Integer size) throws ParseException;

    void deleteByDateForDayBefore(Integer day);

    void deleteAllById(List<String> ids);
}
