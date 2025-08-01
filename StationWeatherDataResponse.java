package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationWeatherDataResponse {
    private Integer stationId;
    private String stationName;
    private String displayName;
    private String stationCode;
    private String latitude;
    private String longitude;
    private String locationDetails;
    private Boolean mobilityFlag;
    private Object displayRoundTo;
    private String latestTime;
    private String airTemperature;
    private String relativeHumidity;
    private String windDirection;
    private String windSpeed;
    private String rainfall;
    private String qnh;
    private String visibility;


    public StationWeatherDataResponse(String nullString) {
        this.airTemperature = nullString;
        this.qnh = nullString;
        this.relativeHumidity = nullString;
        this.windDirection = nullString;
        this.windSpeed = nullString;
        this.rainfall = nullString;
        this.visibility = nullString;
        this.locationDetails ="";
    }
}
