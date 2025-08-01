package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorResponse {
    private int sensorId;
    private String sensorCode;
    private String sensorName;
    private Integer sensorOrder;
}
