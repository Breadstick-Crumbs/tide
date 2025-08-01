package com.tridel.tems_sensor_service.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    /*private String stationId;
    private String stationName;
    private String sensorId;
    private String sensorCode;
    private String sensorName;
    private String notifyTypeId;
    private String notifyTypeName;*/
    private String notifyUserName;
    private String notifyUserEmail;
}
