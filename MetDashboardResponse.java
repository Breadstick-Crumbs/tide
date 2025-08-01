package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetDashboardResponse {
    private String latestTime;
    private Map<Integer,MetDashboardData> latest;
    private Map<Integer,MetDashboardData> hourly;
    private Map<Integer,MetDashboardData> tenMin;
}
