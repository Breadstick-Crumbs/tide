package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.UserDashboardSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserDashboardSettingRepository extends JpaRepository<UserDashboardSettings, Integer> {
    List<UserDashboardSettings> findAllByUserIdOrderBySensorOrderSeq(Integer userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserDashboardSettings WHERE sensor.sensorCode =:sensorCode AND userId =:user")
    void deleteSensorDashboardSettings(String sensorCode, Integer user);

    @Query("SELECT uds FROM UserDashboardSettings uds WHERE uds.sensor.sensorCode =:sensorCode AND userId =:user")
    UserDashboardSettings findBySensorCodeAndUserId(String sensorCode, Integer user);

}
