package com.tridel.tems_sensor_service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionLogFilter {

    private String loggedIn;
    private String fromDate;
    private String toDate;
    private List<Integer> users;

}
