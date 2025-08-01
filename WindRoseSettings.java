package com.tridel.tems_sensor_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tems_windrose_settings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WindRoseSettings {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "windrose_settings_id",nullable = false)
	private Integer windRoseSettingsId;
	
	@ManyToOne
	@JoinColumn(name="sensor_parameter_id", referencedColumnName="sensor_param_id")
	private SensorParams parameterId;
	
	@ManyToOne
	@JoinColumn(name="sensor_sec_parameter_id", referencedColumnName="sensor_param_id")
	private SensorParams parameterSecondId;

}
