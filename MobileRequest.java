package com.tridel.tems_sensor_service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileRequest {
    private Integer loggedIn;
    private Integer sensor;
    private Integer station;
    private String fromDate;
    private String toDate;
    private Integer interval;
}