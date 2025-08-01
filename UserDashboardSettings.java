package com.tridel.tems_sensor_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tems_user_dashboard_settings", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "sensor"}))
public class UserDashboardSettings {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "user_dashboard_id",nullable = false)
	private Integer userDashboardId;

	@Column(name="user_id")
	private Integer userId;

	@ManyToOne
	@JoinColumn(name="sensor_id", referencedColumnName="sensor_id")
	private Sensors sensor;

	@Column(name="sensor_order_seq")
	private Integer sensorOrderSeq;

}
