package com.tridel.tems_sensor_service.service;


import com.tridel.tems_sensor_service.entity.log.NotificationType;
import com.tridel.tems_sensor_service.entity.log.SensorNotifications;
import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.model.request.NotificationStatusReq;
import com.tridel.tems_sensor_service.model.request.NotificationsReq;
import com.tridel.tems_sensor_service.model.response.NotificationTypeResp;
import com.tridel.tems_sensor_service.model.response.SensorNotificationResponse;
import com.tridel.tems_sensor_service.repository.NotificationTypeRepository;
import com.tridel.tems_sensor_service.repository.SensorNotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import static com.tridel.tems_sensor_service.util.CommonLogUtil.*;

@Service
public class NotificationLogServiceImpl implements NotificationLogService {
    SensorNotificationsRepository snRepo;
    NotificationTypeRepository ntRepo;

    @Autowired
    NotificationLogServiceImpl(SensorNotificationsRepository snRepo, NotificationTypeRepository ntRepo) {
        this.snRepo = snRepo;
        this.ntRepo = ntRepo;
    }

    @Override
    public List<SensorNotificationResponse> getAllUnreadNotification(List<Integer> stationIds) {
        List<SensorNotificationResponse> snList = new ArrayList<>();
        try {
            snList = snRepo.findAllUnreadNotificationsByStations(stationIds);
        } catch (Exception e) {
            throw new TemsCustomException("Exception occurred in getAllNotification api with message: " + e.getMessage());
        }
        return snList;
    }

    @Override
    public List<NotificationTypeResp> getAllNotificationType() {
        List<NotificationTypeResp> response = new ArrayList<>();
        try {
            List<NotificationType> notificationTypes = ntRepo.findAll();
            notificationTypes.forEach(not -> {
                NotificationTypeResp resp = new NotificationTypeResp();
                resp.setNotifyTypeId(String.valueOf(not.getNotifyTypeId()));
                resp.setNotifyTypeName(not.getNotifyTypeName());
                resp.setNotifyTypeCode(not.getNotifyTypeCode());
                response.add(resp);
            });
            response.add(new NotificationTypeResp("1,2", "Both (Warning & Danger)", "BOTH"));

        } catch (Exception e) {
            throw new TemsCustomException("Exception occurred in getAllNotificationType api with message: " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<SensorNotificationResponse> getAllAlarmNotify(NotificationsReq request) {
        List<SensorNotificationResponse> snList;
        try {
            snList = snRepo.findAllByNotifyDateTimeBetweenOrderByCreatedAtDesc(
                    dateFormat3.parse(request.getFromDate().replace("T", " ")),
                    dateFormat3.parse(request.getToDate().replace("T", " ")),
                    request.getStationIds());
        } catch (ParseException e) {
            throw new TemsCustomException("Exception occurred  while parsing dates in getAllAlarmNotify api with message: " + e.getMessage());
        }
        return snList;
    }

    @Override
    public List<SensorNotificationResponse> loadStationNotification(NotificationsReq request) {
        List<SensorNotificationResponse> snList;
        try {
            Calendar calendar = Calendar.getInstance();
            Date currentTime = calendar.getTime();
            calendar.add(Calendar.MINUTE, -1);
            Date oneMinuteBack = calendar.getTime();
            snList = snRepo.findAllByNotifyDateTimeBetweenOrderByCreatedAtDesc(
                    oneMinuteBack,
                    currentTime,
                    request.getStationIds());
        } catch (Exception e) {
            throw new TemsCustomException("Exception occurred  while loading station notification: " + e.getMessage());
        }
        return snList;
    }

    @Override
    public String updateNotificationStatus(NotificationStatusReq request) {
        StringBuilder response = new StringBuilder("false");
        if (request.getType().equalsIgnoreCase("All")) {
            Calendar toDate = Calendar.getInstance();
            toDate.setTime(new Date());

            Calendar fromDate = Calendar.getInstance();
            fromDate.setTime(toDate.getTime());
            fromDate.set(Calendar.HOUR_OF_DAY, 0);
            fromDate.set(Calendar.MINUTE, 0);
            fromDate.set(Calendar.SECOND, 0);
            snRepo.updateAllNotificationsBetweenNotifyDateTime(fromDate.getTime(), toDate.getTime());
            response.setLength(0);
            response.append("true");
            response.trimToSize();
        } else {
            Optional<SensorNotifications> notification = snRepo.findById(request.getId());
            if (notification.isPresent()) {
                notification.get().setUnread(false);
                snRepo.save(notification.get());
            }
            response.setLength(0);
            response.append("true");
            response.trimToSize();
        }

        return response.toString();
    }
}
