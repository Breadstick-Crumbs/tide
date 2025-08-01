package com.tridel.tems_sensor_service.controller;

import com.tridel.tems_sensor_service.entity.master.*;
import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.modelmaster.ReportRequest;
import com.tridel.tems_sensor_service.service.MasterDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Master data Controller", description = "Comprises of all the look up services")
public class MasterDataController {
      MasterDataService masterDataService;

    public MasterDataController(MasterDataService masterDataService) {
        this.masterDataService = masterDataService;
    }

    @GetMapping("/calibrationTypes")
    @Operation(summary = "Get all the calibration types", description = "Get all the calibration types")
    public ResponseEntity<List<SensorCalibrationType>> getAllCalibrationTypes() {
            return ResponseEntity
                    .ok()
                    .body(masterDataService.getAllCalibrationTypes());
    }
    @PostMapping("/calibrationType")
    @Operation(summary = "Get the calibration types", description = "Get the calibration type b ased on Id")
    public ResponseEntity<SensorCalibrationType> getCalibrationTypeById(@RequestParam int id) {
        return ResponseEntity
                .ok()
                .body(masterDataService.getCalibrationTypeById(id));
    }
    @GetMapping("/communicationTypes")
    @Operation(summary = "Get all the communication types", description = "Get all the communication types")
    public ResponseEntity<List<CommunicationType>> getAllCommunicationTypes() {
        return ResponseEntity
                .ok()
                .body(masterDataService.getAllCommunicationTypes());
    }
    @GetMapping("/stationLegends")
    @Operation(summary = "Get all the station legends", description = "Get all the station legends")
    public ResponseEntity<List<StationLegends>> getAllStationLegends() {
        return ResponseEntity
                .ok()
                .body(masterDataService.getAllStationLegends());
    }
    @GetMapping("/stationTypes")
    @Operation(summary = "Get all the station types", description = "Get all the station types")
    public ResponseEntity<List<StationType>> getAllStationTypes() {
        return ResponseEntity
                .ok()
                .body(masterDataService.getAllStationTypes());
    }
    @GetMapping("/sensorTypes")
    @Operation(summary = "Get all the sensor types", description = "Get all the sensor types")
    public ResponseEntity<List<SensorType>> getAllSensorTypes() {
        return ResponseEntity
                .ok()
                .body(masterDataService.getAllSensorTypes());
    }
    @GetMapping("/sensorRanges")
    @Operation(summary = "Get all the sensor range", description = "Get all the sensor range")
    public ResponseEntity<List<SensorRange>> getAllSensorRange() {
        return ResponseEntity
                .ok()
                .body(masterDataService.getAllSensorRanges());
    }
    @PostMapping("/sensorRange")
    @Operation(summary = "Get the sensor range of a parameter", description = "Get the sensor range of a parameter")
    public ResponseEntity<SensorRange> getSensorRangeById(@RequestParam int id) {
        return ResponseEntity
                .ok()
                .body(masterDataService.getSensorRangeByParameterId(id));
    }
    @GetMapping("/reportPeriods")
    @Operation(summary = "Get all the report period", description = "Get all the report period")
    public ResponseEntity<List<ReportPeriod>> getAllReportPeriods() {
        return ResponseEntity
                .ok()
                .body(masterDataService.getAllReportPeriods());
    }
    @PostMapping("/reportPeriod")
    @Operation(summary = "Get the report period by id", description = "Get the report period by id")
    public ResponseEntity<ReportPeriod> getReportPeriodById(@RequestParam int id) {
        return ResponseEntity
                .ok()
                .body(masterDataService.getReportPeriodsById(id));
    }
    @PostMapping("/reportIntervals")
    @Operation(summary = "Get all the report interval", description = "Get all the report interval")
    public ResponseEntity<List<ReportInterval>> getAllReportIntervals(@RequestBody ReportRequest request) {
        return ResponseEntity
                .ok()
                .body(masterDataService.getAllReportIntervals(request.getModule()));
    }
    @PostMapping("/reportInterval")
    @Operation(summary = "Get the report interval by id", description = "Get the report interval by id")
    public ResponseEntity<ReportInterval> getReportIntervalById(@RequestBody Request req) {
        return ResponseEntity
                .ok()
                .body(masterDataService.getReportIntervalById(req.getId()));
    }
    @GetMapping("/reportFunctions")
    @Operation(summary = "Get all the report functions", description = "Get all the report functions")
    public ResponseEntity<List<ReportFunctions>> getAllReportFunctions() {
        return ResponseEntity
                .ok()
                .body(masterDataService.getAllReportFunctions());
    }
    @PostMapping("/reportFunction")
    @Operation(summary = "Get the report function by id", description = "Get the report function by id")
    public ResponseEntity<ReportFunctions> getReportFunctionById(@RequestParam int id) {
        return ResponseEntity
                .ok()
                .body(masterDataService.getReportFunctionsById(id));
    }
    @GetMapping("/aqiReference")
    @Operation(summary = "Get all the aqi ref", description = "Get all the aqi ref")
    public ResponseEntity<List<AQIReference>> getAllAqiReferences() {
        return ResponseEntity
                .ok()
                .body(masterDataService.getAllAQIreference());
    }
    @GetMapping("/appSettings")
    @Operation(summary = "Get all the app settings", description = "Get all the app settings")
    public ResponseEntity<ApplicationSettings> getAppSettings() {
        return ResponseEntity
                .ok()
                .body(masterDataService.getAppSettings());
    }

    @GetMapping("/test")
    @Operation(summary = "test", description = "test")
    public ResponseEntity<String> test() {
        return ResponseEntity
                .ok()
                .body("Hello test");
    }

}
