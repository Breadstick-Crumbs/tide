package com.tridel.tems_sensor_service.controller;

import com.tridel.tems_sensor_service.model.request.NotificationRecipient;
import com.tridel.tems_sensor_service.model.request.SensorDataMailRequest;
import com.tridel.tems_sensor_service.model.response.MailDetails;
import com.tridel.tems_sensor_service.service.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Notification Controller", description = "Comprise of all the notification APIs")
public class NotificationController {
    NotificationService notificationService;
    NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }
    @PostMapping("/getAllNotificationRecipient")
    public ResponseEntity<List<NotificationRecipient>> getAllNotificationRecipient(@RequestBody NotificationRecipient request){
        return ResponseEntity
                .ok()
                .body(notificationService.getAllNotificationRecipients(request));
    }
    @PostMapping("/addNotificationRecipients")
    public ResponseEntity<String> addNotificationRecipients(@RequestBody NotificationRecipient request, HttpServletRequest httpServletRequest){
        return ResponseEntity
                .ok()
                .body(notificationService.addNotificationRecipients(request,httpServletRequest));
    }
    @PostMapping("/deleteNotificationRecipients")
    public ResponseEntity<String> deleteNotificationRecipients(@RequestBody NotificationRecipient request, HttpServletRequest httpServletRequest){
        return ResponseEntity
                .ok()
                .body(notificationService.deleteNotificationRecipients(request,httpServletRequest));
    }
    @PostMapping("/mailDetails")
    public ResponseEntity<MailDetails> getMailDetails(@RequestBody SensorDataMailRequest request){
        return ResponseEntity
                .ok()
                .body(notificationService.getMailDetails(request));
    }
}
