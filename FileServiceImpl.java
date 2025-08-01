package com.tridel.tems_sensor_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
@Slf4j
public class FileServiceImpl implements FileService{
    @Value("${station.image.path}")
    private String imgPath;
    @Override
    public String saveStationImage(MultipartFile file, String stCode) {
        String resp = "";
        String fName = file.getOriginalFilename();
        if (fName != null && !fName.isEmpty()) {
            String name = fName.substring(0, fName.lastIndexOf('.'));
            String ext = getFileExtension(file);
            String imgPath1 = stCode + "_" + name + ext;
            Path imgpath = Paths.get(imgPath);
            try {
                Files.copy(file.getInputStream(), imgpath.resolve(imgPath1));
            } catch (Exception e) {
                throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
            }
            resp = imgPath1;
        }
        return resp;
    }

    private String getFileExtension(MultipartFile file) {
        String name = file.getOriginalFilename();
        int lastIndexOf = name != null ? name.lastIndexOf(".") : 0;
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name != null ? name.substring(lastIndexOf) : "";
    }
}
