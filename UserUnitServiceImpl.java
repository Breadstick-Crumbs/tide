package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.entity.*;
import com.tridel.tems_sensor_service.exception.TemsBadRequestException;
import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.model.UserSensorParamUnitsPojo;
import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.request.StatisticsDataRequest;
import com.tridel.tems_sensor_service.model.request.UserUnitRequest;
import com.tridel.tems_sensor_service.model.response.ParameterUnitPojo;
import com.tridel.tems_sensor_service.model.response.SensorParamView;
import com.tridel.tems_sensor_service.model.response.UnitResponse;
import com.tridel.tems_sensor_service.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tridel.tems_sensor_service.util.CommonUtil.*;


@Service
@Slf4j
public class UserUnitServiceImpl implements UserUnitService{

    SensorParamViewRepository sensorParamViewRepo;
    UnitRepository unitRepo;
    ParameterUnitRepository paramUnitRepo;
    UserSensorParameterUnitRepository upuRepo;
    SensorParamRepository sensorParamRepo;
    DataServiceCircuitBreakerWrapper circuitBreakerWrapper;
//    DataServiceFeignClient dataServiceFeignClient;

UserUnitServiceImpl(SensorParamViewRepository sensorParamViewRepo, UnitRepository unitRepo,
                    ParameterUnitRepository paramUnitRepo, UserSensorParameterUnitRepository upuRepo,
                    SensorParamRepository sensorParamRepo,DataServiceCircuitBreakerWrapper circuitBreakerWrapper){
    this.sensorParamViewRepo=sensorParamViewRepo;
    this.unitRepo=unitRepo;
    this.paramUnitRepo=paramUnitRepo;
    this.upuRepo=upuRepo;
    this.sensorParamRepo=sensorParamRepo;
    this.circuitBreakerWrapper = circuitBreakerWrapper;
}

    public String updateUserUnitSettings(UserUnitRequest request) {
        log.info("updateUserUnitSettings entry ");
        try {
            if (request.getLoggedIn() == null || request.getLoggedIn() == 0) {
                throw new TemsBadRequestException(USER_NOT_FOUND);
            }
            if (request.getUnitId() == null || request.getUnitId() == 0) {
                throw new TemsBadRequestException(UNIT_NOT_FOUND);
            }
            if (request.getParamId() == null || request.getParamId() == 0) {
                throw new TemsBadRequestException(PARAMETER_NOT_FOUND);
            }
            Optional<SensorParams> pm = sensorParamRepo.findById(request.getParamId());
            if (pm.isPresent()) {
                Optional<Units> unit = unitRepo.findById(request.getUnitId());
                if (unit.isPresent()) {
                    ParameterUnits paramUnits = paramUnitRepo.findBySensorParamsAndUnitId(pm.get(), unit.get());
                    UserSensorParamUnits upu = upuRepo.findAllByUserAndParam(request.getLoggedIn(), pm.get().getId());
                    if (paramUnits != null) {
                        if (upu != null) {
                            upu.setParamUnitId(paramUnits);
                            upu.setGraphYAxisMin(request.getGraphYAxisMin());
                        } else {
                            upu = new UserSensorParamUnits();
                            upu.setParamUnitId(paramUnits);
                            upu.setSensorParamId(pm.get());
                            upu.setGraphYAxisMin(request.getGraphYAxisMin());
                            upu.setUser(request.getLoggedIn());
                        }
                        upuRepo.save(upu);
                    }
                }
            }
        }catch(Exception e){
            log.debug("updateUserUnitSettings exception "+e.getMessage());
            if(e instanceof TemsBadRequestException)
                throw e;
            else
                throw new TemsCustomException(e.getMessage());
        }
        log.info("updateUserUnitSettings entry ");
        return SUCCESSFULLY_UPDATED;

    }

    @Override
    public List<UnitResponse> getUnitByUserParam(UserUnitRequest request) {
        List<UnitResponse> response = new ArrayList<>();
        if (request.getLoggedIn() == null || request.getLoggedIn() == 0) {
            throw new TemsBadRequestException(USER_NOT_FOUND);
        }
        if (request.getParamId() == null || request.getParamId() == 0) {
            throw new TemsBadRequestException(PARAMETER_NOT_FOUND);
        }
        SensorParamView parameter = sensorParamViewRepo.findByParameterId(request.getParamId());
        UserSensorParamUnits upu = upuRepo.findAllByUserAndParam(request.getLoggedIn(),request.getParamId());
        List<UnitResponse> paramUnits = paramUnitRepo.findUnitByParameterId(parameter.getParamId());
        if (!paramUnits.isEmpty())
            paramUnits.forEach(pu -> response.add(new UnitResponse(pu, upu)));
        paramUnits = null;
        return response;
    }

