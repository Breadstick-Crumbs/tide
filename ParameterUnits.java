package com.tridel.tems_sensor_service.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tems_parameter_units", uniqueConstraints = @UniqueConstraint(columnNames = {"sensor_param_id", "unit_id"}))
public class ParameterUnits {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "param_unit_id",nullable = false)
	private Integer paramUnitId;

	@ManyToOne
	@JoinColumn(name="unit_id", referencedColumnName="unit_id")
	private Units unitId;

	@ManyToOne
	@JoinColumn(name="sensor_param_id", referencedColumnName="sensor_param_id")
	private SensorParams sensorParams;

	@Column(name="operation")
	private String operation;

	@Column(name="calculated_value",precision = 18, scale = 9)
	private BigDecimal calculatedValue;

}
