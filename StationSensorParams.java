package com.tridel.tems_sensor_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tems_station_sensor_params", uniqueConstraints = @UniqueConstraint(columnNames = {"sensor_param_id", "station_id"}))
public class StationSensorParams {
	
	@Id@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "station_param_id",nullable = false)
	private Integer stationParamId;
	
	@ManyToOne
	@JoinColumn(name="sensor_param_id", referencedColumnName="sensor_param_id")
	private SensorParams sensorParamId;
	
	@ManyToOne
	@JoinColumn(name="station_id", referencedColumnName="station_id")
	private StationData stationData;
}
