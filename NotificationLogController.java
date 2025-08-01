package com.tridel.tems_sensor_service.controllerlog;

import com.tridel.tems_sensor_service.model.request.NotificationStatusReq;
import com.tridel.tems_sensor_service.model.request.NotificationsReq;
import com.tridel.tems_sensor_service.model.response.NotificationTypeResp;
import com.tridel.tems_sensor_service.model.response.SensorNotificationResponse;
import com.tridel.tems_sensor_service.service.NotificationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin
@RequestMapping("/api/notifications")
@SecurityRequirement(name = "Authorization")
@Tag(name = "Notification Controller", description = "Comprises of all the notification services")
public class NotificationLogController {
    NotificationLogService notificationService;
    @Autowired
    NotificationLogController(NotificationLogService notificationService){
        this.notificationService = notificationService;
    }
    @PostMapping("/allNotifications")
    @Operation(summary = "Sensor Notifications", description = "List all sensor notifications")
    public ResponseEntity<List<SensorNotificationResponse>> getAllSensorNotifications(@RequestBody NotificationsReq req) {
        return ResponseEntity.ok()
                .body(notificationService.getAllUnreadNotification(req.getStationIds()));

    }
    @GetMapping("/allNotificationTypes")
    @Operation(summary = "Notifications Types", description = "List all Notifications Types")
    public ResponseEntity<List<NotificationTypeResp>> getAllNotificationType() {
        return ResponseEntity
                    .ok()
                    .body(notificationService.getAllNotificationType());
    }
    @PostMapping("/AllAlarmNotify")
    @Operation(summary = "Alarm Notifications", description = "List all Alarm Notifications")
    public ResponseEntity<List<SensorNotificationResponse>> getAllAlarmNotify(@RequestBody NotificationsReq request) {
        return ResponseEntity
                    .ok()
                    .body(notificationService.getAllAlarmNotify(request));

    }
    @PostMapping("/StationNotification")
    @Operation(summary = "Station Notifications", description = "Load all Station Notifications")
    public ResponseEntity<List<SensorNotificationResponse>> loadStationNotification(@RequestBody NotificationsReq request) {
        return ResponseEntity
                    .ok()
                    .body(notificationService.loadStationNotification(request));
    }

    @PostMapping("/updateNotificationStatus")
    public ResponseEntity<String> updateNotificationStatus(@RequestBody NotificationStatusReq request) {
        try{
            return ResponseEntity
                    .ok()
                    .body(notificationService.updateNotificationStatus(request));
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
