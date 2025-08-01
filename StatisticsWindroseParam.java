package com.tridel.tems_sensor_service.model.request;

import com.tridel.tems_sensor_service.model.WindRoseRange;
import com.tridel.tems_sensor_service.model.response.SensorParamView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsWindroseParam {
    private String paramName;
    SensorParamView parameterOne;
    SensorParamView parameterTwo;
    private List<WindRoseRange> windRoseRanges;
}
