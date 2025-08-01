package com.tridel.tems_sensor_service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private Integer id;
    private Integer loggedIn;
    private Integer paramId;
    private String sensorCode;
    private String station;
    private Integer type;
    private List<Integer> stationIds;
}
