package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.master.SensorRange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorRangeRepository extends JpaRepository<SensorRange, Integer> {
    Optional<SensorRange> findBySensorParameterId(Integer id);
}
