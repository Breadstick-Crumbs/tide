package com.tridel.tems_sensor_service.repository;


import com.tridel.tems_sensor_service.entity.master.SensorCalibrationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorCalibrationRepository extends JpaRepository<SensorCalibrationType,Integer> {

}
