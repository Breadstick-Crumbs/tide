package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationCommunicationResponse {
    private int stationId;
    private String stationCode;
    private String stationName;
    private String stationImg;
    private String latestDateTime;
    private String batteryVoltage;
    private Boolean activeFlag;
    private String batteryMin;
    private String batteryMax;
    private String batteryUnit;

    public StationCommunicationResponse(StationResponse station){
        this.stationId = station.getStationId();
        this.stationName = station.getStationName();
        this.stationCode = station.getStationCode();
        this.stationImg = station.getStationImg() == null ? "" : station.getStationImg();

    }

}
