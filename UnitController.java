package com.tridel.tems_sensor_service.controller;

import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.request.UserUnitRequest;
import com.tridel.tems_sensor_service.model.response.UnitResponse;
import com.tridel.tems_sensor_service.service.UserUnitService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unit")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Unit data Controller", description = "Comprise of all the Unit data services")
public class UnitController {

    UserUnitService userUnitSvc;

    UnitController(UserUnitService userUnitSvc){this.userUnitSvc=userUnitSvc;}


    @PostMapping("/updateUserUnitSettings")
    public ResponseEntity<String> updateUserUnitSettings(@RequestBody UserUnitRequest request, HttpServletResponse response) {
            return ResponseEntity.ok()
                    .body(userUnitSvc.updateUserUnitSettings(request));
    }

    @PostMapping("/getUnitByUserParam")
    public ResponseEntity<List<UnitResponse>> getUnitByUserParam(@RequestBody UserUnitRequest request, HttpServletResponse response) {
        return ResponseEntity.ok()
                .body(userUnitSvc.getUnitByUserParam(request));
    }

    @PostMapping("/getParamUnitConversion")
    public ResponseEntity<String> getParamUnitConversion(@RequestBody Request request) {
        return ResponseEntity
                .ok()
                .body(userUnitSvc.getParamUnitConversion(request));
    }
}
