package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.Sensors;
import com.tridel.tems_sensor_service.model.response.SensorResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensors, Integer> {
    @Query(value = """
            select distinct new com.tridel.tems_sensor_service.model.response.SensorResponse(stationParamId.sensorParamId.sensorId.sensorId,stationParamId.sensorParamId.sensorId.sensorCode,stationParamId.sensorParamId.sensorId.sensorName,stationParamId.sensorParamId.sensorId.sensorOrder)
            from UserStationSensorParams where user =:usr
            """)
    List<SensorResponse> findAllSensorsByUser(Integer usr);

    @Query("""
            select distinct new com.tridel.tems_sensor_service.model.response.SensorResponse(stationParamId.sensorParamId.sensorId.sensorId,stationParamId.sensorParamId.sensorId.sensorCode,stationParamId.sensorParamId.sensorId.sensorName,stationParamId.sensorParamId.sensorId.sensorOrder) from UserStationSensorParams us where
            us.stationParamId.stationData.id=:station and us.user = :usr
            """)
    List<SensorResponse> findAllSensorsByUserAndStation(Integer usr,Integer station);
    @Query("""
            select distinct new com.tridel.tems_sensor_service.model.response.SensorResponse(sensorId,sensorCode,sensorName,sensorOrder) from Sensors where sensorId= :sensorId
            """)
    SensorResponse findDtlsBySensorId(int sensorId);
    Sensors findBySensorCode(String code);

    Sensors findBySensorId(Integer sensor);
}
