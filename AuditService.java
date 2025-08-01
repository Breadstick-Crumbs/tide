package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.ActionLogFilter;
import com.tridel.tems_sensor_service.model.response.UserLog;

import java.util.List;

public interface AuditService {
    List<UserLog> getAllUserAction(ActionLogFilter request);
}
