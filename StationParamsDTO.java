package com.tridel.tems_sensor_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StationParamsDTO {
    private String paramsToFetch;
    private Integer stationId;

    // Constructor
    public StationParamsDTO(String paramsToFetch, Integer stationId) {
        this.paramsToFetch = paramsToFetch;
        this.stationId = stationId;
    }
    @Override
    public String toString() {
        return "StationParamsDTO{" +
                "paramsToFetch='" + paramsToFetch + '\'' +
                ", stationId=" + stationId +
                '}';
    }
}