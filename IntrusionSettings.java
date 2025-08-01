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
@Table(name="tems_intrusion_settings")
public class IntrusionSettings {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "settings_id",nullable = false)
	private int settingsId;
	
	@Column(name = "intrusion_value")
	private Double intrusionValue;
	
	@Column(name="created_datetime")
	private Date created;
	  
	@Column(name="updated_datetime")
	private Date updated;

	@ManyToOne
	@JoinColumn(name="sensor_param_id", referencedColumnName="sensor_param_id")
	private SensorParams parameterId;

}
