package com.tridel.tems_sensor_service.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tridel.tems_sensor_service.entity.StationData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationResponse {
    private int stationId;
    private String stationCode;
    private String stationName;
    private Integer stationType;
    private String stationTypeName;
    private String stationImg;
    private String legend;
    private Double latitude;
    private Double longitude;
    private Double latestLat;
    private Double latestLong;
    private String locationDetails;
    private Boolean mobilityFlag;
    private Boolean noSignalFlag;
    private Boolean switchState;
    private Boolean maintenanceFlag;
    private Date maintenanceStartDate;
    private String maintenanceEndDate;
    private Boolean isBuoyFlag;
    private JSONObject headerData;
    private String buoyWatchCircleWarn;
    private String buoyWatchCircleDanger;


    public StationResponse(StationData station){
        this.stationId = station.getId();
        this.stationName = station.getStationName();
        this.stationCode = station.getStationCode();
        this.stationType = station.getStationType().getStationTypeId();
        this.latitude = station.getLatitude();
        this.longitude = station.getLongitude();
        this.switchState = false;
        this.locationDetails = station.getLocationDetails() != null ? station.getLocationDetails() : "";
        this.mobilityFlag = station.getMobilityFlag()!=null ? station.getMobilityFlag() : false;
        this.noSignalFlag = station.getNoSignalFlag()!=null ? station.getNoSignalFlag() : false;
        this.maintenanceFlag = station.getMaintenanceFlag()!=null ? station.getMaintenanceFlag() : false;
        this.maintenanceStartDate = station.getMaintenanceStartDate();
        this.isBuoyFlag = station.getIsBuoyType()!=null ? station.getIsBuoyType() : false;
        this.buoyWatchCircleWarn = station.getBuoyWatchCircleWarn()!=null ?
                String.valueOf(station.getBuoyWatchCircleWarn()) : "";
        this.buoyWatchCircleDanger = station.getBuoyWatchCircleDanger()!=null ?
                String.valueOf(station.getBuoyWatchCircleDanger()) : "";
        this.stationImg = station.getStationImg()!=null ? station.getStationImg() : "";
    }

    public StationResponse(int stationId, String stationCode, String stationName) {
        this.stationId = stationId;
        this.stationCode = stationCode;
        this.stationName = stationName;
    }

    public StationResponse(int stationId, String stationCode, String stationName, String stationImg) {
        this.stationId = stationId;
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.stationImg = stationImg;
    }
}
