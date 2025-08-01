package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.SensorParams;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorParamRepository extends JpaRepository<SensorParams, Integer> {

}
