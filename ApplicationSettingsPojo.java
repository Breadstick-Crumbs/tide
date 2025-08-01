package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSettingsPojo {
    private Integer applicationSettingsId;
    private String jwtSecret;
    private long jwtTokenExpiryTime;
    private long jwtRefreshTokenExpiryTime;
    private Integer pollingIntervalMin;
    private Integer noSignalTimeIntervalMin;
    private Integer utcConversionInMin;
    private Integer appTimeZone;
}
