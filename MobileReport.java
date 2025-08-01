package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileReport {
	private Double value;
	private String paramName;
	private String dateTime;
	private String unit;
}
