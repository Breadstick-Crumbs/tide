package com.tridel.tems_sensor_service.service;


import com.tridel.tems_sensor_service.exception.TemsBadRequestException;
import com.tridel.tems_sensor_service.model.ParamDtlsPojo;
import com.tridel.tems_sensor_service.model.request.DataMgmtRequest;
import com.tridel.tems_sensor_service.model.request.EditQCRequest;
import com.tridel.tems_sensor_service.model.request.ReportRequest;
import com.tridel.tems_sensor_service.model.response.SensorParamView;
import com.tridel.tems_sensor_service.repository.SensorParamViewRepository;
import com.tridel.tems_sensor_service.repository.StationRepository;
import com.tridel.tems_sensor_service.repository.UserSensorParameterUnitRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.tridel.tems_sensor_service.util.CommonUtil.*;

@Service
@Transactional
@Slf4j
public class DataManagementServiceImpl implements DataManagementService {
    DataServiceCircuitBreakerWrapper dataServiceCircuitBreakerWrapper;
    SensorParamViewRepository sensorParamViewRepository;
    StationRepository stationRepository;
    UserSensorParameterUnitRepository upuRepo;
    CommonService commonService;
    DataManagementServiceImpl(StationRepository stationRepository,
                              SensorParamViewRepository sensorParamViewRepository, UserSensorParameterUnitRepository upuRepo,
                              CommonService commonService,DataServiceCircuitBreakerWrapper dataServiceCircuitBreakerWrapper){
        this.stationRepository = stationRepository;
        this.sensorParamViewRepository = sensorParamViewRepository;
        this.upuRepo = upuRepo;
        this.commonService = commonService;
        this.dataServiceCircuitBreakerWrapper = dataServiceCircuitBreakerWrapper;
    }

    @Override
    public String loadAllQCData(DataMgmtRequest request) {

        if (request.getLoggedIn() == null)
            throw new TemsBadRequestException(USER_NOT_FOUND);
        if (request.getStationId() == null)
            throw new TemsBadRequestException(STN_NOT_FOUND);
        if (request.getSensorCode() == null)
            throw new TemsBadRequestException(SENSOR_CODE);
        if (request.getFromDate().isEmpty() || request.getToDate().isEmpty())
            throw new TemsBadRequestException(DATE_NOT_FOUND);

        List<String>        headerList    = new ArrayList<>();
        List<ParamDtlsPojo> paramDtlsList = new ArrayList<>();
        String              dataObj       = null;

        log.info("loadAllQCData entry ");
        headerList.add(STATIONNAME);
        headerList.add(DATE + " " + TIME);

        ReportRequest req = new ReportRequest();
        req.setStationId (request.getStationId());
        req.setLoggedIn  (request.getLoggedIn());
        req.setSensorCode(request.getSensorCode());
        req.setFromDate  (request.getFromDate());
        req.setToDate    (request.getToDate());

        List<SensorParamView> paramList =
                commonService.getHeaderDataForReports(req,
                        new String[]{request.getStationId().toString()},
                        headerList,
                        paramDtlsList);

        if (!paramList.isEmpty()) {
            req.setParamList      (paramList);
            req.setSensorTableCode(paramList.getFirst().getSensorTableName());

            dataObj = dataServiceCircuitBreakerWrapper.loadAllQCData(req);
            System.out.println(dataObj);
        }

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("header",     headerList);
        jsonObj.put("parameters", paramDtlsList);
        jsonObj.put("data",       dataObj);

        log.info("loadAllQCData exit ");
        return jsonObj.toString();
    }

    @Override
    @Transactional
    public String editQCData(EditQCRequest req) {
        if (req.getLoggedIn()     == null) throw new TemsBadRequestException(USER_NOT_FOUND);
        if (req.getStationId()    == null) throw new TemsBadRequestException(STN_NOT_FOUND);
        if (req.getSensorCode()   == null) throw new TemsBadRequestException(SENSOR_CODE);
        if (req.getParamDatetime()== null) throw new TemsBadRequestException(DATE_NOT_FOUND);

        /* only derive table name when the caller didn't give one explicitly */
        if (req.getSensorTableCode() == null || req.getSensorTableCode().isBlank()) {
            req.setSensorTableCode("sensor_" + req.getSensorCode().toLowerCase());
        }

        return dataServiceCircuitBreakerWrapper.editQCData(req);
    }




}
