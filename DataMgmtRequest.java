package com.tridel.tems_sensor_service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataMgmtRequest {
    private Integer loggedIn;
    private Integer stationId;
    private String fromDate;
    private String toDate;
    private String sensorCode;
}
