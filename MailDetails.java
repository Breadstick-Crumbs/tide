package com.tridel.tems_sensor_service.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MailDetails {
    private String[] emails;
    private String stationCode;
    private String locationDetails;
}
