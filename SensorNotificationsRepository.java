package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.log.SensorNotifications;
import com.tridel.tems_sensor_service.model.response.SensorNotificationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface SensorNotificationsRepository extends JpaRepository<SensorNotifications,Integer> {
    @Query("select new com.tridel.tems_sensor_service.model.response.SensorNotificationResponse(notifyId, station.id, " +
            " stationName, notificationType.notifyTypeName, notificationDatetime, notificationMsg) from SensorNotifications" +
            " where unread is TRUE and CAST(notificationDatetime AS DATE)=CAST(GETDATE() AS DATE)and station.id IN (:stationIds)")
    List<SensorNotificationResponse> findAllUnreadNotificationsByStations(List<Integer> stationIds);
    @Query("select new com.tridel.tems_sensor_service.model.response.SensorNotificationResponse(notifyId, station.id, " +
            "  stationName, notificationType.notifyTypeName as type, notificationDatetime as dateTime, notificationMsg as message) from SensorNotifications" +
            " where notificationDatetime between :fromDate and :toDate AND station.id in :stationIds")
    List<SensorNotificationResponse> findAllByNotifyDateTimeBetweenOrderByCreatedAtDesc(Date fromDate, Date toDate, List<Integer> stationIds);

    @Modifying
    @Transactional
    @Query("update SensorNotifications set unread = FALSE where notificationDatetime between :fromDate and :toDate")
    void updateAllNotificationsBetweenNotifyDateTime(Date fromDate, Date toDate);
}
