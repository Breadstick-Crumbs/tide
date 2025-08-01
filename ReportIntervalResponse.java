package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportIntervalResponse {

	private int reportIntervalId;
	private String reportIntervalEN;
	private String reportIntervalAR;
	private boolean reportIntervalFlag;
	private String reportBaseInterval;
	private int reportBaseIntervalCount;
	private String reportBaseIntervalMinutes;

}
