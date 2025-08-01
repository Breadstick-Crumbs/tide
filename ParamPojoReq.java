package com.tridel.tems_sensor_service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamPojoReq {
    private String parameterName;
    private String operation;
    private BigDecimal calculatedValue;
    private Integer displayRoundTo;
    private Boolean isAverageParam;
}
