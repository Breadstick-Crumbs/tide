package com.tridel.tems_sensor_service.dao;

import com.tridel.tems_sensor_service.model.request.ReportRequest;

import java.util.List;

public interface ReportDao {

    List<String> getIntervalClimateReportsHeader(ReportRequest request);
}
