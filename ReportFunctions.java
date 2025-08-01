package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportFunctions {
	private int reportFunctionsId;
	private String reportFunctionsNameEN;
	private String reportFunctionsNameAR;
}
