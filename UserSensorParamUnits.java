package com.tridel.tems_sensor_service.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tems_user_sensorparam_units")
public class UserSensorParamUnits {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "user_sensor_id",nullable = false)
	private Integer userSensorId;
	
	@ManyToOne
	@JoinColumn(name="sensor_param_id", referencedColumnName="sensor_param_id")
	private SensorParams sensorParamId;
	@ManyToOne
	@JoinColumn(name="param_unit_id", referencedColumnName="param_unit_id")
	private ParameterUnits paramUnitId;

	@Column(name = "graph_yaxis_min")
	private Double graphYAxisMin;

	@Column(name="user_id")
	private Integer user;

}
