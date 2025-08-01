package com.tridel.tems_sensor_service.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tems_user_stations", uniqueConstraints = @UniqueConstraint(columnNames = {"user", "station_id"}))
public class UserStations {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "user_station_id",nullable = false)
	private Integer userStationId;
	
	@ManyToOne
	@JoinColumn(name="station_id", referencedColumnName="station_id")
	private StationData stationId;

	@Column(name="user_id")
	private Integer user;

	@Column(name="display_in_home")
	private Boolean displayInhome;

	@Column(name="display_in_dashboard")
	private Boolean displayIndashboard;

	@Column(name="display_in_analytics")
	private Boolean displayInAnalytics;

}
