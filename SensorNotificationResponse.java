package com.tridel.tems_sensor_service.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class SensorNotificationResponse {

	private Integer notifyId;
	private Integer stationId;
	private String stationName;
	private String type;
	private Date dateTime;
	private String message;

	public SensorNotificationResponse(Integer notifyId, Integer stationId, String stationName, String type, Date dateTime, String message) {
		this.notifyId = notifyId;
		this.stationId = stationId;
		this.stationName = stationName;
		this.type = type;
		this.dateTime = dateTime;
		this.message = message;
	}

	public SensorNotificationResponse() {
		super();
	}
}
