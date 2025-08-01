package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.model.WindRoseRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface WindRoseRangeRepository extends JpaRepository<com.tridel.tems_sensor_service.entity.WindRoseRange,Integer> {

    @Query("""
            SELECT new com.tridel.tems_sensor_service.model.WindRoseRange(windRoseRangeId,scaleMin,scaleMax) FROM WindRoseRange wr where wr.windroseSettings.parameterId.id =:pmId
            """)
    List<WindRoseRange> findAllWindRoseRangeByParameter(Integer pmId);
}
