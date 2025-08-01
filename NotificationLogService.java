package com.tridel.tems_sensor_service.service;


import com.tridel.tems_sensor_service.model.request.NotificationStatusReq;
import com.tridel.tems_sensor_service.model.request.NotificationsReq;
import com.tridel.tems_sensor_service.model.response.NotificationTypeResp;
import com.tridel.tems_sensor_service.model.response.SensorNotificationResponse;

import java.util.List;

public interface NotificationLogService {
    List<SensorNotificationResponse> getAllUnreadNotification(List<Integer> stationIds);

    List<NotificationTypeResp>  getAllNotificationType();

    List<SensorNotificationResponse> getAllAlarmNotify(NotificationsReq request);

    List<SensorNotificationResponse> loadStationNotification(NotificationsReq request);

    String updateNotificationStatus(NotificationStatusReq request);
}
