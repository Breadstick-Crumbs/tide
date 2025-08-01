package com.tridel.tems_sensor_service.model.response;

import com.tridel.tems_sensor_service.entity.UserSensorParamUnits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitResponse {
	private Integer unitId;
	private String unitSymbol;
	private boolean flag;

	public UnitResponse(UnitResponse pu, UserSensorParamUnits upu) {

		this.unitId = (pu.getUnitId()!=null ? pu.getUnitId() : null);
		this.unitSymbol = (pu.getUnitSymbol()!=null ? pu.getUnitSymbol() : "");
		this.flag = (pu.getUnitId() != null && upu!=null && upu.getParamUnitId() != null
				&& upu.getParamUnitId().getParamUnitId() != null
				&& Objects.equals(pu.getUnitId(), upu.getParamUnitId().getUnitId().getUnitId()));
	}

	public UnitResponse(Integer unitId, String unitSymbol) {
		this.unitId = unitId;
		this.unitSymbol = unitSymbol;
	}
}
