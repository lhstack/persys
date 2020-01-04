package com.lhstack.service.monitoring;

import java.util.Map;

public interface IMongoMonitoringService  {
    Map<String,Object> getState();
}
