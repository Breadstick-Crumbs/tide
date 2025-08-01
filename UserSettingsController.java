package com.tridel.tems_sensor_service.controller;

import com.tridel.tems_sensor_service.model.request.usersettings.SequenceSettingRequest;
import com.tridel.tems_sensor_service.model.request.usersettings.UserStationSensorRequest;
import com.tridel.tems_sensor_service.model.response.usersettings.StationParamResponse;
import com.tridel.tems_sensor_service.model.response.usersettings.UserSettingsResponse;
import com.tridel.tems_sensor_service.service.UserSettingsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stsensor")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Station Sensor Controller", description = "Comprise of all the User Station Sensor services")
public class UserSettingsController {
    UserSettingsService userSettingsSvc;

    UserSettingsController(UserSettingsService userSettingsSvc){
        this.userSettingsSvc = userSettingsSvc;
    }

    @PostMapping("/allParameters")
    public ResponseEntity<List<StationParamResponse>> getAllParameters(@RequestBody UserStationSensorRequest req) {
            return ResponseEntity
                    .ok()
                    .body(userSettingsSvc.getAllParameters(req));
    }

    @PostMapping("/addUserStationSensor")
    public ResponseEntity<String> addUserStationSensor(@RequestBody List<UserStationSensorRequest> req) {
        return ResponseEntity
                .ok()
                .body(userSettingsSvc.addUserStationSensor(req));
    }

    @PostMapping("/userStationSensorDetails")
    public ResponseEntity<String> getUserStationSensorDetail(@RequestBody UserStationSensorRequest req) {
        return ResponseEntity
                .ok()
                .body(userSettingsSvc.getUserStationSensorDetail(req));
    }

    @PostMapping("/customizedDashboardDetails")
    public ResponseEntity<List<UserSettingsResponse>> getCustomizedDashboardDetails(@RequestBody SequenceSettingRequest req) {
        return ResponseEntity
                .ok()
                .body(userSettingsSvc.getCustomizedDashboardDetails(req));
    }

    @PostMapping("/addCustomizedDashboard")
    public ResponseEntity<String> addCustomizedDashboard(@RequestBody SequenceSettingRequest req) {
        return ResponseEntity
                .ok()
                .body(userSettingsSvc.addCustomizedDashboard(req));
    }

    @PostMapping("/updateCustomizedDashboard")
    public ResponseEntity<String> updateCustomizedDashboard(@RequestBody SequenceSettingRequest req) {
        return ResponseEntity
                .ok()
                .body(userSettingsSvc.updateCustomizedDashboard(req));
    }

    @PostMapping("/deleteCustomizedDashboard")
    public ResponseEntity<String> deleteCustomizedDashboard(@RequestBody SequenceSettingRequest req) {
        return ResponseEntity
                .ok()
                .body(userSettingsSvc.deleteCustomizedDashboard(req));
    }

    @PostMapping("/customizedHomeDetails")
    public ResponseEntity<List<UserSettingsResponse>> getCustomizedHomeDetails(@RequestBody SequenceSettingRequest req) {
        return ResponseEntity
                .ok()
                .body(userSettingsSvc.getCustomizedHomeDetails(req));
    }

    @PostMapping("/addCustomizedHome")
    public ResponseEntity<String> addCustomizedHome(@RequestBody SequenceSettingRequest req) {
        return ResponseEntity
                .ok()
                .body(userSettingsSvc.addCustomizedHome(req));
    }

    @PostMapping("/updateCustomizedHome")
    public ResponseEntity<String> updateCustomizedHome(@RequestBody SequenceSettingRequest req) {
        return ResponseEntity
                .ok()
                .body(userSettingsSvc.updateCustomizedHome(req));
    }

    @PostMapping("/deleteCustomizedHome")
    public ResponseEntity<String> deleteCustomizedHome(@RequestBody SequenceSettingRequest req) {
        return ResponseEntity
                .ok()
                .body(userSettingsSvc.deleteCustomizedHome(req));
    }
}
