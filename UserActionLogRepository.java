package com.tridel.tems_sensor_service.repository;


import com.tridel.tems_sensor_service.entity.log.UserActionLog;
import com.tridel.tems_sensor_service.model.response.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserActionLogRepository extends JpaRepository<UserActionLog,Integer> {
    @Query("SELECT new com.tridel.tems_sensor_service.model.response.UserLog(u.userAction, u.actionModule, u.created, u.actionRemarks, u.userId) " +
            "FROM UserActionLog u " +
            "WHERE u.created BETWEEN :fromDate AND :toDate " +
            "AND u.userId IN :userIds " +
            "ORDER BY u.created DESC")
    List<UserLog> findUserLogsWithUsers(@Param("fromDate") Date fromDate,
                                        @Param("toDate") Date toDate,
                                        @Param("userIds") List<Integer> userIds);

    @Query("SELECT new com.tridel.tems_sensor_service.model.response.UserLog(u.userAction, u.actionModule, u.created, u.actionRemarks, u.userId) " +
            "FROM UserActionLog u " +
            "WHERE u.created BETWEEN :fromDate AND :toDate " +
            "ORDER BY u.created DESC")
    List<UserLog> findUserLogs(@Param("fromDate") Date fromDate,
                               @Param("toDate") Date toDate);
}
