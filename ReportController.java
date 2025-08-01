package com.tridel.tems_sensor_service.controller;

import com.tridel.tems_sensor_service.model.request.ReportRequest;
import com.tridel.tems_sensor_service.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Report Controller", description = "Report APIs")
public class ReportController {
    ReportService reportService;
    ReportController(ReportService reportService){
        this.reportService = reportService;
    }
    @PostMapping("/loadReportsDataOneMinute")
    public ResponseEntity<String> loadReportsDataOneMinute(@RequestBody ReportRequest request){
        return ResponseEntity
                .ok()
                .body(reportService.loadReportsDataOneMinute(request));
    }
    @PostMapping("/loadReportsDataByInterval")
    public ResponseEntity<String> loadReportsDataByInterval(@RequestBody ReportRequest request){
        return ResponseEntity
                .ok()
                .body(reportService.loadReportsDataByInterval(request));
    }
    @PostMapping("/loadReportsDataHourly")
    public ResponseEntity<String> loadReportsDataHourly(@RequestBody ReportRequest request){
        return ResponseEntity
                .ok()
                .body(reportService.loadReportsDataHourly(request));
    }
    @PostMapping("/loadClimateReportsData")
    public ResponseEntity<String> loadClimateReportsData(@RequestBody ReportRequest request){
        return ResponseEntity
                .ok()
                .body(reportService.loadClimateReportsData(request));
    }
    @PostMapping("/loadClimateIntervalFilterReportsData")
    public ResponseEntity<String> loadClimateIntervalFilterReportsData(@RequestBody ReportRequest request){
        return ResponseEntity
                .ok()
                .body(reportService.loadClimateIntervalFilterReportsData(request));
    }
}
