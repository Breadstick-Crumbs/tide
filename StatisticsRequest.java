package com.tridel.tems_sensor_service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsRequest {
    private List<Integer> stationIds;
    private List<Integer> parameters;
    private List<String> windRoseParam;
    private String sensorCode;
    private Integer loggedIn;
    private String fromDate;
    private String toDate;
}
