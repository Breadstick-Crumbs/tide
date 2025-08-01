package com.tridel.tems_sensor_service.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationRecipient {
    private String loggedIn;
    private String stationId;
    private String sensorId;
    private String stationName;
    private String sensorCode;
    private String sensorName;
    private Integer notificationDetailId;
    private String notifyTypeId;
    private String notifyTypeName;
    private Integer notifyModeId;
    private String notifyModeName;
    private String notifyUserName;
    private String notifyUserEmail;

    public NotificationRecipient(Integer notificationDetailId,String stationName, String sensorCode, String sensorName, Integer notifyTypeId, Integer notifyModeId, String notifyModeName,String notifyUserName, String notifyUserEmail) {
        this.stationName = stationName;
        this.sensorCode = sensorCode;
        this.sensorName = sensorName;
        this.notifyTypeId = String.valueOf(notifyTypeId);
        this.notifyModeId = notifyModeId;
        this.notifyModeName = notifyModeName;
        this.notifyUserName = notifyUserName;
        this.notifyUserEmail = notifyUserEmail;
        this.notificationDetailId = notificationDetailId;
    }
    public NotificationRecipient(Integer notificationDetailId, String stationName, Integer notifyTypeId, Integer notifyModeId, String notifyModeName, String notifyUserName, String notifyUserEmail) {
        this.stationName = stationName;
        this.notifyTypeId = String.valueOf(notifyTypeId);
        this.notifyModeId = notifyModeId;
        this.notifyModeName = notifyModeName;
        this.notifyUserName = notifyUserName;
        this.notifyUserEmail = notifyUserEmail;
        this.notificationDetailId = notificationDetailId;
    }
}
