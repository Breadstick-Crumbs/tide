package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.response.SensorResponse;

import java.util.List;

public interface SensorService {

    List<SensorResponse> getAllSensorsByUser(Request req);

    List<SensorResponse> getAllSensorsByStations(Request req);
}
