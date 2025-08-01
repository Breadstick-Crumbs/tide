package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTypeResp {
    private String notifyTypeId;
    private String notifyTypeName;
    private String notifyTypeCode;
}
