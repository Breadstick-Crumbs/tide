package com.tridel.tems_sensor_service.model.request;

import com.tridel.tems_sensor_service.model.response.SensorParamView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDataRequest {
    private Integer stationId;
    private SensorParamView paramDtls;
    private String sensorTableCode;
    private Double graphYAxisMin;
}
