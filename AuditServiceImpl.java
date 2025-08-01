package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.model.request.ActionLogFilter;
import com.tridel.tems_sensor_service.model.response.UserLog;
import com.tridel.tems_sensor_service.repository.UserActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.tridel.tems_sensor_service.util.CommonLogUtil.*;

@Service
public class AuditServiceImpl implements AuditService {
    UserActionLogRepository userActionLogRepository;
    @Autowired
    AuditServiceImpl(UserActionLogRepository userActionLogRepository){
        this.userActionLogRepository = userActionLogRepository;
    }
    @Override
    public List<UserLog> getAllUserAction(ActionLogFilter request) {
        if (request.getLoggedIn() == null || request.getLoggedIn().isEmpty()) {
            throw new TemsCustomException(USER_NOT_FOUND_MSG);
        }
        List<UserLog> userLogs;
        try {
            Date fromDate = dateFormat1.parse(request.getFromDate());
            Date toDate = dateFormat1.parse(request.getToDate());
            if (request.getUsers() == null || request.getUsers().isEmpty()) {
                userLogs = userActionLogRepository.findUserLogs(fromDate, toDate);
            } else {
                userLogs = userActionLogRepository.findUserLogsWithUsers(fromDate, toDate, request.getUsers());
            }
            return userLogs;
        } catch (ParseException e) {
            throw new TemsCustomException(INVALID_DATE_FORMAT);
        }

    }
}
