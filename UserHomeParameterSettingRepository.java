package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.UserHomeParameterSettings;
import com.tridel.tems_sensor_service.entity.UserHomeSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserHomeParameterSettingRepository extends JpaRepository<UserHomeParameterSettings, Integer> {

    @Query("""
            SELECT uhps FROM UserHomeParameterSettings uhps where userHomeSettingsId =:userHomeSettings
             AND sensorParams IN (SELECT stationParamId.sensorParamId FROM UserStationSensorParams WHERE user=:user)
              ORDER BY paramOrderSeq
            """)
    List<UserHomeParameterSettings> findAllByUserHomeSettingsIdOrderByParamOrderSeq(UserHomeSettings userHomeSettings, Integer user);

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM UserHomeParameterSettings  WHERE userHomeSettingsId in
     (SELECT uds FROM UserHomeSettings uds where
      sensor.sensorCode =:sensorCode AND userId =:user)
    """)
    void deleteAllSensorParamHomeSettings(String sensorCode, Integer user);

    @Query("""
            SELECT uhps FROM UserHomeParameterSettings uhps where userHomeSettingsId.sensor.sensorCode =:sensorCode
             AND userHomeSettingsId.userId =:user
            """)
    List<UserHomeParameterSettings> findAllBySensorAndUser(String sensorCode, Integer user);
}
