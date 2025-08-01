package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.log.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Integer> {

    NotificationType findByNotifyTypeId(int i);
}
