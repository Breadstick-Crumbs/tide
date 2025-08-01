package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.entity.StationData;
import com.tridel.tems_sensor_service.model.ParamDtlsPojo;
import com.tridel.tems_sensor_service.model.UserSensorParamUnitsPojo;
import com.tridel.tems_sensor_service.model.request.DataRequest;
import com.tridel.tems_sensor_service.model.request.ParamPojoReq;
import com.tridel.tems_sensor_service.model.request.SummaryRequest;
import com.tridel.tems_sensor_service.model.response.SensorParamView;
import com.tridel.tems_sensor_service.repository.SensorParamViewRepository;
import com.tridel.tems_sensor_service.repository.StationRepository;
import com.tridel.tems_sensor_service.repository.UserSensorParameterUnitRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.tridel.tems_sensor_service.util.CommonUtil.*;

@Service
@Transactional
@Slf4j
public class SummaryServiceImpl implements SummaryService {
    SensorParamViewRepository sensorParamViewRepository;
    StationRepository stationRepository;
    UserSensorParameterUnitRepository upuRepo;
    DataServiceCircuitBreakerWrapper circuitBreakerWrapper;
    SummaryServiceImpl(StationRepository stationRepository,
                       SensorParamViewRepository sensorParamViewRepository,UserSensorParameterUnitRepository upuRepo,
                       DataServiceCircuitBreakerWrapper circuitBreakerWrapper){
        this.stationRepository = stationRepository;
        this.sensorParamViewRepository = sensorParamViewRepository;
        this.upuRepo = upuRepo;
        this.circuitBreakerWrapper = circuitBreakerWrapper;
    }
    @Override
    public String loadSummaryData(SummaryRequest request) {
        JSONArray summaryData = new JSONArray();
        log.info("loadSummaryData entry ");
        DataRequest dataRequest = new DataRequest();
        List<String> header = new ArrayList<>();
        List<ParamDtlsPojo> paramDtlsList = new ArrayList<>();
        List<ParamPojoReq> paramReqList = new ArrayList<>();
        String fromDate;
        String toDate;
        int hour = 0;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from;
        LocalDateTime to;
        int user = Integer.parseInt(request.getLoggedIn());

        if (request.getInterval() == 24) {
            from = now.withHour(0).withMinute(0).withSecond(0).withNano(0).minusHours(hour);
            to = now.withHour(23).withMinute(59).withSecond(59).withNano(0).minusHours(hour);
        } else {
            from = now.minusHours(1);
            to = now.minusHours(hour);
        }
        fromDate = from.format(formatter1);
        toDate = to.format(formatter1);

//        fromDate = "2025-02-25 00:00:00";
//        toDate = "2025-02-25 23:59:59";
        header.add(STATIONNAME);
        List<StationData> stationIds = stationRepository.findAllStationsByUserForSummary(user);
        String stations =stationIds.stream()
                .map(station->String.valueOf(station.getId()))
                .collect(Collectors.joining(","));
        List<SensorParamView> summaryParametersList  = sensorParamViewRepository.findAllMETDataParamsForSummary(user);
        AtomicInteger count = new AtomicInteger();
        summaryParametersList.forEach(pm->{
            ParamDtlsPojo paramDtlsPojo = new ParamDtlsPojo(count.get(),pm.getParamId(),pm.getParamName(),pm.getWarn(),pm.getWarnOperation(),pm.getDanger(),pm.getDangerOperation());
            StringBuilder pmName = new StringBuilder(pm.getParamName());
            ParamPojoReq pojoReq = new ParamPojoReq();
            UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(Integer.valueOf(request.getLoggedIn()), pm.getParamId());
            pmName.trimToSize();
            if(!pm.getParamName().trim().equalsIgnoreCase(RAINFALL) &&
                    !pm.getParamName().trim().equalsIgnoreCase(VISIBILITY)){
                if (upu != null && upu.getOperation() != null)
                    pojoReq = new ParamPojoReq(pm.getDataParamName(),upu.getOperation(),upu.getCalculatedValue(),pm.getDisplayRoundTo()!=null?pm.getDisplayRoundTo():2,false);
                else
                    pojoReq = new ParamPojoReq(pm.getDataParamName(),"*", BigDecimal.ONE,pm.getDisplayRoundTo()!=null?pm.getDisplayRoundTo():2,false);
                /*if (!dataParams.isEmpty()) {
                    dataParams.append(",");
                }
                dataParams.append(pm.getDataParamName());*/
                header.add(pmName +" Avg");
                header.add(pmName +" Min");
                header.add(pmName +" Min Time");
                header.add(pmName +" Max");
                header.add(pmName +" Max Time");
            }else{
                if (upu != null && upu.getOperation() != null)
                    pojoReq = new ParamPojoReq(pm.getDataParamName(),upu.getOperation(),upu.getCalculatedValue(),pm.getDisplayRoundTo()!=null?pm.getDisplayRoundTo():2,true);
                else
                    pojoReq = new ParamPojoReq(pm.getDataParamName(),"*",BigDecimal.ONE,pm.getDisplayRoundTo()!=null?pm.getDisplayRoundTo():2,true);
                /*if (!avgDataParams.isEmpty()) {
                    avgDataParams.append(",");
                }
                avgDataParams.append(pm.getDataParamName());*/
                header.add(pmName.toString());
            }
            pmName.delete(0, pmName.length());
            count.getAndIncrement();
            paramDtlsList.add(paramDtlsPojo);
            paramReqList.add(pojoReq);
        });
        if(!stationIds.isEmpty()) {
            dataRequest.setFromDate(fromDate);
            dataRequest.setToDate(toDate);
            dataRequest.setParamReqList(paramReqList);
            dataRequest.setStationIds(stations);
            String summaryResponse = circuitBreakerWrapper.loadSummaryData(dataRequest);
            if(StringUtils.isNotEmpty(summaryResponse))
                summaryData = new JSONArray(summaryResponse);
        }
        JSONObject obj = new JSONObject();
        obj.put("header", header);
        obj.put("data", summaryData);
        obj.put("parameters",paramDtlsList);
        log.info("loadSummaryData exit ");
        return obj.toString();
    }
}
