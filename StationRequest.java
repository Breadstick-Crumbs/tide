package com.tridel.tems_sensor_service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationRequest {
    private String loggedIn;
    private Integer stationId;
    private String stationCode;
    private String stationName;
    private Integer stationTypeId;
    private String stationTypeName;
    private String stationTypeCode;
    private MultipartFile stationImg;
    private Double latitude;
    private Double longitude;
    private String locationDetails;
    private Boolean maintenanceFlag;
    private String maintenanceStartDate;
    private String maintenanceEndDate;
    private Boolean imgChanged;
    private Boolean noSignalFlag;
    private Boolean isBuoyFlag;
    private Integer buoyWatchCircleWarn;
    private Integer buoyWatchCircleDanger;
}
