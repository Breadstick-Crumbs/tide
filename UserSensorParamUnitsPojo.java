package com.tridel.tems_sensor_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
public class UserSensorParamUnitsPojo {
	private String operation;
	private BigDecimal calculatedValue;
	private String unitSymbol;
	private Double graphYAxisMin;

	public UserSensorParamUnitsPojo(String operation, BigDecimal calculatedValue, String unitSymbol,Double graphYAxisMin) {
		this.operation = operation;
		this.calculatedValue = calculatedValue;
		this.unitSymbol = unitSymbol;
		this.graphYAxisMin = graphYAxisMin;
	}
}
