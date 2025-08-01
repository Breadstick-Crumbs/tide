package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationTypePojo {
	private Integer stationTypeId;
	private String stationTypeName;
	private String stationTypeCode;
	private String stationTypeIcon;
}
