package com.tridel.tems_sensor_service.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tridel.tems_sensor_service.entity.StationData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomeSensorDataResponse {

    private String stationType;
    private String stationLat;
    private String stationLong;
    private Boolean buoyType;
    private Integer buoyWarn;
    private Integer buoyDanger;
    private String latestLat;
    private String latestLong;
    private String latestTime;
    private Boolean stationStatus;
    private List<SensorDataResponse> sensorDataList;

    public HomeSensorDataResponse(StationData stationData){
        this.stationLat = stationData.getLatitude() != null ? String.valueOf(stationData.getLatitude()) : "";
        this.stationLong =  stationData.getLongitude() != null ? String.valueOf(stationData.getLongitude()) : "";
        this.buoyWarn =  stationData.getBuoyWatchCircleWarn() != null ? stationData.getBuoyWatchCircleWarn() : null;
        this.buoyDanger =  stationData.getBuoyWatchCircleDanger() != null ? stationData.getBuoyWatchCircleDanger() : null;
        this.buoyType =  stationData.getIsBuoyType() != null ? stationData.getIsBuoyType() : false;
    }

}
