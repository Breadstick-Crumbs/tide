package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.NotificationRecipient;
import com.tridel.tems_sensor_service.model.request.SensorDataMailRequest;
import com.tridel.tems_sensor_service.model.response.MailDetails;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface NotificationService {
    List<NotificationRecipient> getAllNotificationRecipients(NotificationRecipient notificationRecipient);

    String addNotificationRecipients(NotificationRecipient notificationRecipient, HttpServletRequest httpServletRequest);

    String deleteNotificationRecipients(NotificationRecipient notificationRecipient, HttpServletRequest httpServletRequest);

    MailDetails getMailDetails(SensorDataMailRequest request);
}
