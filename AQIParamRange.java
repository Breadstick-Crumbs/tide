package com.tridel.tems_sensor_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tems_aqi_param_range")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AQIParamRange {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "aqi_range_id",nullable = false)
	private Integer aqiRangeId;
	
	@ManyToOne
	@JoinColumn(name="aqi_settings_id", referencedColumnName="aqi_settings_id")
	private AQISettings aqiSettings;

	@Column(name = "min_value")
	private Double minValue;
	
	@Column(name = "max_value")
	private Double maxValue;
	
	@Column(name = "index_low")
	private Integer indexLow;
	
	@Column(name = "index_high")
	private Integer indexHigh;
	
	@Column(name = "concen_low")
	private Double concentrationLow;
	
	@Column(name = "concen_high")
	private Double concentrationHigh;

}
