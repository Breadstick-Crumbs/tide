package com.tridel.tems_sensor_service.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CentricDashboardData {
    private Integer stationId;
    private String stationName;
    private String date;
    private String paramName;
    private Double warn;
    private Double danger;
    private String warnOp;
    private String dangerOp;
    private String data;
    private String unit;
}
