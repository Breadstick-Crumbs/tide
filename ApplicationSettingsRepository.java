package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.master.ApplicationSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationSettingsRepository extends JpaRepository<ApplicationSettings, Integer> {

}
