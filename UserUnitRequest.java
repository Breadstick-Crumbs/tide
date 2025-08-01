package com.tridel.tems_sensor_service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUnitRequest {
    private Integer loggedIn;
    private Integer paramId;
    private Integer unitId;
    private Double graphYAxisMin;

}
