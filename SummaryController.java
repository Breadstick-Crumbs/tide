package com.tridel.tems_sensor_service.controller;

import com.tridel.tems_sensor_service.model.request.SummaryRequest;
import com.tridel.tems_sensor_service.service.SummaryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Summary Controller", description = "API to load the summary page")
public class SummaryController {
    SummaryService summaryService;
    SummaryController(SummaryService summaryService){
        this.summaryService = summaryService;
    }
    @PostMapping("/loadSummaryData")
    public ResponseEntity<String> loadSummaryData(@RequestBody SummaryRequest request){
        return ResponseEntity
                .ok()
                .body(summaryService.loadSummaryData(request));
    }
}