    @Override
    public String getParamUnitConversion(Request request) {
        JSONArray array = new JSONArray();
        log.info("getParamUnitConversion entry ");
        try {
            if (request.getLoggedIn() == null)
                throw new TemsBadRequestException("User not found");
            if (request.getId() == null)
                throw new TemsBadRequestException("Station not found");
            if (request.getParamId() == null)
                throw new TemsBadRequestException("Parameter not found");
            SensorParamView parameter = sensorParamViewRepo.findParameterDtlsById(request.getId(), request.getLoggedIn(), request.getParamId());
            List<ParameterUnitPojo> pmUnitList = paramUnitRepo.findAllByParameterId(parameter.getParamId());
            if(!pmUnitList.isEmpty()){
                StatisticsDataRequest dataRequest = new StatisticsDataRequest();
                dataRequest.setStationId(request.getId());
                dataRequest.setParamDtls(parameter);
                dataRequest.setSensorTableCode(parameter.getSensorStatusTableName());
                Double data = circuitBreakerWrapper.getLatestDataForHomeParam(dataRequest);
                for (ParameterUnitPojo pu : pmUnitList) {
                    Double data1 = null;
                    Double warn = 0.0;
                    Double danger = 0.0;
                    JSONObject obj = new JSONObject();
                    if (data != null && pu.getOperation() != null) {
                        data1 = switch (pu.getOperation()) {
                            case "+" -> data + Double.parseDouble(String.valueOf(pu.getCalculatedValue()));
                            case "*" -> data * Double.parseDouble(String.valueOf(pu.getCalculatedValue()));
                            case "(* 1.8)+32" -> (data * 1.8) + 32;
                            default -> data;
                        };
                        warn = switch (pu.getOperation()) {
                            case "+" -> parameter.getWarn() + Double.parseDouble(String.valueOf(pu.getCalculatedValue()));
                            case "*" -> parameter.getWarn() * Double.parseDouble(String.valueOf(pu.getCalculatedValue()));
                            case "(* 1.8)+32" -> (parameter.getWarn() * 1.8) + 32;
                            default -> parameter.getWarn();
                        };
                        danger = switch (pu.getOperation()) {
                            case "+" -> parameter.getDanger() + Double.parseDouble(String.valueOf(pu.getCalculatedValue()));
                            case "*" -> parameter.getDanger() * Double.parseDouble(String.valueOf(pu.getCalculatedValue()));
                            case "(* 1.8)+32" -> (parameter.getDanger() * 1.8) + 32;
                            default -> parameter.getDanger();
                        };
                    }
                    if (data1 != null && parameter.getDisplayRoundTo() != null && parameter.getDisplayRoundTo() != 0) {
                        data1 = Math.floor(data1 * Math.pow(10, parameter.getDisplayRoundTo())) / Math.pow(10, parameter.getDisplayRoundTo());
                    }
                    obj.put("id", pu.getParamUnitId());
                    obj.put("data", data1 != null ? data1 : "");
                    obj.put("warn", warn != null ? warn : "");
                    obj.put("danger", danger != null ? danger : "");
                    obj.put("dangerOp", parameter.getDangerOperation() != null ? parameter.getDangerOperation() : "");
                    obj.put("warnOp", parameter.getWarnOperation() != null ? parameter.getWarnOperation() : "");
                    obj.put("unit", pu.getUnitSymbol());
                    UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(request.getLoggedIn(), parameter.getParamId());
                    obj.put("status", (upu != null && upu.getUnitSymbol().equals(pu.getUnitSymbol())) ||
                            (upu == null && pu.getUnitSymbol().equalsIgnoreCase(parameter.getUnitSymbol())));
                    array.put(obj);
                }
            }
        }catch(Exception e){
            throw new TemsCustomException("Unit Conversion failed");
        }
        log.info("getParamUnitConversion exit ");
        return array.toString();
    }


}
