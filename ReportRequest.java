package com.tridel.tems_sensor_service.model.request;

import com.tridel.tems_sensor_service.model.response.SensorParamView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {

    private String station;
    private Integer loggedIn;
    private String sensorCode;
    private List<SensorParamView> paramList;
    private String sensorTableCode;
    private String fromDate;
    private String toDate;
    private String standardTime;
    private Integer stationId;
    private String interval;
    private Integer minIntrvl;
    private String dateType;
    private List<String> functions;
    private List<Boolean> containsFunctions;
}
