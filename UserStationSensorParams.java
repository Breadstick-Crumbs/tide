package com.tridel.tems_sensor_service.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tems_user_station_sensor_params")
public class UserStationSensorParams {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "user_station_param_id",nullable = false)
	private Integer userStationParamId;
	
	@ManyToOne
	@JoinColumn(name="station_param_id", referencedColumnName="station_param_id")
	private StationSensorParams stationParamId;

	@Column(name="user_id")
	private Integer user;


}
