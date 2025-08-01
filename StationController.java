package com.tridel.tems_sensor_service.controller;

import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.request.StationRequest;
import com.tridel.tems_sensor_service.model.response.HomeSensorDataResponse;
import com.tridel.tems_sensor_service.model.response.StationResponse;
import com.tridel.tems_sensor_service.model.response.StationWeatherDataResponse;
import com.tridel.tems_sensor_service.service.StationSensorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/station")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Station data Controller", description = "Comprise of all the Station data services")
public class StationController {
    StationSensorService stationSensorService;

    StationController(StationSensorService stationSensorService){
        this.stationSensorService=stationSensorService;
    }
    @PostMapping("/getAllStation")
    public ResponseEntity<List<StationResponse>> getAllStationDetails(@RequestBody Request request) {
        return ResponseEntity
                .ok()
                .body(stationSensorService.getAllStations(request));

    }
    @PostMapping("/getAllStationByUserAndType")
    public ResponseEntity<List<StationResponse>> getAllStationByUserAndType(@RequestBody Request request) {
        return ResponseEntity
                .ok()
                .body(stationSensorService.getAllStationByUserAndType(request));
    }
    @PostMapping("/getAllStationByUserAndParam")
    public ResponseEntity<List<StationResponse>> getAllStationByUserAndParam(@RequestBody Request request) {
        return ResponseEntity
                .ok()
                .body(stationSensorService.getAllStationByUserAndParam(request));
    }

    @PostMapping("/getStationDetailsById")
    public ResponseEntity<StationResponse> getStationDetailsById(@RequestBody Request request) {
            return ResponseEntity
                    .ok()
                    .body(stationSensorService.getStationDetailsById(request));

    }

    @PostMapping("/loadAllStations")
    public ResponseEntity<List<StationResponse>> loadAllStations(@RequestBody Request request) {
        return ResponseEntity
                .ok()
                .body(stationSensorService.listAllStationData(request.getLoggedIn()));

    }
    @PostMapping("/getSensorDataDetailsForHome")
    public ResponseEntity<HomeSensorDataResponse> getSensorDataDetailsForHome(@RequestBody Request request) {
        return ResponseEntity
                .ok()
                .body(stationSensorService.getSensorDetailsForHome(request));

    }
    @PostMapping("/loadAllStationWeatherData")
    public ResponseEntity<List<StationWeatherDataResponse>> loadAllStationWeatherData(@RequestBody Request request) {
            return ResponseEntity
                    .ok()
                    .body(stationSensorService.loadAllStationWeatherData(request));
    }
    @PostMapping("/getStationCommunicationDetails")
    public ResponseEntity<String> getStationCommunicationDetails(@RequestBody Request request) {
        return ResponseEntity
                .ok()
                .body(stationSensorService.getCommunicationDetails(request));
    }
    @PostMapping("/generateLog")
    public ResponseEntity<String> generateLog(@RequestBody Request request) {
        return ResponseEntity
                .ok()
                .body(stationSensorService.generateLog(request));
    }
    @PostMapping("/updateStation")
    public ResponseEntity<String> updateStation(@ModelAttribute StationRequest request) {
        return ResponseEntity
                .ok()
                .body(stationSensorService.updateStation(request));
    }

    @PostMapping("/userStations")
    public ResponseEntity<List<Integer>> getAllUserStation(@RequestBody Request request) {
        return ResponseEntity
                .ok()
                .body(stationSensorService.getAllUserStations(request));
    }


}
