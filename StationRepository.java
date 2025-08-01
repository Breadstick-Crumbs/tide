package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.StationData;
import com.tridel.tems_sensor_service.model.response.StationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<StationData,Integer> {
    @Query("""
            select distinct new com.tridel.tems_sensor_service.model.response.StationResponse(stationParamId.stationData.id, stationParamId.stationData.stationCode,
             stationParamId.stationData.stationName,stationParamId.stationData.stationImg) from UserStationSensorParams where user =:user
            """)
    List<StationResponse> findAllByStationUser(Integer user);

    /*@Query("""
            select distinct new com.tridel.tems_sensor_service.model.response.StationResponse(stationParamId.stationData.id, stationParamId.stationData.stationCode, stationParamId.stationData.stationName)
            from UserStationSensorParams where user =:user and stationParamId.stationData.stationType =:stType
            """)
    List<StationResponse> findAllStationByUserAndType(Integer user, Integer stType);*/
    @Query("""
            select distinct new com.tridel.tems_sensor_service.model.response.StationResponse(stationId.id, stationId.stationCode, stationId.stationName)
            from UserStations where user =:user and stationId.stationType.stationTypeId =:stType and displayInAnalytics is true
            """)
    List<StationResponse> findAllStationByUserAndType(Integer user, Integer stType);

    @Query("""
            select distinct stationParamId.stationData.id from UserStationSensorParams where user =:user
            """)
    List<Integer> findAllStationForUser(Integer user);
    @Query("""
     select stationId from UserStations us where user = :user and displayInhome is true""")
    List<StationData> findAllStationsByUserForHome(Integer user);
    @Query("""
     select stationId from UserStations us where user = :user and displayInAnalytics is true""")
    List<StationData> findAllStationsByUserForSummary(Integer user);

    @Query("""
            select distinct new com.tridel.tems_sensor_service.model.response.StationResponse(stationId.id, stationId.stationCode, stationId.stationName)
            from UserStations where user =:user and stationId.id in :stationIds
            """)
    List<StationResponse> findStationDtlsById(Integer user, List<Integer> stationIds);
    @Query("""
            select distinct new com.tridel.tems_sensor_service.model.response.StationResponse(stationParamId.stationData.id, stationParamId.stationData.stationCode, stationParamId.stationData.stationName)
            from UserStationSensorParams where user =:user and stationParamId.sensorParamId.id = :paramId
            """)
    List<StationResponse> findStationDtlsByUserAndParam(Integer user, Integer paramId);
    @Query("""
            select distinct new com.tridel.tems_sensor_service.model.response.StationResponse(id, stationCode, stationName)
            from StationData where id = :stationId
            """)
    StationResponse findStationById(Integer stationId);

    List<StationData> findAllByIdNotIn(List<Integer> stationId);
}
