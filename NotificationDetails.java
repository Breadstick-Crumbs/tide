package com.tridel.tems_sensor_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tems_notification_details")
public class NotificationDetails {
	
	@Id@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "notification_detail_id",nullable = false)
	private Integer notificationDetailId;
	
	@ManyToOne
	@JoinColumn(name="sensor_id", referencedColumnName="sensor_id")
	private Sensors sensor;
	
	@ManyToOne
	@JoinColumn(name="station_id", referencedColumnName="station_id")
	private StationData stationData;

	@Column(name = "notification_mode_id")
	private Integer notificationModeId;

	@Column(name = "notification_mode_name")
	private String notificationModeName;

	@Column(name = "notification_types")
	private Integer notificationType;

	@Column(name = "name")
	private String name;

	@Column(name = "email")
	private String email;

	@Column(name="created_datetime")
	private Date createdAt;

}
