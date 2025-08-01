package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.UserStationSensorParams;
import com.tridel.tems_sensor_service.model.SensorParamViewReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserStationSensorParamRepository extends JpaRepository<UserStationSensorParams, Integer> {

//    @Query("""
//            SELECT ussp FROM UserStationSensorParams ussp WHERE user=:userId
//            """)
    List<UserStationSensorParams> findAllByUser(Integer userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserStationSensorParams e WHERE e.user = :userId")
    void deleteAllUser(Integer userId);

    @Query("""
            SELECT DISTINCT new com.tridel.tems_sensor_service.model.SensorParamViewReq(
            usp.stationParamId.stationData.id, usp.stationParamId.sensorParamId.sensorId.sensorCode,
            usp.stationParamId.sensorParamId.sensorId.sensorTableName) FROM UserStationSensorParams usp
             WHERE usp.stationParamId.stationData.id=:id AND user=:loggedIn
            """)
    List<SensorParamViewReq> findAllSensorByStation(Integer id, Integer loggedIn);
}
