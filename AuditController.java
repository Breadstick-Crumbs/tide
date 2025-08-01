package com.tridel.tems_sensor_service.controllerlog;

import com.tridel.tems_sensor_service.model.request.ActionLogFilter;
import com.tridel.tems_sensor_service.model.response.UserLog;
import com.tridel.tems_sensor_service.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/logs")
@SecurityRequirement(name = "Authorization")
@Tag(name = "Audit Controller", description = "Comprises of all the audit/log services")
public class AuditController {
    AuditService auditService;
    @Autowired
    AuditController(AuditService auditService){
        this.auditService= auditService;
    }
    @PostMapping("/userLogs")
    @Operation(summary = "User Action Logs", description = "List All User action logs")
    public ResponseEntity<List<UserLog>> getAllUserAction(@RequestBody ActionLogFilter actionLogFilter) {
        return ResponseEntity.ok()
                .body(auditService.getAllUserAction(actionLogFilter));

    }

}
