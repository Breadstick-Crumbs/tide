package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.MobileRequest;
import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.response.MobileReport;

import java.util.List;

public interface MobileService {
    String getStationStatus(Request request);

    List<MobileReport> loadReports(MobileRequest request);
}
