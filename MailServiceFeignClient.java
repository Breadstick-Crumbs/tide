package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.MailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="email-service")
public interface MailServiceFeignClient {

    @PostMapping("/api/email/send")
    void sendEmail(MailRequest request);
}
