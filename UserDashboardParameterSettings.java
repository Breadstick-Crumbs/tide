package com.tridel.tems_sensor_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tems_user_dashboard_param_settings")
public class UserDashboardParameterSettings {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "user_dashboard_param_id",nullable = false)
	private Integer userDashboardParamId;

	@ManyToOne
	@JoinColumn(name="user_dashboard_id", referencedColumnName="user_dashboard_id")
	private UserDashboardSettings userDashboardSettingsId;

	@ManyToOne
	@JoinColumn(name="sensor_param_id", referencedColumnName="sensor_param_id")
	private SensorParams sensorParams;

	@Column(name="param_order_seq")
	private Integer paramOrderSeq;

}
