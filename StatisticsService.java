package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.request.StatisticsRequest;

public interface StatisticsService {
    String loadParameterDataGraph(Request request);
    String loadStatisticsData(StatisticsRequest request);

    String loadAdvStatisticsData(StatisticsRequest request);
}
