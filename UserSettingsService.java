package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.usersettings.SequenceSettingRequest;
import com.tridel.tems_sensor_service.model.request.usersettings.UserStationSensorRequest;
import com.tridel.tems_sensor_service.model.response.usersettings.StationParamResponse;
import com.tridel.tems_sensor_service.model.response.usersettings.UserSettingsResponse;

import java.util.List;

public interface UserSettingsService {
    List<StationParamResponse> getAllParameters(UserStationSensorRequest req);

    String addUserStationSensor(List<UserStationSensorRequest> req);

    String getUserStationSensorDetail(UserStationSensorRequest req);

    List<UserSettingsResponse> getCustomizedDashboardDetails(SequenceSettingRequest req);

    String addCustomizedDashboard(SequenceSettingRequest req);

    String updateCustomizedDashboard(SequenceSettingRequest req);

    String deleteCustomizedDashboard(SequenceSettingRequest req);

    List<UserSettingsResponse> getCustomizedHomeDetails(SequenceSettingRequest req);

    String addCustomizedHome(SequenceSettingRequest req);

    String updateCustomizedHome(SequenceSettingRequest req);

    String deleteCustomizedHome(SequenceSettingRequest req);
}
