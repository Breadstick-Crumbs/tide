package com.tridel.tems_sensor_service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notifications {
    private Integer noticeType;
    private String dateTime;
    private Double sensorVal;
    private String paramUnit;
    private Integer parameterId;
    private String paramName;

}
