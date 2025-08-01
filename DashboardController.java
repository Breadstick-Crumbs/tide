package com.tridel.tems_sensor_service.controller;

import com.tridel.tems_sensor_service.model.request.*;
import com.tridel.tems_sensor_service.model.response.*;
import com.tridel.tems_sensor_service.service.DashboardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Dashboard Controller", description = "Comprise of all the Dashboard APIs")
public class DashboardController {
    DashboardService dashboardService;
    DashboardController(DashboardService dashboardService){
        this.dashboardService = dashboardService;
    }
    @PostMapping("/loadCustomDashboard")
    public ResponseEntity<DashboardSensorDataResponse> loadCustomDashboard(@RequestBody Request request){
        return ResponseEntity
                .ok()
                .body(dashboardService.getSensorDetailsForDashboard(request));
    }
    @PostMapping("/loadDashboardDataForStation")
    public ResponseEntity<List<DailyDashboardData>> loadDashboardDataForStation(@RequestBody DailyDashboardRequest request){
        return ResponseEntity
                .ok()
                .body(dashboardService.loadDashboardDataForStation(request));
    }
    @PostMapping("/loadDashboardDataForWind")
    public ResponseEntity<WindDashboardData> loadDashboardDataForWind(@RequestBody DailyDashboardRequest request){
        return ResponseEntity
                .ok()
                .body(dashboardService.loadDashboardWindDataForMET(request));
    }
    @PostMapping("/loadDashboardDataForMet")
    public ResponseEntity<MetDashboardResponse> loadDashboardDataForMet(@RequestBody DailyDashboardRequest request){
        return ResponseEntity
                .ok()
                .body(dashboardService.loadDashboardDataForMET(request));
    }
    @PostMapping("/loadCentricDashboard")
    public ResponseEntity<List<CentricDashboardData>> loadCentricDashboard(@RequestBody CentricDashboardRequest request){
        return ResponseEntity
                .ok()
                .body(dashboardService.loadCentricDashboard(request));
    }

}
