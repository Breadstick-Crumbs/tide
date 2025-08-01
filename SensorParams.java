package com.tridel.tems_sensor_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tems_sensor_params")
public class SensorParams {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "sensor_param_id",nullable = false)
	private Integer id;

	@Column(name = "sensor_param_name",unique=true,nullable = false)
	private String paramName;

	@Column(name="parameter_display_name")
	private String parameterDisplayName;

	@ManyToOne
	@JoinColumn(name="sensor_id", referencedColumnName="sensor_id")
	private Sensors sensorId;

	@ManyToOne
	@JoinColumn(name="param_unit_id", referencedColumnName="param_unit_id")
	private ParameterUnits paramUnitId;

	@Column(name = "is_app_enabled")
	private Boolean appParamEnabled;

	@Column(name = "tems_active_flag")
	private Boolean temsActiveFlag;

	@Column(name = "display_round_to")
	private Integer displayRoundTo;

	@Column(name = "sensor_param_sequence")
	private Integer sensorParamSequence;

//	@Column(name="param_column_type")
//	private String paramColumnType;

	@Column(name = "sensor_param_value_danger")
	private Double danger;

	@Column(name = "sensor_param_value_warn")
	private Double warn;

	@Column(name = "sensor_param_danger_op")
	private String dangerOperation;

	@Column(name = "sensor_param_warn_op")
	private String warnOperation;

	@Column(name = "sensor_param_value_min")
	private Double min;

	@Column(name = "sensor_param_value_max")
	private Double max;

	@Column(name = "sensor_param_notify")
	private Boolean notifyFlag;

	/*@Column(name = "graph_yaxis_min")
	private Integer graphYAxisMin;*/

	@Column(name="created_at")
	@CreationTimestamp
	private Date created;

	@Column(name="updated_at")
	@UpdateTimestamp
	private Date updated;
}
