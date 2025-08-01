package com.tridel.tems_sensor_service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyDashboardRequest {
    private String loggedIn;
    private Integer stationId;
    private String interval;
}
