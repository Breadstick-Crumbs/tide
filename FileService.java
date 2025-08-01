package com.tridel.tems_sensor_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String saveStationImage(MultipartFile file, String stCode);
}
