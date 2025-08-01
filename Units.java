package com.tridel.tems_sensor_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tems_units")
public class Units {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "unit_id",nullable = false)
	private Integer unitId;
	
	@Column(name="unit_name")
	private String unitName;
	
	@Column(name="unit_abbr")
	private String unitAbbreviation;
	
	@Column(name="unit_symbol")
	private String unitSymbol;
}
