package com.tridel.tems_sensor_service.service;


import com.tridel.tems_sensor_service.entity.Sensors;
import com.tridel.tems_sensor_service.entity.master.ReportInterval;
import com.tridel.tems_sensor_service.exception.TemsBadRequestException;
import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.model.request.MobileRequest;
import com.tridel.tems_sensor_service.model.request.ReportRequest;
import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.response.MobileReport;
import com.tridel.tems_sensor_service.model.response.ReportIntervalResponse;
import com.tridel.tems_sensor_service.model.response.SensorParamView;
import com.tridel.tems_sensor_service.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.tridel.tems_sensor_service.util.CommonUtil.*;


@Service
@Slf4j
public class MobileServiceImpl implements MobileService{

    StationRepository stationRepo;
    SensorRepository sensorRepo;
    DataServiceCircuitBreakerWrapper circuitBreakerWrapper;
    CommonService commonService;
    SensorParamViewRepository sensorParamViewRepo;
    ReportIntervalRepository reportIntervalRepo;

    MobileServiceImpl(StationRepository stationRepo, DataServiceCircuitBreakerWrapper circuitBreakerWrapper,
                      CommonService commonService, SensorParamViewRepository sensorParamViewRepo,
                      SensorRepository sensorRepo, ReportIntervalRepository reportIntervalRepo){
        this.stationRepo = stationRepo;
        this.circuitBreakerWrapper = circuitBreakerWrapper;
        this.commonService = commonService;
        this.sensorParamViewRepo = sensorParamViewRepo;
        this.sensorRepo = sensorRepo;
        this.reportIntervalRepo = reportIntervalRepo;
    }

    @Override
    public String getStationStatus(Request request) {
        if(request.getLoggedIn() == null)
            throw new TemsBadRequestException(USER_NOT_FOUND);

        List<Integer> stationIds = stationRepo.findAllStationForUser(request.getLoggedIn());
        if(stationIds!=null){
            request.setStationIds(stationIds);
            return circuitBreakerWrapper.getStationStatus(request);
        }
        return null;
    }

    @Override
    public List<MobileReport> loadReports(MobileRequest request) {
        if(request.getLoggedIn() == null)
            throw new TemsBadRequestException(USER_NOT_FOUND);
        if(request.getStation()== null)
            throw new TemsBadRequestException(STN_NOT_FOUND);
        if(request.getSensor() == null)
            throw new TemsBadRequestException(SENSOR_CODE);
        if(request.getFromDate() == null)
            throw new TemsBadRequestException(DATE_NOT_FOUND);
        if(request.getToDate() == null)
            throw new TemsBadRequestException(DATE_NOT_FOUND);
        if(request.getInterval() == null)
            throw new TemsBadRequestException("Interval not found");

        Sensors sensors = sensorRepo.findBySensorId(request.getSensor());
//        ReportInterval interval = commonService.getReportIntervalFromCache().get(request.getInterval());
        Optional<ReportInterval> intervalMap = reportIntervalRepo.findById(Integer.valueOf(request.getInterval()));
        if(intervalMap.isEmpty()) throw new TemsCustomException(INTERVAL_DATA_FETCH_FAIL);
        ReportInterval interval = intervalMap.get();
        List<SensorParamView> paramList = sensorParamViewRepo.findAllParametersByStationAndSensorForReport(Collections.singletonList(request.getStation()), sensors.getSensorCode(), request.getLoggedIn());

        if(!paramList.isEmpty()){
            ReportRequest req = new ReportRequest();
            req.setInterval(interval.getReportBaseIntervalMinutes() == null ? interval.getReportIntervalEN() : interval.getReportBaseIntervalMinutes());
            req.setParamList(paramList);
            req.setSensorCode(sensors.getSensorCode());
            req.setStation(String.valueOf(request.getStation()));
            req.setSensorTableCode(sensors.getSensorTableName());
            req.setStandardTime("LT");
            req.setFromDate(request.getFromDate());
            req.setToDate(request.getToDate());
            req.setSensorTableCode(paramList.getFirst().getSensorTableName());
            if (request.getInterval() != 9) {
                return circuitBreakerWrapper.loadReportsData(req);
            } else {
                req.setMinIntrvl(50);
                return circuitBreakerWrapper.loadReportsMinData(req);
            }
        }
        return new ArrayList<>();
    }
}
