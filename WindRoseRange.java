package com.tridel.tems_sensor_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tems_windrose_range")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WindRoseRange {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "windrose_range_id",nullable = false)
	private Integer windRoseRangeId;

	@Column(name = "color_code")
	private String colorCode;
	
	@Column(name = "scale_min")
	private Double scaleMin;
	
	@Column(name = "scale_max")
	private Double scaleMax;
	
	@ManyToOne
	@JoinColumn(name="windrose_settings_id", referencedColumnName="windrose_settings_id")
	private WindRoseSettings windroseSettings;

}
