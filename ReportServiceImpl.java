package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.dao.ReportDao;
import com.tridel.tems_sensor_service.entity.master.ReportInterval;
import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.model.ParamDtlsPojo;
import com.tridel.tems_sensor_service.model.UserSensorParamUnitsPojo;
import com.tridel.tems_sensor_service.model.request.ReportRequest;
import com.tridel.tems_sensor_service.model.response.ReportIntervalResponse;
import com.tridel.tems_sensor_service.model.response.SensorParamView;
import com.tridel.tems_sensor_service.repository.ReportIntervalRepository;
import com.tridel.tems_sensor_service.repository.SensorParamViewRepository;
import com.tridel.tems_sensor_service.repository.UserSensorParameterUnitRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tridel.tems_sensor_service.util.CommonUtil.*;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService{
    SensorParamViewRepository sensorParamViewRepository;
    UserSensorParameterUnitRepository upuRepo;
//    MasterServiceFeignClient masterServiceFeignClient;
    ReportDao reportDao;
    CommonService commonService;
    ReportIntervalRepository reportIntervalRepo;
//    MasterDataCircuitBreakerWrapper circuitBreakerWrapper;
    DataServiceCircuitBreakerWrapper dataServiceCircuitBreakerWrapper;
    ReportServiceImpl(SensorParamViewRepository sensorParamViewRepository,UserSensorParameterUnitRepository upuRepo,
                      ReportDao reportDao, CommonService commonService, ReportIntervalRepository reportIntervalRepo,
//                      MasterServiceFeignClient masterServiceFeignClient,MasterDataCircuitBreakerWrapper circuitBreakerWrapper,
                      DataServiceCircuitBreakerWrapper dataServiceCircuitBreakerWrapper){
        this.sensorParamViewRepository = sensorParamViewRepository;
        this.upuRepo = upuRepo;
        this.reportDao = reportDao;
        this.commonService = commonService;
        this.reportIntervalRepo = reportIntervalRepo;
//        this.masterServiceFeignClient = masterServiceFeignClient;
//        this.circuitBreakerWrapper = circuitBreakerWrapper;
        this.dataServiceCircuitBreakerWrapper = dataServiceCircuitBreakerWrapper;
    }

    @Override
    public String loadReportsDataOneMinute(ReportRequest request) {
        List<SensorParamView> paramList;
        List<String> headerList = new ArrayList<>();
        List<ParamDtlsPojo> paramDtlsList = new ArrayList<>();
        log.info("loadReportsDataOneMinute entry ");
        headerList.add(STATIONNAME);
        headerList.add(DATE + " " + TIME);
        String dataObj= null;
        try {
            String[] stationSelected = request.getStation().split(",");
            if (stationSelected.length > 1) {
                request.setSensorCode("MET");
            }
            paramList = commonService.getHeaderDataForReports(request, stationSelected, headerList,paramDtlsList);
            log.debug("loadReportsDataOneMinute header data from data service list size "+(paramList.isEmpty()?0:paramList.size()));
            request.setStation(request.getStation());
            request.setParamList(paramList);
            request.setSensorTableCode(paramList.getFirst().getSensorTableName());
            dataObj = dataServiceCircuitBreakerWrapper.loadReportsDataOneMinute(request);
        }catch(Exception e){
            log.debug("loadReportsDataOneMinute exception "+e.getMessage());
            throw new TemsCustomException(ONE_MIN_DATA_FETCH_FAIL);
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("header", headerList);
        jsonObj.put("parameters", paramDtlsList);
        jsonObj.put("data", dataObj);
        log.info("loadReportsDataOneMinute exit ");
        return jsonObj.toString();
    }

    @Override
    public String loadReportsDataByInterval(ReportRequest request) {
        List<SensorParamView> paramList;
        List<String> headerList = new ArrayList<>();
        List<ParamDtlsPojo> paramDtlsList = new ArrayList<>();
        log.info("loadReportsDataByInterval entry ");
        headerList.add(STATIONNAME);
        headerList.add(DATE + " " + TIME);
        String dataObj= null;
        try {
            String[] stationSelected = request.getStation().split(",");
            if (stationSelected.length > 1) {
                request.setSensorCode("MET");
            }
            List<Integer> stations = Arrays.stream(stationSelected).map(Integer::parseInt).toList();
            paramList = sensorParamViewRepository.findAllParametersByStationAndSensorForReport(stations, request.getSensorCode(), request.getLoggedIn());
            getHeaderDataForReports(request, paramList, headerList,paramDtlsList);
//            ReportInterval intervalMap = commonService.getReportIntervalFromCache().get(Integer.parseInt(request.getInterval()));
            Optional<ReportInterval> interval = reportIntervalRepo.findById(Integer.valueOf(request.getInterval()));
            if(interval.isEmpty()) throw new TemsCustomException(INTERVAL_DATA_FETCH_FAIL);
            ReportInterval intervalMap = interval.get();

            if(StringUtils.isNotEmpty(intervalMap.getReportIntervalEN())) {
                request.setInterval(intervalMap.getReportBaseIntervalMinutes() == null ? intervalMap.getReportIntervalEN() : intervalMap.getReportBaseIntervalMinutes());
                request.setParamList(paramList);
                request.setSensorTableCode(paramList.getFirst().getSensorTableName());
                dataObj = dataServiceCircuitBreakerWrapper.getReportByInterval(request);
            }else{
                log.debug("Master data service is down : Report Interval ");
                throw new TemsCustomException(INTERVAL_DATA_FETCH_FAIL);
            }
        }catch(Exception e){
            log.debug("loadReportsDataByInterval exception "+e.getMessage());
            throw new TemsCustomException(INTERVAL_DATA_FETCH_FAIL);
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("header", headerList);
        jsonObj.put("parameters", paramDtlsList);
        jsonObj.put("data", dataObj);
        log.info("loadReportsDataByInterval exit ");
        return jsonObj.toString();
    }


//    private void getHeaderData(ReportRequest request, List<SensorParamView> paramList, List<String> headerList, List<ParamDtlsPojo> paramDtlsList, List<Boolean> functionOps) {
//         log.info("getHeaderData entry ");
//         AtomicInteger count = new AtomicInteger();
//         paramList.forEach(param -> {
//             log.debug("getHeaderData param "+param.getParamName());
//             ParamDtlsPojo paramDtlsPojo = new ParamDtlsPojo(count.get(),param.getParamId(),param.getParamName(),param.getWarn(),param.getWarnOperation(),param.getDanger(),param.getDangerOperation());
//             UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(request.getLoggedIn(), param.getParamId());
//             if (upu != null && upu.getOperation() != null) {
//                 param.setUnitSymbol(upu.getUnitSymbol());
//                 param.setOperation(upu.getOperation());
//                 param.setCalculatedValue(String.valueOf(upu.getCalculatedValue()));
//             }else{
//                 param.setOperation("*");
//                 param.setCalculatedValue("1");
//             }
//             param.setDisplayRoundTo(param.getDisplayRoundTo() != null?param.getDisplayRoundTo():1);
//             *//*String paramHeaderMin = param.getParamName() + " Min (" + param.getUnitSymbol() + ") </br><span class='text-muted'> Min: " + param.getMin() + " Max: " + param.getMax() + "</span>";
//            String paramHeaderMax = param.getParamName() + " Max (" + param.getUnitSymbol() + ") </br><span class='text-muted'> Min: " + param.getMin() + " Max: " + param.getMax() + "</span>";
//            String paramHeaderAvg = param.getParamName() + " Avg (" + param.getUnitSymbol() + ") </br><span class='text-muted'> Min: " + param.getMin() + " Max: " + param.getMax() + "</span>";
//            headerList.add(paramHeaderMin);
//            headerList.add(paramHeaderMax);
//            headerList.add(paramHeaderAvg);*//*
//            String paramHeaderMin = "";
//            if(functionOps.get(0)) {
//                paramHeaderMin=param.getParamName() + " Min (" + param.getUnitSymbol() + ") </br><span class='text-muted'> Min: " + param.getMin() + " Max: " + param.getMax() + "</span>";
//                headerList.add(paramHeaderMin);
//            }
//            String paramHeaderMax =  "";
//            if(functionOps.get(1)) {
//                paramHeaderMax = param.getParamName() + " Max (" + param.getUnitSymbol() + ") </br><span class='text-muted'> Min: " + param.getMin() + " Max: " + param.getMax() + "</span>";
//                headerList.add(paramHeaderMax);
//            }
//            String paramHeaderAvg = "";
//            if(functionOps.get(2)) {
//                paramHeaderAvg = param.getParamName() + " Avg (" + param.getUnitSymbol() + ") </br><span class='text-muted'> Min: " + param.getMin() + " Max: " + param.getMax() + "</span>";
//                headerList.add(paramHeaderAvg);
//            }
//            String paramHeaderSum = "";
//            if(functionOps.get(3)) {
//                paramHeaderSum = param.getParamName() + " Sum (" + param.getUnitSymbol() + ")";
//                headerList.add(paramHeaderSum);
//            }
//            String paramHeaderCount = "";
//            if(functionOps.get(4)) {
//                paramHeaderCount = param.getParamName() + " Count ";
//                headerList.add(paramHeaderCount);
//            }
//            String paramHeaderMode = "";
//            if(functionOps.get(5)) {
//                paramHeaderMode = param.getParamName() + " Mode (" + param.getUnitSymbol() + ")";
//                headerList.add(paramHeaderMode);
//            }
//            String paramHeaderStdDev ="";
//            if(functionOps.get(6)) {
//                paramHeaderStdDev = param.getParamName() + " Standard Deviation (" + param.getUnitSymbol() + ")";
//                headerList.add(paramHeaderStdDev);
//            }
//            paramDtlsList.add(paramDtlsPojo);
//            count.getAndIncrement();
//        });
//        log.info("getHeaderData exit ");
//    }

    private void getHeaderDataForReports(ReportRequest request, List<SensorParamView> paramList, List<String> headerList,List<ParamDtlsPojo> paramDtlsList) {
        log.info("getHeaderData entry ");
        AtomicInteger count = new AtomicInteger();
        List<String> functionList = request.getFunctions();
        List<Boolean> functionOps=List.of(functionList.stream().anyMatch(s -> s.equalsIgnoreCase("MIN")),
                functionList.stream().anyMatch(s -> s.equalsIgnoreCase("MAX")),
                functionList.stream().anyMatch(s -> s.equalsIgnoreCase("AVG")),
                functionList.stream().anyMatch(s -> s.equalsIgnoreCase("SUM")),
                functionList.stream().anyMatch(s -> s.equalsIgnoreCase("COUNT")),
                functionList.stream().anyMatch(s -> s.equalsIgnoreCase("MODE")),
                functionList.stream().anyMatch(s -> s.equalsIgnoreCase("STDEV")));
        request.setContainsFunctions(functionOps);
        paramList.forEach(param -> {
            log.debug("getHeaderData param "+param.getParamName());
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
            String paramHeaderMin = "";
            if(functionOps.get(0)) {
                paramHeaderMin=param.getParamName() + " Min (" + param.getUnitSymbol() + ") </br><span class='text-muted'> Min: " + param.getMin() + " Max: " + param.getMax() + "</span>";
                headerList.add(paramHeaderMin);
            }
            String paramHeaderMax =  "";
            if(functionOps.get(1)) {
                paramHeaderMax = param.getParamName() + " Max (" + param.getUnitSymbol() + ") </br><span class='text-muted'> Min: " + param.getMin() + " Max: " + param.getMax() + "</span>";
                headerList.add(paramHeaderMax);
            }
            String paramHeaderAvg = "";
            if(functionOps.get(2)) {
                paramHeaderAvg = param.getParamName() + " Avg (" + param.getUnitSymbol() + ") </br><span class='text-muted'> Min: " + param.getMin() + " Max: " + param.getMax() + "</span>";
                headerList.add(paramHeaderAvg);
            }
            String paramHeaderSum = "";
            if(functionOps.get(3)) {
                paramHeaderSum = param.getParamName() + " Sum (" + param.getUnitSymbol() + ")";
                headerList.add(paramHeaderSum);
            }
            String paramHeaderCount = "";
            if(functionOps.get(4)) {
                paramHeaderCount = param.getParamName() + " Count ";
                headerList.add(paramHeaderCount);
            }
            String paramHeaderMode = "";
            if(functionOps.get(5)) {
                paramHeaderMode = param.getParamName() + " Mode (" + param.getUnitSymbol() + ")";
                headerList.add(paramHeaderMode);
            }
            String paramHeaderStdDev ="";
            if(functionOps.get(6)) {
                paramHeaderStdDev = param.getParamName() + " Standard Deviation (" + param.getUnitSymbol() + ")";
                headerList.add(paramHeaderStdDev);
            }
            paramDtlsList.add(paramDtlsPojo);
            count.getAndIncrement();
        });
        log.info("getHeaderData exit ");
    }

    @Override
    public String loadReportsDataHourly(ReportRequest request) {
        List<SensorParamView> paramList;
        List<String> headerList = new ArrayList<>();
        List<ParamDtlsPojo> paramDtlsList = new ArrayList<>();
        log.info("loadReportsDataHourly entry ");
        headerList.add(STATIONNAME);
        headerList.add(DATE + " " + TIME);
        String dataObj= null;
        try {
            String[] stationSelected = request.getStation().split(",");
            if (stationSelected.length > 1) {
                request.setSensorCode("MET");
            }
            paramList = commonService.getHeaderDataForReports(request, stationSelected, headerList,paramDtlsList);
            request.setStation(request.getStation());
            request.setParamList(paramList);
            if(request.getInterval().equalsIgnoreCase("9"))
                request.setMinIntrvl(50);
            request.setSensorTableCode(paramList.getFirst().getSensorTableName());
            dataObj = dataServiceCircuitBreakerWrapper.loadReportsDataHourly(request);
        }catch(Exception e){
            log.debug("loadReportsDataHourly 50th min exception "+e.getMessage());
            throw new TemsCustomException(HOURLY_DATA_FETCH_FAIL);
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("header", headerList);
        jsonObj.put("parameters", paramDtlsList);
        jsonObj.put("data", dataObj);

        log.info("loadReportsDataHourly exit ");
        return jsonObj.toString();
    }

    @Override
    public String loadClimateReportsData(ReportRequest request) {
        List<SensorParamView> paramList;
        List<String> headerList = new ArrayList<>();
        List<ParamDtlsPojo> paramDtlsList = new ArrayList<>();
        log.info("loadClimateReportsData entry ");
//        String headerInterval = Map.of(
//                "1", "Hour",
//                "2", "Day",
//                "3", "Day",
//                "4", "Month"
//        ).getOrDefault(request.getInterval(), "Date Time");
        headerList.add("Date Time");
        JSONObject obj = new JSONObject();
        String dataObj = "";

//        List<String> header;
        try {
            String[] stationSelected = request.getStation().split(",");
            if (stationSelected.length > 1) {
                request.setSensorCode("MET");
            }
            if (request.getDateType().equalsIgnoreCase("1min")) {

                paramList = commonService.getHeaderDataForReports(request, stationSelected, headerList,paramDtlsList);
                request.setStation(request.getStation());
                request.setParamList(paramList);
                request.setSensorTableCode(paramList.getFirst().getSensorTableName());
                getDatesForDataFetch(request);
                dataObj = dataServiceCircuitBreakerWrapper.loadClimateReportsDataOneMin(request);
                obj.put("header", headerList);
                obj.put("parameters", paramDtlsList);
                obj.put("data", new JSONArray(dataObj));
                dataObj = obj.toString();
            } /*else {
                List<Integer> stations = Arrays.stream(stationSelected).map(Integer::parseInt).toList();
                paramList = sensorParamViewRepository.findAllParametersByStationAndSensorForReport(stations, request.getSensorCode(), request.getLoggedIn());
                getHeaderData(request, paramList, headerList,paramDtlsList);
                request.setStation(request.getStation());
                request.setParamList(paramList);
                request.setSensorTableCode(paramList.getFirst().getSensorTableName());
                dataObj = dataServiceFeignClient.loadClimateReportsDataInterval(request);
                obj.put("header", headerList);
                obj.put("parameters", paramDtlsList);
                obj.put("data", dataObj);
                dataObj = obj.toString();
            }*/
        } catch (Exception e) {
            log.debug("loadClimateReportsData exception "+e.getMessage());
            throw new TemsCustomException(HOURLY_DATA_FETCH_FAIL);
        }
        log.info("loadClimateReportsData entry ");
        return dataObj;
    }

    private static void getDatesForDataFetch(ReportRequest request) throws ParseException {
        log.info("getDatesForDataFetch entry ");
        Date fromDate = dateFormat2.parse(request.getFromDate().replace("T", " "));
        Date toDate = dateFormat2.parse(request.getToDate().replace("T", " "));
        Date today = new Date();
        if (toDate.after(today))
            toDate = today;
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(fromDate);
        fromCal.set(Calendar.HOUR_OF_DAY, 0);
        fromCal.set(Calendar.MINUTE, 0);
        fromCal.set(Calendar.SECOND, 0);

        Calendar toCal = Calendar.getInstance();
        toCal.setTime(toDate);
        toCal.set(Calendar.HOUR_OF_DAY, 23);
        toCal.set(Calendar.MINUTE, 59);
        toCal.set(Calendar.SECOND, 59);
        request.setFromDate(dateFormat1.format(fromCal.getTime()));
        request.setToDate(dateFormat1.format(toCal.getTime()));
        log.info("getDatesForDataFetch exit ");
    }
//    @CircuitBreaker(name = "masterDataCB", fallbackMethod = "fallbackAllReportInterval")
    @Override
    public String loadClimateIntervalFilterReportsData(ReportRequest request) {
        String dataObj = "";
        List<SensorParamView> paramList;
        List<ParamDtlsPojo> paramDtlsList = new ArrayList<>();
        List<String> headerList = new ArrayList<>();
        log.info("loadClimateIntervalFilterReportsData entry ");
        try {
            String headerInterval = Map.of(
                    "4", "Hour",
                    "2", "Time",
                    "3", "Time",
                    "6", "Day",
                    "7", "Day",
                    "8", "Month"
            ).getOrDefault(request.getInterval(), "Date Time");
            headerList.add(headerInterval);
            String[] stationSelected = request.getStation().split(",");
            if (stationSelected.length > 1) {
                request.setSensorCode("MET");
            }

            List<Integer> stations = Arrays.stream(stationSelected).map(Integer::parseInt).toList();
            paramList = sensorParamViewRepository.findAllParametersByStationAndSensorForReport(stations, request.getSensorCode(), request.getLoggedIn());
            getHeaderDataForReports(request, paramList, headerList,paramDtlsList);

            request.setStation(request.getStation());
            request.setParamList(paramList);
//            ReportInterval intervalMap = circuitBreakerWrapper.getAllReportIntervalWithCircuitBreaker(new ReportIntervalRequest(Integer.parseInt(request.getInterval()), null));
            Optional<ReportInterval> interval = reportIntervalRepo.findById(Integer.valueOf(request.getInterval()));
            if(interval.isEmpty()) throw new TemsCustomException(INTERVAL_DATA_FETCH_FAIL);
            ReportInterval intervalMap = interval.get();
            if(StringUtils.isNotEmpty(intervalMap.getReportIntervalEN())) {
                request.setInterval(intervalMap.getReportBaseIntervalMinutes() == null ? intervalMap.getReportIntervalEN() : intervalMap.getReportBaseIntervalMinutes());

                request.setSensorTableCode(paramList.getFirst().getSensorTableName());
                dataObj = dataServiceCircuitBreakerWrapper.loadClimateReportsDataInterval(request);
            }else{

                throw new TemsCustomException(HOURLY_DATA_FETCH_FAIL);
            }
        }catch (Exception e) {
            if(e instanceof  TemsCustomException)
                log.info("Master service to fetch report interval down ");
            else
                log.info("loadClimateIntervalFilterReportsData exception "+e.getMessage());
            throw new TemsCustomException(HOURLY_DATA_FETCH_FAIL);
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("header", headerList);
        jsonObj.put("parameters", paramDtlsList);
        jsonObj.put("data", new JSONArray(dataObj));
        log.info("loadClimateIntervalFilterReportsData exit ");
        return jsonObj.toString();
    }
    public ReportIntervalResponse fallbackAllReportInterval(Throwable t) {
        return null;
    }

}
