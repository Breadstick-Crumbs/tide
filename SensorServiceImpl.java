package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.exception.TemsBadRequestException;
import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.response.SensorResponse;
import com.tridel.tems_sensor_service.repository.SensorRepository;
import com.tridel.tems_sensor_service.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.tridel.tems_sensor_service.util.CommonUtil.USER_NOT_FOUND;

@Service
@Slf4j
public class SensorServiceImpl implements  SensorService{
    SensorRepository sensorRepository;
    SensorServiceImpl(SensorRepository sensorRepository){
        this.sensorRepository = sensorRepository;
    }
    @Override
    public List<SensorResponse> getAllSensorsByUser(Request req) {
        List<SensorResponse> sensorResponseList = new ArrayList<>();
        log.info("getAllSensorsByUser entry ");
        try {
            if (req.getLoggedIn() == null)
                throw new TemsBadRequestException(USER_NOT_FOUND);
            sensorResponseList = sensorRepository.findAllSensorsByUser(req.getLoggedIn());
        }catch(Exception e){
            log.debug("getAllSensorsByUser exception "+e.getMessage());
            if(e instanceof TemsBadRequestException)
                throw e;
            else
                throw new TemsCustomException(e.getMessage());
        }
        sensorResponseList.sort(Comparator.comparingInt(SensorResponse::getSensorOrder));
        log.info("getAllSensorsByUser exit ");
        return sensorResponseList;
    }
    @Override
    public List<SensorResponse> getAllSensorsByStations(Request req) {
        List<SensorResponse> sensorResponseList = new ArrayList<>();
        log.info("getAllSensorsByStations entry ");
        try {
            if (req.getLoggedIn() == null)
                throw new TemsBadRequestException(USER_NOT_FOUND);
            if (!req.getStation().contains(",")) {
                sensorResponseList = sensorRepository.findAllSensorsByUserAndStation(req.getLoggedIn(),Integer.parseInt(req.getStation()));
            }else {
                SensorResponse sensor = sensorRepository.findDtlsBySensorId(CommonUtil.METID);
                sensorResponseList.add(sensor);
            }
        }catch(Exception e){
            log.debug("getAllSensorsByStations exception "+e.getMessage());
            if(e instanceof TemsBadRequestException)
                throw e;
            else
                throw new TemsCustomException(e.getMessage());
        }
        sensorResponseList.sort(Comparator.comparingInt(SensorResponse::getSensorOrder));
        log.info("getAllSensorsByStations exit ");
        return sensorResponseList;
    }
}
