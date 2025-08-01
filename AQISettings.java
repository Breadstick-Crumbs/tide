package com.tridel.tems_sensor_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tems_aqi_settings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AQISettings {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "aqi_settings_id",nullable = false)
	private Integer aqiSettingsId;

	@ManyToOne
	@JoinColumn(name="sensor_param_id", referencedColumnName="sensor_param_id")
	private SensorParams sensorParamId;
	
	@Column(name = "isenabled")
	private Boolean isEnabled;
	
	@Column(name = "avg_interval")
	private int avgInterval;

}
