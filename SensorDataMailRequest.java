package com.tridel.tems_sensor_service.model.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SensorDataMailRequest {
    private Integer stationId;
    private Integer sensorId;
    private Integer notifyTypeId;
}
