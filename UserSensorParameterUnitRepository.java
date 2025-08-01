package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.UserSensorParamUnits;
import com.tridel.tems_sensor_service.model.UserSensorParamUnitsPojo;
import com.tridel.tems_sensor_service.model.response.UserSensorParamUnitsResponse;
import com.tridel.tems_sensor_service.model.response.usersettings.SensorParamResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSensorParameterUnitRepository extends JpaRepository<UserSensorParamUnits, Integer> {

    @Query("""
             select upu from UserSensorParamUnits upu where
             user=:userId and sensorParamId.id=:parameter
             """)
    UserSensorParamUnits findAllByUserAndParam(Integer userId, Integer parameter);
    
    @Query("""
          select new com.tridel.tems_sensor_service.model.response.UserSensorParamUnitsResponse(
          id, parameterDisplayName,paramName,
          (select upu.paramUnitId.unitId.unitSymbol from UserSensorParamUnits upu where upu.user=:user and upu.sensorParamId.id=parameterId),
          (select upu.graphYAxisMin from UserSensorParamUnits upu where upu.user=:user and upu.sensorParamId.id=parameterId))
          from SensorParamsView where parameterId in (select stationParamId.sensorParamId.id from UserStationSensorParams
          where user = :user) and sensorCode= :sensorCode
             """)
    List<UserSensorParamUnitsResponse> findAllByUser(Integer user, String sensorCode);

    @Query("""
             select new com.tridel.tems_sensor_service.model.UserSensorParamUnitsPojo(upu.paramUnitId.operation,
             upu.paramUnitId.calculatedValue,upu.paramUnitId.unitId.unitSymbol,upu.graphYAxisMin)
              from UserSensorParamUnits upu where
             user=:userId and sensorParamId.id=:parameter
             """)
    UserSensorParamUnitsPojo findAllByUserAndParamForHome(Integer userId, Integer parameter);

    @Query("""
            select new com.tridel.tems_sensor_service.model.response.usersettings.SensorParamResponse(id,paramName)
             from SensorParamsView where parameterId in (select stationParamId.sensorParamId.id
              from UserStationSensorParams where user = :user) and sensorCode= :sensorCode
            """)
    List<SensorParamResponse> findAllParamByUser(Integer user, String sensorCode);
}
