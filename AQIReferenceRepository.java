package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.master.AQIReference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AQIReferenceRepository extends JpaRepository<AQIReference, Integer> {

}
