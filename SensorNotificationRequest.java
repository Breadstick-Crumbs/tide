package com.tridel.tems_sensor_service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorNotificationRequest {
//    private Integer notifyId;
    private Integer notifyTypeId;
    private Integer sensorId;
    private String sensorCode;
    private Integer stationId;
    private String stationName;
//    private String noticeLocation;
    private List<Notifications> notifications;

}
