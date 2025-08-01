package com.tridel.tems_sensor_service.controller;

import com.tridel.tems_sensor_service.config.MQConfig;
import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.request.SensorNotifications;
import com.tridel.tems_sensor_service.model.response.SensorResponse;
import com.tridel.tems_sensor_service.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensor")
@CrossOrigin
@SecurityRequirement(name = "Authorization")
@Tag(name = "Sensor data Controller", description = "Comprise of all the Sensor data services")
public class SensorController {
    SensorService sensorService;
    RabbitTemplate rabbitTemplate;

    SensorController(SensorService sensorService,RabbitTemplate rabbitTemplate){
        this.sensorService = sensorService;
        this.rabbitTemplate = rabbitTemplate;
    }
    @GetMapping("/test")
    @Operation(summary = "test", description = "test")
    public ResponseEntity<String> test() {
        SensorNotifications sensorNotifications = new SensorNotifications();
        sensorNotifications.setNotifyId(1);
        rabbitTemplate.convertAndSend(MQConfig.SENSOREXCHANGE, MQConfig.SENSOR_ROUTING_KEY, sensorNotifications);
        return ResponseEntity
                .ok()
                .body("Sensor test");
    }
    @PostMapping("/getAllSensorsByUser")
    public ResponseEntity<List<SensorResponse>> getAllSensorsByUser(@RequestBody Request req) {
            return ResponseEntity
                    .ok()
                    .body(sensorService.getAllSensorsByUser(req));
    }
    @PostMapping("/getAllSensorsByStations")
    public ResponseEntity<List<SensorResponse>> getAllSensorsByStations(@RequestBody Request req) {
        return ResponseEntity
                .ok()
                .body(sensorService.getAllSensorsByStations(req));
    }


}
