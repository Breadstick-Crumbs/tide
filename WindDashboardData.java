package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WindDashboardData {
    private String windSpeedAvg;
    private String windSpeedMin;
    private String windSpeedMax;
    private String windDirectionAvg;
    private String windDirectionMin;
    private String windDirectionMax;
    private String windSpeedUnit;
    private String windDirectionUnit;
    private String windGustSpeedAvg;
    private String windGustDirectionAvg;
    private String windGustSpeedUnit;
    private String windGustDirectionUnit;


}
