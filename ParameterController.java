package com.tridel.tems_sensor_service.controller;

import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.response.SensorParamView;
import com.tridel.tems_sensor_service.model.response.UserSensorParamUnitsResponse;
import com.tridel.tems_sensor_service.model.response.usersettings.SensorParamResponse;
import com.tridel.tems_sensor_service.service.ParameterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/param")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Parameter Controller", description = "Comprise of all the parameter data services")
public class ParameterController {
    ParameterService parameterService;
    ParameterController(ParameterService parameterService){
        this.parameterService=parameterService;
    }
    @PostMapping("/loadParametersByType")
    public ResponseEntity<List<SensorParamView>> loadParametersByType(@RequestBody Request request){
        return ResponseEntity
                .ok()
                .body(parameterService.loadParametersByType(request));
    }
    @PostMapping("/getAllParamsBySensor")
    public ResponseEntity<List<SensorParamView>> getAllParamsBySensor(@RequestBody Request request) {
        return ResponseEntity
                .ok()
                .body(parameterService.getAllParamsBySensor(request));

    }
    @PostMapping("/getAllParamsBySensorAndUser")
    public ResponseEntity<List<UserSensorParamUnitsResponse>> getAllParamsBySensorAndUser(@RequestBody Request request) {
        return ResponseEntity
                .ok()
                .body(parameterService.getAllParamsBySensorAndUser(request));

    }

    @PostMapping("/allParamsBySensorAndUser")
    public ResponseEntity<List<SensorParamResponse>> getAllParameterBySensorAndUser(@RequestBody Request request) {
        return ResponseEntity
                .ok()
                .body(parameterService.getAllParameterBySensorAndUser(request));

    }
    @PostMapping("/getAllStatisticsParamsBySensor")
    public ResponseEntity<String> getAllStatisticsParamsBySensor(@RequestBody Request req) {
        return ResponseEntity
                .ok()
                .body(parameterService.getAllStatisticsParamsBySensor(req));
    }

    @PostMapping("/loadSelectedParamDetails")
    public ResponseEntity<String> loadSelectedParamDetails(@RequestBody Request req) {
        return ResponseEntity
                .ok()
                .body(parameterService.loadSelectedParam(req));
    }
    @PostMapping("/updateParamDetails")
    public ResponseEntity<String> updateParamDetails(@RequestBody SensorParamView req, HttpServletRequest httpRequest) {
        return ResponseEntity
                .ok()
                .body(parameterService.updateParamDetails(req,httpRequest));
    }

}
