package com.tridel.tems_sensor_service.controllerlog;

import com.tridel.tems_sensor_service.model.response.SensorNotificationResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendMessageToWebSocket(SensorNotificationResponse sensorNotificationResponse) {
        simpMessagingTemplate.convertAndSend("/topic/notification", sensorNotificationResponse); // Send message to all subscribers on /topic/notification
    }

}
