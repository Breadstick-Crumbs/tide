package com.tridel.tems_sensor_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorParamViewReq {

    private Integer stationId;
    private String sensorCode;
    private String params;
    private String sensorTableCode;
    private boolean isWeather;

    public SensorParamViewReq(Integer stationId, String sensorCode, String sensorTableCode) {
        this.stationId = stationId;
        this.sensorCode = sensorCode;
        this.sensorTableCode = sensorTableCode;
    }
}
