package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.NotificationDetails;
import com.tridel.tems_sensor_service.model.request.NotificationRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationDetailsRepository extends JpaRepository<NotificationDetails, Integer> {
    @Query(""" 
            select new com.tridel.tems_sensor_service.model.request.NotificationRecipient(nr.notificationDetailId,nr.stationData.stationName,
            nr.sensor.sensorCode,nr.sensor.sensorName,notificationType,notificationModeId,notificationModeName
            ,name,email) from NotificationDetails nr where nr.stationData.id= :stationId and nr.sensor.sensorId = :sensorId and notificationType IN( :notificationType)
            """)
    List<NotificationRecipient> findAllByStationAndSensor(Integer stationId,Integer sensorId,List<String> notificationType);
    @Query(""" 
            select new com.tridel.tems_sensor_service.model.request.NotificationRecipient
            (nr.notificationDetailId,nr.stationData.stationName,
            notificationType,notificationModeId,notificationModeName,name,email)
             from NotificationDetails nr where nr.stationData.id= :stationId and sensor is null
            """)
    List<NotificationRecipient> findAllGeneral(Integer stationId);

    @Query(""" 
            select notificationDetailId from NotificationDetails nr where nr.stationData.id= :stationId and nr.sensor.sensorId = :sensorId
            and nr.notificationType in (:type) and nr.name = :name and nr.email = :email
            """)
    List<Integer> findAllByStationAndSensorAndTypeAndNameAndEmail(Integer stationId,Integer sensorId,List<String> type,String name,String email);

    @Query("""
            SELECT nr.email FROM NotificationDetails nr WHERE nr.stationData.id =:stationId AND
             nr.sensor.sensorId =:sensorId AND nr.notificationType=:notifyTypeId
            """)
    List<String> findAllMailIdByStationSensor(Integer stationId, Integer sensorId, Integer notifyTypeId);
}
