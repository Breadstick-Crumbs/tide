package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.config.MQConfig;
import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.model.ParamDtlsPojo;
import com.tridel.tems_sensor_service.model.UserSensorParamUnitsPojo;
import com.tridel.tems_sensor_service.model.request.ReportIntervalRequest;
import com.tridel.tems_sensor_service.model.request.ReportRequest;
import com.tridel.tems_sensor_service.model.request.UserActionLogReq;
import com.tridel.tems_sensor_service.model.response.*;
import com.tridel.tems_sensor_service.repository.SensorParamViewRepository;
import com.tridel.tems_sensor_service.repository.UserSensorParameterUnitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.tridel.tems_sensor_service.util.CommonUtil.dateFormat3;
@Service
@Slf4j
public class CommonService {
    SensorParamViewRepository sensorParamViewRepository;
    UserSensorParameterUnitRepository upuRepo;
    @Autowired
    CommonService(SensorParamViewRepository sensorParamViewRepository,UserSensorParameterUnitRepository upuRepo){
        this.sensorParamViewRepository = sensorParamViewRepository;
        this.upuRepo = upuRepo;
    }
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void saveAuditDetails(int userId, String userAction, String reqDetails, String actionRemarks, String actionModule, Date createdDate) {
        UserActionLogReq request = new UserActionLogReq();
        try {
            request.setUserId(userId);
            request.setUserAction(userAction);
            request.setReqDetails(reqDetails);
            request.setActionRemarks(actionRemarks);
            request.setActionModule(actionModule);
            request.setCreated(createdDate);
//            rabbitTemplate.convertAndSend(MQConfig.USRACTIONEXCHANGE, MQConfig.MSG_ROUTING_KEY, request);
        }catch(Exception e){
            log.info("Rabbit MQ Connection failed"+e.getMessage());
            throw new TemsCustomException("Sensor data audit failed");
        }
   /* @Cacheable(value = "legendsCache", key = "#root.method.name")
    public Map<String, StationLegendsPojo> getLegendsMap() {
        List<StationLegendsPojo> legendsList = circuitBreakerWrapper.getLegendsWithCircuitBreaker();
        return legendsList.stream()
                .collect(Collectors.toMap(StationLegendsPojo::getLegendName, legend -> legend));
    }

    public Map<String, StationLegendsPojo> getLegendsMapFromCache() {
        Cache cache = cacheManager.getCache("legendsCache");
        Map<String, StationLegendsPojo> cachedLegendsMap = cache.get("getLegendsMap", Map.class);
        if (cachedLegendsMap == null) {
            List<StationLegendsPojo> legendsList = circuitBreakerWrapper.getLegendsWithCircuitBreaker();
            cachedLegendsMap = legendsList.stream()
                    .collect(Collectors.toMap(StationLegendsPojo::getLegendName, legend -> legend));
            cache.put("getLegendsMap", cachedLegendsMap);
        }

        return cachedLegendsMap;
    }*/
    }
    public String changeDateFormat(String dat) {
        if (dat != null) {
            String[] date = dat.split(" ");
            String[] date1 = date[0].split("-");
            return date1[2] + "/" + date1[1] + "/" + date1[0] + " " + date[1];
        }
        return null;
    }
    public LocalDateTime getDateAfterSecCheck(String paramDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat3);
        LocalDateTime dateTime = LocalDateTime.parse(paramDateStr, formatter);
        if (dateTime.getSecond() > 30) {
            dateTime = dateTime.plusMinutes(1).withSecond(0);
        }
        return dateTime;
    }

    public double applyOperation(String beforeOp, String operation, BigDecimal afterOp){
        double data =Double.parseDouble(beforeOp);
        if (operation.equals("*")) {
            data *= afterOp.doubleValue();
        } else if (operation.equals("+")) {
            data += afterOp.doubleValue();
        } else if (operation.equals("-")) {
            data -= afterOp.doubleValue();
        } else if (operation.equals("/")) {
            data /= afterOp.doubleValue();
        }
        return data;
    }
    public MetricsPojo getDataMetrics(UserSensorParamUnitsPojo upu, SensorParamView spm, String paramVal){
        MetricsPojo pojo = new MetricsPojo();
        String data = null;
        double dat = 0.00;
        double warn = 0.00;
        double danger = 0.00;


        if (upu != null && upu.getOperation() != null) {
            spm.setUnitSymbol(upu.getUnitSymbol());
            String operation = upu.getOperation();
            if (operation.equalsIgnoreCase("(* 1.8)+32")) {
                dat = (Double.parseDouble(paramVal) * 1.8) + 32;
                warn = (spm.getWarn() * 1.8) + 32;
                danger = (spm.getDanger() * 1.8) + 32;

            } else {
                dat = applyOperation(paramVal, operation, upu.getCalculatedValue());
                warn = applyOperation(spm.getWarn().toString(), operation, upu.getCalculatedValue());
                danger = applyOperation(spm.getDanger().toString(), operation, upu.getCalculatedValue());
            }
        } else {
            danger = spm.getDanger();
            warn = spm.getWarn();
            dat = Double.parseDouble(paramVal);
        }
        pojo.setData(dat);
        pojo.setDanger(danger);
        pojo.setWarn(warn);
        return pojo;
    }

    public String fallbackMethod(Throwable t) {
        return "Service is temporarily unavailable. Please try again later.";
    }

    public List<SensorParamView> getHeaderDataForReports(ReportRequest request, String[] stationSelected, List<String> headerList, List<ParamDtlsPojo> paramDtlsList) {
        List<SensorParamView> paramList;
        log.info("getHeaderDataForReports entry ");
        List<Integer> stations = Arrays.stream(stationSelected).map(Integer::parseInt).toList();
        paramList = sensorParamViewRepository.findAllParametersByStationAndSensorForReport(stations, request.getSensorCode(), request.getLoggedIn());
        AtomicInteger count = new AtomicInteger();
        paramList.forEach(param -> {
            log.debug("getHeaderDataForReports parameter "+param.getParamName());
            ParamDtlsPojo paramDtlsPojo = new ParamDtlsPojo(count.get(),param.getParamId(),param.getParamName(),param.getWarn(),param.getWarnOperation(),param.getDanger(),param.getDangerOperation());
            UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(request.getLoggedIn(), param.getParamId());
            if (upu != null && upu.getOperation() != null) {
                param.setUnitSymbol(upu.getUnitSymbol());
                param.setOperation(upu.getOperation());
                param.setCalculatedValue(String.valueOf(upu.getCalculatedValue()));
            }else{
                param.setOperation("*");
                param.setCalculatedValue("1");
            }
            param.setDisplayRoundTo(param.getDisplayRoundTo() != null?param.getDisplayRoundTo():1);
            String paramHeader = param.getParamName() + "(" + param.getUnitSymbol() + ") </br><span class='text-muted'> Min: " + param.getMin() + " Max: " + param.getMax() + "</span>";
            headerList.add(paramHeader);
            paramDtlsList.add(paramDtlsPojo);
            count.getAndIncrement();
        });
        log.info("getHeaderDataForReports exit ");
        return paramList;
    }


}
