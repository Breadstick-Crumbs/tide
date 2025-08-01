package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.SensorParamsView;
import com.tridel.tems_sensor_service.model.response.SensorParamView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SensorParamViewRepository extends JpaRepository<SensorParamsView, Integer> {
    @Query("""
            select new com.tridel.tems_sensor_service.model.response.SensorParamView (parameterId, paramName, warn, danger,
             CASE WHEN unitSymbol!='' THEN unitSymbol ELSE '' END, sensorCode,
             CASE WHEN notifyFlag IS NOT NULL THEN notifyFlag ELSE false END,
             CASE WHEN parameterDisplayName IS NOT NULL AND parameterDisplayName!='' THEN parameterDisplayName ELSE paramName END,displayRoundTo,dataParamName)
            from SensorParamsView where parameterId in (select stationParamId.sensorParamId.id from UserStationSensorParams  
            where user = :user and stationParamId.sensorParamId.sensorId.sensorId= :sensorId) order by sensorParamSeq""")
    List<SensorParamView> findAllParamsBySensor(Integer sensorId,Integer user);

    @Query("""
            select new com.tridel.tems_sensor_service.model.response.SensorParamView (parameterId, paramName, warn, danger,
             CASE WHEN unitSymbol!='' THEN unitSymbol ELSE '' END, sensorCode,
             CASE WHEN notifyFlag IS NOT NULL THEN notifyFlag ELSE false END,
             CASE WHEN parameterDisplayName IS NOT NULL AND parameterDisplayName!='' THEN parameterDisplayName ELSE paramName END,displayRoundTo,dataParamName)
            from SensorParamsView where paramName in (select stationParamId.sensorParamId.paramName from UserStationSensorParams where where user = :user and stationParamId.sensorParamId.paramName in :paramName)""")
    SensorParamView findAllByParamName(String paramName,Integer user);

    @Query("""
            select new com.tridel.tems_sensor_service.model.response.SensorParamView(parameterId, CASE WHEN parameterDisplayName IS NOT NULL AND parameterDisplayName!=''
             THEN parameterDisplayName ELSE paramName END) from SensorParamsView where parameterId in
              (select stationParamId.sensorParamId.id from UserStationSensorParams  where user = :user and stationParamId.sensorParamId.sensorId.sensorCode= :sensorCode and stationParamId.stationData.id = :station)""")
    List<SensorParamView> findAllParametersByStationAndSensor(Integer station, String sensorCode,Integer user);
    /*@Query("""
            select new com.tridel.tems_sensor_service.model.response.SensorParamView(parameterId, CASE WHEN parameterDisplayName IS NOT NULL AND parameterDisplayName!=''
             THEN parameterDisplayName ELSE paramName END) from SensorParamsView where parameterId in
              (select stationParamId.sensorParamId.id from UserStationSensorParams  where user = :user and stationParamId.sensorParamId.sensorId.sensorCode= :sensorCode)""")
    List<SensorParamView> findAllParametersByUserAndSensor(String sensorCode,Integer user);*/
    @Query(""" 
            select new com.tridel.tems_sensor_service.model.response.SensorParamView(parameterId,
            CASE WHEN parameterDisplayName IS NOT NULL AND parameterDisplayName!='' THEN parameterDisplayName ELSE paramName END) from SensorParamsView
             where parameterId in (19,20,21,22,23,27) and parameterId in
              (select stationParamId.sensorParamId.id from UserStationSensorParams where user = :user)""")
    List<SensorParamView>findAllMETDataParams(Integer user);
    @Query("""
            select new com.tridel.tems_sensor_service.model.response.SensorParamView(parameterId, CASE WHEN spv.parameterDisplayName IS NOT NULL AND spv.parameterDisplayName!='' THEN spv.parameterDisplayName ELSE spv.paramName END, dataParamName,min,max,warn,danger,
            displayRoundTo,unitSymbol,sensorTableName,sensorStatusTableName,dangerOperation,warnOperation, notifyFlag) 
            from SensorParamsView spv where appParamEnabled is true and parameterId in
              (select stationParamId.sensorParamId.id from UserStationSensorParams where user = :user and stationParamId.stationData.id = :station) and parameterId = :paramId""")
    SensorParamView findParameterDtlsById(Integer station, Integer user,Integer paramId);

    @Query("""
            select new com.tridel.tems_sensor_service.model.response.SensorParamView(parameterId, paramName, CASE WHEN parameterDisplayName IS NOT NULL AND parameterDisplayName!=''
             THEN parameterDisplayName ELSE paramName END) from SensorParamsView where parameterId in
              (select stationParamId.sensorParamId.id from UserStationSensorParams where user = :user and stationParamId.sensorParamId.sensorId.sensorCode= :sensorCode and stationParamId.stationData.id = :station)""")
    List<SensorParamView> findAllParametersByStationAndSensorForStat(Integer station, String sensorCode,Integer user);
    @Query(""" 
            select new com.tridel.tems_sensor_service.model.response.SensorParamView(parameterId, paramName,
            CASE WHEN parameterDisplayName IS NOT NULL AND parameterDisplayName!='' THEN parameterDisplayName ELSE paramName END) from SensorParamsView where parameterId in (19,20,21,22,23,27) and parameterId in
              (select stationParamId.sensorParamId.id from UserStationSensorParams where user = :user)""")
    List<SensorParamView> findAllMETDataParamsForStat(Integer user);
    @Query("""
                select new com.tridel.tems_sensor_service.model.response.SensorParamView(parameterId,CASE WHEN spv.parameterDisplayName IS NOT NULL AND spv.parameterDisplayName!='' THEN spv.parameterDisplayName ELSE spv.paramName END,dataParamName) from 
                SensorParamsView spv where sensorId is null
            """)
    List<SensorParamView> findDtlsOfParamList();
    @Query("""
            select new com.tridel.tems_sensor_service.model.response.SensorParamView(parameterId, CASE WHEN spv.parameterDisplayName IS NOT NULL AND spv.parameterDisplayName!='' THEN spv.parameterDisplayName ELSE spv.paramName END, dataParamName,min,max,warn,danger,
            displayRoundTo,unitSymbol,sensorId,sensorCode,sensorTableName,sensorStatusTableName,sensorName,notifyFlag,sensorOrder)
            from SensorParamsView spv where paramName in (select stationParamId.sensorParamId.paramName from UserStationSensorParams where where user = :user and stationParamId.sensorParamId.paramName in :paramName)""")
    List<SensorParamView> findAllDtlsOfParamList(List<String> paramName,Integer user);
    @Query("""
            select new com.tridel.tems_sensor_service.model.response.SensorParamView(parameterId, CASE WHEN spv.parameterDisplayName IS NOT NULL AND spv.parameterDisplayName!='' THEN spv.parameterDisplayName ELSE spv.paramName END, dataParamName,min,max,warn,danger,
            displayRoundTo,unitSymbol,sensorId,sensorCode,sensorTableName,sensorStatusTableName,sensorName,notifyFlag,sensorOrder)
            from SensorParamsView spv where paramName in (select stationParamId.sensorParamId.paramName from UserStationSensorParams where where user = :user and stationParamId.sensorParamId.paramName in :paramName and stationParamId.sensorParamId.sensorId.sensorCode= :sensorCode)""")
    List<SensorParamView> findAllDtlsOfParamListAndSensor(List<String> paramName,Integer user,String sensorCode);
    @Query("""
                select new com.tridel.tems_sensor_service.model.response.SensorParamView(spv.parameterId, CASE WHEN spv.parameterDisplayName IS NOT NULL AND spv.parameterDisplayName!='' THEN spv.parameterDisplayName ELSE spv.paramName END,spv.dataParamName,spv.min,spv.max,spv.warn,spv.danger,
                spv.displayRoundTo,spv.unitSymbol,spv.sensorId,spv.sensorCode,spv.sensorName,sensorTableName,sensorStatusTableName,ups.userHomeSettingsId.sensorOrderSeq,ups.paramOrderSeq,dangerOperation,warnOperation) 
                from SensorParamsView spv inner join UserStationSensorParams ussp on spv.parameterId = ussp.stationParamId.sensorParamId.id 
                inner join UserHomeParameterSettings ups on ussp.stationParamId.sensorParamId = ups.sensorParams where spv.appParamEnabled is true 
                and ups.userHomeSettingsId.userId =:user and spv.sensorCode is not null and ussp.user = :user and ussp.stationParamId.stationData.id = :station""")
    List<SensorParamView> findAllParametersByStationForHome(Integer station, Integer user);
            @Query("""
                    select new com.tridel.tems_sensor_service.model.response.SensorParamView(spv.parameterId, CASE WHEN spv.parameterDisplayName IS NOT NULL AND spv.parameterDisplayName!='' THEN spv.parameterDisplayName ELSE spv.paramName END,spv.dataParamName,spv.min,spv.max,spv.warn,spv.danger,
                    spv.displayRoundTo,spv.unitSymbol,spv.sensorId,spv.sensorCode,spv.sensorName,sensorTableName,sensorStatusTableName,ups.userDashboardSettingsId.sensorOrderSeq,ups.paramOrderSeq,dangerOperation,warnOperation) 
                    from SensorParamsView spv inner join UserStationSensorParams ussp on spv.parameterId = ussp.stationParamId.sensorParamId.id 
                    inner join UserDashboardParameterSettings ups on ussp.stationParamId.sensorParamId = ups.sensorParams where spv.appParamEnabled is true 
                    and ups.userDashboardSettingsId.userId =:user and spv.sensorCode is not null and ussp.user = :user and ussp.stationParamId.stationData.id = :station""")
            List<SensorParamView> findAllParametersByStationForDashboard(Integer station, Integer user);

    @Query("""
                select new com.tridel.tems_sensor_service.model.response.SensorParamView(spv.parameterId, CASE WHEN spv.parameterDisplayName IS NOT NULL AND spv.parameterDisplayName!='' THEN spv.parameterDisplayName ELSE spv.paramName END,spv.dataParamName,spv.min,spv.max,spv.warn,spv.danger,
                spv.displayRoundTo,spv.unitSymbol,spv.sensorId,spv.sensorCode,spv.sensorName,sensorTableName,sensorStatusTableName,ups.userDashboardSettingsId.sensorOrderSeq,ups.paramOrderSeq,dangerOperation,warnOperation) 
                from SensorParamsView spv inner join UserStationSensorParams ussp on spv.parameterId = ussp.stationParamId.sensorParamId.id 
                inner join UserDashboardParameterSettings ups on ussp.stationParamId.sensorParamId = ups.sensorParams where spv.appParamEnabled is true 
                and spv.sensorCode is not null and ussp.user = :user and ussp.stationParamId.stationData.id = :station
                 and ups.userDashboardSettingsId.userId =:user and ussp.stationParamId.sensorParamId.sensorId.sensorCode= :sensorCode""")
    List<SensorParamView> findAllParametersByStationAndSensorForDashboard(Integer station, String sensorCode,Integer user);

    @Query("""
                select new com.tridel.tems_sensor_service.model.response.SensorParamView(spv.parameterId, CASE WHEN spv.parameterDisplayName IS NOT NULL AND spv.parameterDisplayName!='' THEN spv.parameterDisplayName ELSE spv.paramName END,spv.dataParamName,spv.min,spv.max,spv.warn,spv.danger,
                spv.displayRoundTo,spv.unitSymbol,spv.sensorId,spv.sensorCode,spv.sensorName,sensorTableName,sensorStatusTableName,ups.userHomeSettingsId.sensorOrderSeq,ups.paramOrderSeq,dangerOperation,warnOperation) 
                from SensorParamsView spv inner join UserStationSensorParams ussp on spv.parameterId = ussp.stationParamId.sensorParamId.id 
                inner join UserHomeParameterSettings ups on ussp.stationParamId.sensorParamId = ups.sensorParams where spv.appParamEnabled is true 
                and ups.userHomeSettingsId.userId =:user and spv.sensorCode is not null and ussp.user = :user and ussp.stationParamId.stationData.id = :station and ussp.stationParamId.sensorParamId.sensorId.sensorCode= :sensorCode""")
    List<SensorParamView> findAllParametersByStationAndSensorForHome(Integer station, String sensorCode,Integer user);

    @Query("""
            select new com.tridel.tems_sensor_service.model.response.SensorParamView(parameterId, CASE WHEN spv.parameterDisplayName IS NOT NULL AND spv.parameterDisplayName!='' THEN spv.parameterDisplayName ELSE spv.paramName END, dataParamName,min,max,warn,danger,
            displayRoundTo,unitSymbol,sensorTableName,sensorStatusTableName,dangerOperation,warnOperation, notifyFlag) from SensorParamsView spv where appParamEnabled is true and parameterId in
              (select stationParamId.sensorParamId.id from UserStationSensorParams  where user = :user and stationParamId.sensorParamId.sensorId.sensorCode= :sensorCode and stationParamId.stationData.id IN (:station))""")
    List<SensorParamView> findAllParametersByStationAndSensorForReport(List<Integer> station, String sensorCode,Integer user);

    @Query(""" 
            select new com.tridel.tems_sensor_service.model.response.SensorParamView(parameterId, CASE WHEN spv.parameterDisplayName IS NOT NULL AND spv.parameterDisplayName!='' THEN spv.parameterDisplayName ELSE spv.paramName END, dataParamName,min,max,warn,danger,
            displayRoundTo,unitSymbol,sensorTableName,sensorStatusTableName,dangerOperation,warnOperation, notifyFlag) from SensorParamsView spv
             where parameterId in (19,20,21,22,23,27,90) and parameterId in
              (select stationParamId.sensorParamId.id from UserStationSensorParams where user = :user)""")
    List<SensorParamView> findAllMETDataParamsForSummary(Integer user);

    @Query(""" 
            select new com.tridel.tems_sensor_service.model.response.SensorParamView(displayRoundTo, spv.paramName, dataParamName) from SensorParamsView spv 
             where parameterId in (19,20,22,23,27,90,113) and parameterId in
              (select stationParamId.sensorParamId.id from UserStationSensorParams where user = :user)""")
    List<SensorParamView> findAllMETDataParamsForHome(Integer user);
    @Query("select new com.tridel.tems_sensor_service.model.response.SensorParamView(parameterId, CASE WHEN spv.parameterDisplayName IS NOT NULL AND spv.parameterDisplayName!='' THEN spv.parameterDisplayName ELSE spv.paramName END, dataParamName,min,max,warn,danger," +
            "displayRoundTo,unitSymbol,sensorTableName,sensorStatusTableName,dangerOperation,warnOperation, notifyFlag)" +
            " from SensorParamsView spv where appParamEnabled is true and parameterId = :parameterId")
    SensorParamView findByParameterId(Integer parameterId);
    @Query("select new com.tridel.tems_sensor_service.model.response.SensorParamView(parameterId, CASE WHEN spv.parameterDisplayName IS NOT NULL AND spv.parameterDisplayName!='' THEN spv.parameterDisplayName ELSE spv.paramName END, dataParamName,min,max,warn,danger," +
            "displayRoundTo,unitSymbol,sensorId,sensorCode,sensorTableName,sensorStatusTableName,sensorName,notifyFlag,sensorOrder)" +
            " from SensorParamsView spv where appParamEnabled is true and paramName = :paramName")
    SensorParamView findByParameterName(String paramName);
}
