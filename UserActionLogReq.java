package com.tridel.tems_sensor_service.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActionLogReq {
	private Integer userActionId;

	private Integer userId;

	private String actionRemarks;

	private Date created;

	private String userAction;

	private String actionModule;

	private String reqDetails;
}

