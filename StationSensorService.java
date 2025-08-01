package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.request.StationRequest;
import com.tridel.tems_sensor_service.model.response.*;

import java.util.List;

public interface StationSensorService {
    List<StationResponse> getAllStations(Request request);

    List<StationResponse> getAllStationByUserAndType(Request request);

    List<StationResponse> getAllStationByUserAndParam(Request request);

    StationResponse getStationDetailsById(Request request);


    List<StationResponse>  listAllStationData(Integer loggedIn);

    HomeSensorDataResponse getSensorDetailsForHome(Request request);

    List<StationWeatherDataResponse> loadAllStationWeatherData(Request request);

    String getCommunicationDetails(Request request);

    String generateLog(Request req);

    String updateStation(StationRequest stationRequest);

    List<Integer> getAllUserStations(Request request);
}
