package com.tridel.tems_sensor_service.controller;

import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.request.StatisticsRequest;
import com.tridel.tems_sensor_service.service.StatisticsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Statistics Controller", description = "Comprise of all the statistics data services")
public class StatisticsController {
    StatisticsService statisticsService;
    StatisticsController(StatisticsService statisticsService){
        this.statisticsService = statisticsService;
    }
    @PostMapping("/loadParameterDataGraph")
    public ResponseEntity<String> loadParameterDataGraph(@RequestBody Request request) {
        return ResponseEntity
                .ok()
                .body(statisticsService.loadParameterDataGraph(request));

    }
    @PostMapping("/loadStatisticsData")
    public ResponseEntity<String> loadStatisticsData(@RequestBody StatisticsRequest request) {
        return ResponseEntity
                .ok()
                .body(statisticsService.loadStatisticsData(request));

    }
    @PostMapping("/loadAdvStatisticsData")
    public ResponseEntity<String> loadAdvStatisticsData(@RequestBody StatisticsRequest request) {
        return ResponseEntity
                .ok()
                .body(statisticsService.loadAdvStatisticsData(request));

    }
}
