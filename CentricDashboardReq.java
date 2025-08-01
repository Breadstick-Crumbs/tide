package com.tridel.tems_sensor_service.model.request;

import com.tridel.tems_sensor_service.model.response.SensorParamView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CentricDashboardReq {
    private List<Integer> stationIdList;
    private SensorParamView param;
    private String sensorTableCode;
}
