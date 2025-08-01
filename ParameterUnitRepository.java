package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.ParameterUnits;
import com.tridel.tems_sensor_service.entity.SensorParams;
import com.tridel.tems_sensor_service.entity.Units;
import com.tridel.tems_sensor_service.model.response.ParameterUnitPojo;
import com.tridel.tems_sensor_service.model.response.UnitResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParameterUnitRepository extends JpaRepository<ParameterUnits, Integer> {

    ParameterUnits findBySensorParamsAndUnitId(SensorParams parameter, Units units);
    @Query("select pu from ParameterUnits pu where pu.sensorParams.id=:paramId")
    List<ParameterUnits> findByParameterIdFromView(Integer paramId);

    @Query("select new com.tridel.tems_sensor_service.model.response.UnitResponse(unitId.unitId, unitId.unitSymbol) from ParameterUnits where sensorParams.id=:paramId")
    List<UnitResponse> findUnitByParameterId(Integer paramId);

    @Query("select new com.tridel.tems_sensor_service.model.response.ParameterUnitPojo(unitId.unitId, unitId.unitSymbol, operation, calculatedValue ) from ParameterUnits where sensorParams.id=:paramId")
    List<ParameterUnitPojo> findAllByParameterId(Integer paramId);

}
