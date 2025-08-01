package com.tridel.tems_sensor_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationDetails {
    private Integer stationId;
    private Boolean isBuoyType;
    private Integer buoyWatchCircleWarn;
    private Integer buoyWatchCircleDanger;
    private String parameterDateTime;
    private Boolean maintenanceFlag;
    private Date maintenanceSDate;
    private Date maintenanceEDate;
    private String latitude;
    private String longitude;
    private Boolean switchState;
    private Integer stationTypeId;
    private String stationTypeName;
    private String stationImg;
    private String stationName;
    private String stationCode;
    private String locationDetails;
    private Boolean mobilityFlag;
    private Double buoyDepth;
    private String disName;
    private String legend;

    public StationDetails(Integer stationId, Boolean isBuoyType, Integer buoyWatchCircleWarn, Integer buoyWatchCircleDanger,
                          String parameterDateTime, Boolean maintenanceFlag, String latitude, String longitude,
                          Boolean switchState, String stationName, String stationCode, Boolean mobilityFlag,
                          String disName, String legend) {
        this.stationId = stationId;
        this.isBuoyType = isBuoyType;
        this.buoyWatchCircleWarn = buoyWatchCircleWarn;
        this.buoyWatchCircleDanger = buoyWatchCircleDanger;
        this.parameterDateTime = parameterDateTime;
        this.maintenanceFlag = maintenanceFlag;
        this.latitude = latitude;
        this.longitude = longitude;
        this.switchState = switchState;
        this.stationName = stationName;
        this.stationCode = stationCode;
        this.mobilityFlag = mobilityFlag;
        this.disName = disName;
        this.legend = legend;
    }

    public StationDetails(Integer stationId, String stationName){
        this.stationId = stationId;
        this.stationName = stationName;
    }
}
