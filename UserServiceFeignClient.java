package com.tridel.tems_sensor_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name="tems-user-service")
public interface UserServiceFeignClient {

    @GetMapping("/api/user/userEmail")
    List<String> getUserEmail();
}
