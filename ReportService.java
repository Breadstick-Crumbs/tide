package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.ReportRequest;

public interface ReportService {
    String loadReportsDataOneMinute(ReportRequest request);

    String loadReportsDataByInterval(ReportRequest request);

    String loadReportsDataHourly(ReportRequest request);

    String loadClimateReportsData(ReportRequest request);

    String loadClimateIntervalFilterReportsData(ReportRequest request);
}
