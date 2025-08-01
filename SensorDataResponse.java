package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataResponse {
    private String sensorName;
    private String sensorCode;
    private Integer sensorOrder;
    private List<SensorParamView> sensorData;
}
