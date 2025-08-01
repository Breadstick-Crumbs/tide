package com.tridel.tems_sensor_service.model.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MailRequest {
    private String[] to;
    private String subject;
    private String body;
}
