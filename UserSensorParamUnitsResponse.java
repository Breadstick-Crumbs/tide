package com.tridel.tems_sensor_service.model.response;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserSensorParamUnitsResponse {
	private Integer paramId;
	private String paramName;
	private String paramDisplayName;
	private String unitSymbol;
	private Double graphYAxisMin;

	public UserSensorParamUnitsResponse(Integer id, String paramDisplayName,String name, String unitSymbol,Double graphYAxisMin) {
		this.paramId = id;
		this.paramName = name;
		this.paramDisplayName = paramDisplayName;
		this.unitSymbol = unitSymbol!=null ? unitSymbol : "-";
		this.graphYAxisMin = graphYAxisMin;
	}
}
