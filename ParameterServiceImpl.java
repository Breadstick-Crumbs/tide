package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.entity.*;
import com.tridel.tems_sensor_service.exception.TemsBadRequestException;
import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.response.SensorParamView;
import com.tridel.tems_sensor_service.model.response.UserSensorParamUnitsResponse;
import com.tridel.tems_sensor_service.model.response.usersettings.SensorParamResponse;
import com.tridel.tems_sensor_service.repository.*;
import com.tridel.tems_sensor_service.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.tridel.tems_sensor_service.util.CommonUtil.*;

@Service
@Slf4j
public class ParameterServiceImpl implements ParameterService{
    SensorRepository sensorRepository;
    SensorParamViewRepository sensorParamViewRepository;
    UserSensorParameterUnitRepository upuRepo;
    ParameterUnitRepository puRepo;
    UnitRepository unitRepository;
    SensorParamRepository sensorParamRepository;
    CommonService commonService;
    @Autowired
    ParameterServiceImpl(SensorRepository sensorRepository,SensorParamViewRepository sensorParamViewRepository,
                        UserSensorParameterUnitRepository upuRepo,ParameterUnitRepository puRepo,
                         UnitRepository unitRepository,SensorParamRepository sensorParamRepository,CommonService commonService){
        this.sensorRepository = sensorRepository;
        this.sensorParamViewRepository = sensorParamViewRepository;
        this.upuRepo = upuRepo;
        this.puRepo = puRepo;
        this.unitRepository = unitRepository;
        this.sensorParamRepository = sensorParamRepository;
        this.commonService = commonService;
    }
    @Override
    public List<SensorParamView> loadParametersByType(Request request) {
        log.info("loadParametersByType entry ");
        List<SensorParamView> plist = new ArrayList<>();
        try{
        if (request.getSensorCode() == null || request.getSensorCode().isEmpty())
            throw new TemsBadRequestException(SENSOR_CODE);
        if (request.getLoggedIn() == null)
            throw new TemsBadRequestException(USER_NOT_FOUND);
        if (!request.getSensorCode().equalsIgnoreCase(OTHER)) {
            Sensors sensor = sensorRepository.findBySensorCode(request.getSensorCode());
            log.debug("Fetching params for sensor "+request.getSensorCode());
            plist = sensorParamViewRepository.findAllParamsBySensor(sensor.getSensorId(),request.getLoggedIn());

        } else {
            SensorParamView param = sensorParamViewRepository.findAllByParamName(CommonUtil.PARAMNAME,request.getLoggedIn());
            plist.add(param);
        }
        }catch(Exception e){
            log.debug("Exception from  loadParametersByType"+e.getMessage());
            if(e instanceof TemsBadRequestException)
                throw e;
            else
                throw new TemsCustomException(e.getMessage());
        }
        log.info("loadParametersByType exit ");
        return plist;
    }
    @Override
    public List<SensorParamView> getAllParamsBySensor(Request req) {
        List<SensorParamView> paramList = new ArrayList<>();
        log.info("getAllParamsBySensor entry ");
        if (req.getStation() == null)
            throw new TemsBadRequestException(STN_NOT_FOUND);
        if (req.getSensorCode() == null)
            throw new TemsBadRequestException(SENSOR_CODE);

        if (req.getStation().contains(",")) {
            paramList = sensorParamViewRepository.findAllMETDataParams(req.getLoggedIn());
        } else {
            paramList = sensorParamViewRepository.findAllParametersByStationAndSensor(Integer.valueOf(req.getStation()), req.getSensorCode(),req.getLoggedIn());
        }
        log.info("getAllParamsBySensor exit ");
        return paramList;
    }
    @Override
    public List<UserSensorParamUnitsResponse> getAllParamsBySensorAndUser(Request req) {
        List<UserSensorParamUnitsResponse> paramList = new ArrayList<>();
        log.info("getAllParamsBySensorAndUser entry ");
        if (req.getSensorCode() == null)
            throw new TemsBadRequestException(SENSOR_CODE);
        paramList = upuRepo.findAllByUser(req.getLoggedIn(),req.getSensorCode());
        log.info("getAllParamsBySensorAndUser exit ");
        return paramList;
    }
    @Override
    public String getAllStatisticsParamsBySensor(Request req) {
        List<SensorParamView> paramList;
        JSONArray parameterList = new JSONArray();
        log.info("getAllStatisticsParamsBySensor entry ");
        if (req.getStation() == null)
            throw new TemsBadRequestException(STN_NOT_FOUND);
        if (req.getSensorCode() == null)
            throw new TemsBadRequestException(SENSOR_CODE);
        if (req.getStation().contains(",")) {
            paramList = sensorParamViewRepository.findAllMETDataParamsForStat(req.getLoggedIn());
        } else {
            paramList = sensorParamViewRepository.findAllParametersByStationAndSensorForStat(Integer.valueOf(req.getStation()), req.getSensorCode(),req.getLoggedIn());
        }
        if(!paramList.isEmpty()){
            paramList.forEach(pm->{
                JSONObject obj = new JSONObject();
                log.debug("getAllStatisticsParamsBySensor param  "+pm.getParamName());
                if((pm.getParamName().equalsIgnoreCase("Wind Speed") || pm.getParamName().contains("Cell")
                        || pm.getParamName().equalsIgnoreCase("Wind Direction") )) {
                    if(pm.getParamName().contains("Speed")) {
                        obj.put("paramId", pm.getParamName().replace(" Speed", ""));
                        obj.put("paramName", pm.getDataParamName().replace(" Speed", ""));
                        parameterList.put(obj);
                    }
                }else{
                    obj.put("paramId", pm.getParamId());
                    obj.put("paramName", pm.getDataParamName());
                    parameterList.put(obj);
                }

            });

            }
        log.info("getAllStatisticsParamsBySensor exit ");
        return parameterList.toString();
    }
    @Override
    public String loadSelectedParam(Request request) {
        JSONArray array = new JSONArray();
       if (request.getLoggedIn() == null )
            throw new TemsBadRequestException("User not found");
        if (request.getParamId() == null)
            throw new TemsBadRequestException("Parameter not found");
        SensorParamView parameter = sensorParamViewRepository.findByParameterId(request.getParamId());
        if (parameter == null)
            throw new TemsCustomException("Parameter not found");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paramDetails", new JSONObject(parameter));
        //UserSensorParamUnits upu = upuRepo.findAllByUserAndParam(request.getLoggedIn(),request.getParamId());
        List<ParameterUnits> paramUnits = puRepo.findByParameterIdFromView(parameter.getParamId());
        if (!paramUnits.isEmpty()) {
            for (ParameterUnits pu : paramUnits) {
                JSONObject jsonObj = new JSONObject();
                Units unit = pu.getUnitId();
                if (unit != null) {
                    jsonObj.put("unitId", unit.getUnitId());
                    jsonObj.put("unitSymbol", unit.getUnitSymbol());
                } else {
                    jsonObj.put("unitId", JSONObject.NULL);
                    jsonObj.put("unitSymbol", JSONObject.NULL);
                }

                jsonObj.put("flag", Objects.equals(unit.getUnitSymbol(), parameter.getUnitSymbol()));
                array.put(jsonObj);
                jsonObj = null;
            }
        }
        jsonObject.put("paramUnits", array);
        paramUnits = null;
        array = null;
        return jsonObject.toString();
    }
    @Override
    public String  updateParamDetails(SensorParamView paramResp,HttpServletRequest httpRequest) {
        JSONObject obj = new JSONObject();
        StringBuilder reqDtls = new StringBuilder();
        if (paramResp.getLoggedIn() == null || paramResp.getLoggedIn().isBlank()) {
            throw new TemsBadRequestException("User not found");
        }
        if (paramResp.getParamId() == null) {
            throw new TemsBadRequestException("Parameter not found");
        }
        var pms = sensorParamRepository.findById(paramResp.getParamId());
        pms.ifPresentOrElse(parameter -> {
            parameter.setMin(paramResp.getMin());
            parameter.setMax(paramResp.getMax());
            parameter.setWarn(paramResp.getWarn());
            parameter.setDanger(paramResp.getDanger());
            parameter.setDisplayRoundTo(paramResp.getDisplayRoundTo());
            parameter.setNotifyFlag(paramResp.getNotifyFlag());
            parameter.setParameterDisplayName(paramResp.getParameterDisplayName());
            parameter.setWarnOperation(paramResp.getWarnOperation());
            parameter.setDangerOperation(paramResp.getDangerOperation());

                var unit = unitRepository.findById(paramResp.getUnitId());
            unit.ifPresent(u -> {
                var paramUnits = puRepo.findBySensorParamsAndUnitId(parameter, u);
                if (paramUnits == null) {
                    paramUnits = new ParameterUnits();
                    paramUnits.setUnitId(u);
                    paramUnits.setSensorParams(parameter);
                    puRepo.save(paramUnits);
                }
                parameter.setParamUnitId(paramUnits);
            });
            sensorParamRepository.save(parameter);
            obj.put(CommonUtil.MSG, CommonUtil.SUCCESS);
            reqDtls.append(REMOTE_ADDR).append(httpRequest.getRemoteAddr()).
                    append(httpRequest.getRemoteHost()).append(USER_AGENT_STR).
                    append(httpRequest.getHeader(USER_AGENT));
            commonService.saveAuditDetails(Integer.parseInt(paramResp.getLoggedIn()),"Updated","",parameter.getParamName() + " configuration details","Configuration",new Date());
        }, () -> {
            obj.put(CommonUtil.MSG, CommonUtil.ERROR);
        });
        return obj.toString();
    }

    @Override
    public List<SensorParamResponse> getAllParameterBySensorAndUser(Request req) {
        List<SensorParamResponse> paramList = new ArrayList<>();
        log.info("getAllParamsBySensorAndUser entry ");
        if (req.getSensorCode() == null)
            throw new TemsBadRequestException(SENSOR_CODE);
        paramList = upuRepo.findAllParamByUser(req.getLoggedIn(),req.getSensorCode());
        log.info("getAllParamsBySensorAndUser exit ");
        return paramList;
    }

}
