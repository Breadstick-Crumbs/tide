package com.tridel.tems_sensor_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamDtlsPojo {
    private Integer headerIndex;
    private Integer paramId;
    private String paramName;
    private Double warn;
    private String warnOp;
    private Double danger;
    private String dangerOp;

}
