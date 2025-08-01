package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.master.StationLegends;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationLegendRepository extends JpaRepository<StationLegends,Integer> {

    StationLegends findByLegendName(String maintenace);
}
