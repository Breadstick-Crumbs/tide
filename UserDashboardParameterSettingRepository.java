package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.UserDashboardParameterSettings;
import com.tridel.tems_sensor_service.entity.UserDashboardSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserDashboardParameterSettingRepository extends JpaRepository<UserDashboardParameterSettings, Integer> {

    @Query("""
            SELECT udps FROM UserDashboardParameterSettings udps where userDashboardSettingsId =:userDashboardSettings
             AND sensorParams IN (SELECT stationParamId.sensorParamId FROM UserStationSensorParams WHERE user=:user)
              ORDER BY paramOrderSeq
            """)
    List<UserDashboardParameterSettings> findAllByUserDashboardSettingsIdOrderByParamOrderSeq(UserDashboardSettings userDashboardSettings, Integer user);

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM UserDashboardParameterSettings  WHERE userDashboardSettingsId in
     (SELECT uds FROM UserDashboardSettings uds where
      sensor.sensorCode =:sensorCode AND userId =:user)
    """)
    void deleteAllSensorParamDashboardSettings(String sensorCode, Integer user);

    @Query("""
            SELECT udps FROM UserDashboardParameterSettings udps where userDashboardSettingsId.sensor.sensorCode =:sensorCode
             AND userDashboardSettingsId.userId=:user
            """)
    List<UserDashboardParameterSettings> findAllBySensorAndUser(String sensorCode, Integer user);
}
