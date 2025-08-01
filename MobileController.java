package com.tridel.tems_sensor_service.controller;

import com.tridel.tems_sensor_service.model.request.MobileRequest;
import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.response.MobileReport;
import com.tridel.tems_sensor_service.service.MobileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mobile")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Mobile Controller", description = "Comprise of all the Mobile API services")
public class MobileController {

    MobileService mobileSvc;

    MobileController(MobileService mobileSvc){this.mobileSvc=mobileSvc;}


    @PostMapping("/getStationStatus")
    public ResponseEntity<String> getStationStatus(@RequestBody Request request) {
            return ResponseEntity.ok()
                    .body(mobileSvc.getStationStatus(request));
    }

    @PostMapping("/loadReports")
    public ResponseEntity<List<MobileReport>> loadReports(@RequestBody MobileRequest request) {
        return ResponseEntity.ok()
                .body(mobileSvc.loadReports(request));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok()
                .body("mobileSvc.loadReports(request)");
    }

//    @PostMapping("/getUnitByUserParam")
//    public ResponseEntity<List<UnitResponse>> getUnitByUserParam(@RequestBody UserUnitRequest request, HttpServletResponse response) {
//        return ResponseEntity.ok()
//                .body(mobileSvc.getUnitByUserParam(request));
//    }
//
//    @PostMapping("/getParamUnitConversion")
//    public ResponseEntity<String> getParamUnitConversion(@RequestBody Request request) {
//        return ResponseEntity
//                .ok()
//                .body(mobileSvc.getParamUnitConversion(request));
//    }
}
