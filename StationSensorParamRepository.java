package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.StationSensorParams;
import com.tridel.tems_sensor_service.model.response.usersettings.StationParamResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StationSensorParamRepository extends JpaRepository<StationSensorParams, Integer> {

    @Query("""
            SELECT new com.tridel.tems_sensor_service.model.response.usersettings.StationParamResponse
            (stationParamId, sensorParamId.paramName) FROM StationSensorParams where stationData.id =:stationId
            """)
    List<StationParamResponse> findAllStationParam(Integer stationId);
}
