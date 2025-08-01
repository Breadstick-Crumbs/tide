package com.tridel.tems_sensor_service.repository;


import com.tridel.tems_sensor_service.entity.UserHomeSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserHomeSettingRepository extends JpaRepository<UserHomeSettings, Integer> {
    List<UserHomeSettings> findAllByUserIdOrderBySensorOrderSeq(Integer userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserHomeSettings WHERE sensor.sensorCode =:sensorCode and userId =:user")
    void deleteSensorHomeSettings(String sensorCode, Integer user);

    @Query("SELECT uds FROM UserHomeSettings uds WHERE uds.sensor.sensorCode =:sensorCode AND userId =:user")
    UserHomeSettings findBySensorCodeAndUserId(String sensorCode, Integer user);
}
