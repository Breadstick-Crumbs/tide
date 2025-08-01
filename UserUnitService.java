package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.request.UserUnitRequest;
import com.tridel.tems_sensor_service.model.response.UnitResponse;

import java.util.List;

public interface UserUnitService {


    String updateUserUnitSettings(UserUnitRequest request);

    List<UnitResponse> getUnitByUserParam(UserUnitRequest request);

    String getParamUnitConversion(Request request);
}
