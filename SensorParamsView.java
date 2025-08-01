package com.tridel.tems_sensor_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name="sensor_param_view")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SensorParamsView {

    @Id
	@Column(name = "sensor_param_id")
	private Integer parameterId;
	
	@Column(name = "sensor_param_name")
	private String paramName;
	
	@Column(name = "sensor_param_value_min")
	private Double min;

	@Column(name = "sensor_param_value_max")
	private Double max;
	
	@Column(name = "sensor_param_value_warn")
	private Double warn;

	@Column(name = "sensor_param_value_danger")
	private Double danger;

	@Column(name = "sensor_param_danger_op")
	private String dangerOperation;

	@Column(name = "sensor_param_warn_op")
	private String warnOperation;

	@Column(name = "sensor_param_sequence")
	private String sensorParamSeq;

	@Column(name = "display_round_to")
	private Integer displayRoundTo;

	@Column(name = "is_app_enabled")
	private Boolean appParamEnabled;

	@Column(name = "param_unit_id")
	private Integer paramUnitId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "unit_symbol")
	private String unitSymbol;

	@Column(name = "sensor_id")
	private Integer sensorId;

	@Column(name = "sensor_code")
	private String sensorCode;

	@Column(name = "sensor_table_name")
	private String sensorTableName;

	@Column(name = "sensor_status_table_name")
	private String sensorStatusTableName;

	@Column(name = "sensor_name")
	private String sensorName;

	@Column(name = "sensor_order")
	private Integer sensorOrder;

	@Column(name = "sensor_param_notify")
	private Boolean notifyFlag;

	/*@Column(name = "graph_yaxis_min")
	private Integer graphYAxisMin;*/

	@Column(name="parameter_display_name")
	private String parameterDisplayName;

	@Column(name = "param_name")
	private String dataParamName;

}
