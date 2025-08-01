package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.master.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SensorTypeRepository extends JpaRepository<SensorType,Integer> {

}
