package com.tridel.tems_sensor_service.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetDashboardData {
    private Integer paramId;
    private String paramName;
    private String avg;
    private String min;
    private String max;
    private String unit;
}
