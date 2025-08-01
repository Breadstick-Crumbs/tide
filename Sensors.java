package com.tridel.tems_sensor_service.entity;

import com.tridel.tems_sensor_service.entity.master.SensorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tems_sensor")
public class Sensors {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "sensor_id" ,nullable = false)
	private Integer sensorId;
	
	@Column(name = "sensor_name")
	private String sensorName;
	
	@Column(name = "sensor_code")
	private String sensorCode;

	@Column(name = "sensor_table_code")
	private String sensorTableName;

	@Column(name = "sensor_table_name" )
	private String sensorClassName;
	
	@Column(name = "is_app_enabled")
	private Boolean appSensorEnabled;

	@Column(name = "sensor_sequence")
	private Integer sensorSequence; //comserver

	@Column(name = "sensor_order")
	private Integer sensorOrder;

	@Column(name = "sensor_status_table_name" )
	private String sensorStatusTableName;

	@ManyToOne
	@JoinColumn(name="sensor_type_id", referencedColumnName="sensor_type_id")
//	@Column(name="sensor_type_id")
	private SensorType sensorTypeId;
}
