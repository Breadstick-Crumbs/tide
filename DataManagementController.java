package com.tridel.tems_sensor_service.controller;

import com.tridel.tems_sensor_service.model.request.DataMgmtRequest;
import com.tridel.tems_sensor_service.model.request.EditQCRequest;
import com.tridel.tems_sensor_service.service.DataManagementService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/datamgmt")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Data Management Controller", description = "Comprise of all the Data Management APIs")
public class DataManagementController {

    DataManagementService dataManagementService;

    DataManagementController(DataManagementService dataManagementService){
        this.dataManagementService = dataManagementService;
    }



    @PostMapping("/loadAllQCData")
    public ResponseEntity<String> loadAllQCData(@RequestBody DataMgmtRequest request){
        return ResponseEntity
                .ok()
                .body(dataManagementService.loadAllQCData(request));
    }

    @PostMapping("/editQCData")
    public ResponseEntity<String> editQCData(@RequestBody EditQCRequest req) {
        return ResponseEntity
                .ok()
                .body(dataManagementService.editQCData(req));
    }

}
