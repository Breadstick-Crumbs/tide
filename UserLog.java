package com.tridel.tems_sensor_service.model.response;

import com.tridel.tems_sensor_service.util.CommonLogUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.tridel.tems_sensor_service.util.CommonLogUtil.dateFormat2;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLog {
    private String userAction;
    private String module;
    private String performedAt;
    private String remarks;
    private Integer userId;



    public UserLog(String userAction, String module, Date performedAt, String remarks,Integer userId) {
        this.userAction = userAction;
        this.module = module;
        this.performedAt = dateFormat2.format(performedAt); // Ensure correct formatting if needed
        this.remarks = remarks;
        this.userId = userId;
    }

}

