package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParameterUnitPojo {
    private Integer paramUnitId;
    private String unitSymbol;
    private String operation;
    private BigDecimal calculatedValue;

//    public ParameterUnitPojo(Integer paramUnitId, String unitSymbol, String operation, String calculatedValue) {
//        this.paramUnitId = paramUnitId;
//        this.unitSymbol = unitSymbol;
//        this.operation = operation;
//        this.calculatedValue = calculatedValue;
//    }
}
