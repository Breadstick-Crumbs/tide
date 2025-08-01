package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.response.SensorParamView;
import com.tridel.tems_sensor_service.model.response.UserSensorParamUnitsResponse;
import com.tridel.tems_sensor_service.model.response.usersettings.SensorParamResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ParameterService {
    List<SensorParamView> loadParametersByType(Request request);
    List<SensorParamView> getAllParamsBySensor(Request req);
    List<UserSensorParamUnitsResponse> getAllParamsBySensorAndUser(Request req);
    String getAllStatisticsParamsBySensor(Request req);

    String loadSelectedParam(Request request);

    String updateParamDetails(SensorParamView paramResp,HttpServletRequest httpRequest);

    List<SensorParamResponse> getAllParameterBySensorAndUser(Request request);
}
