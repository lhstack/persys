package com.lhstack.service.monitoring;

import java.util.Map;

public interface IRedisMonitoringService {
    Map<String,Object> getState();
}
