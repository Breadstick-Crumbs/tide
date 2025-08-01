package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.SummaryRequest;

public interface SummaryService {

    String loadSummaryData(SummaryRequest request);
}
