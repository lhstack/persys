package com.lhstack.service.monitoring;

import com.lhstack.common.SystemUtils;
import com.lhstack.entity.PropertiesResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SystemMonitoringServiceImpl implements ISystemMonitoringService {
    @Override
    public Map<String, Object> getState() {
        PropertiesResponse<String,Object> propertiesResponse = new PropertiesResponse<>();
        propertiesResponse.put("systemInfo", SystemUtils.getSystemInfo())
        .put("classLoaderInfo",SystemUtils.getClassLoaderInfo())
        .put("deskInfo",SystemUtils.getDeskInfo())
        .put("memoryInfo",SystemUtils.getMemoryInfo())
        .put("runTimeInfo",SystemUtils.getRunTimeInfo())
        .put("threadInfo",SystemUtils.getThreadInfo());
        return propertiesResponse.getMap();
    }
}
