package com.tridel.tems_sensor_service.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationLegendsPojo {
	private Integer legendId;
	private String legendName;
	private String displayName;
	private String legendImgName;
}
