package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyDashboardData {
    private Integer paramId;
    private String paramValAvg;
    private String paramValMin;
    private String paramValMax;
    private String paramValMinDate;
    private String paramValMaxDate;
    private String paramName;
    private String paramUnit;

    public DailyDashboardData(Integer paramId,Double paramValAvg, Double paramValMin, Double paramValMax,
                              String paramValMinDate, String paramValMaxDate){
        this.paramValAvg = paramValAvg!=null ? String.valueOf(paramValAvg) : "";
        this.paramValMin = paramValMin!=null ? String.valueOf(paramValMin) : "";
        this.paramValMax = paramValMax!=null ? String.valueOf(paramValMax) : "";
        this.paramValMinDate = paramValMinDate!=null ? paramValMinDate : "";
        this.paramValMaxDate = paramValMaxDate!=null ? paramValMaxDate : "";
        this.paramId = paramId;
    }
}
