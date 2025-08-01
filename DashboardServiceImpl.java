package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.exception.TemsBadRequestException;
import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.model.SensorParamViewReq;
import com.tridel.tems_sensor_service.model.UserSensorParamUnitsPojo;
import com.tridel.tems_sensor_service.model.request.*;
import com.tridel.tems_sensor_service.model.response.*;
import com.tridel.tems_sensor_service.repository.SensorParamViewRepository;
import com.tridel.tems_sensor_service.repository.StationRepository;
import com.tridel.tems_sensor_service.repository.UserSensorParameterUnitRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.tridel.tems_sensor_service.util.CommonUtil.*;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    SensorParamViewRepository sensorParamViewRepository;
    UserSensorParameterUnitRepository upuRepo;
    StationRepository stationRepo;
    CommonService commonService;
    DataServiceCircuitBreakerWrapper circuitBreakerWrapper;

    DashboardServiceImpl(SensorParamViewRepository sensorParamViewRepository,
                         CommonService commonService, UserSensorParameterUnitRepository upuRepo,
                         DataServiceCircuitBreakerWrapper circuitBreakerWrapper,StationRepository stationRepo) {
        this.sensorParamViewRepository = sensorParamViewRepository;
        this.commonService = commonService;
        this.upuRepo = upuRepo;
        this.stationRepo = stationRepo;
        this.circuitBreakerWrapper = circuitBreakerWrapper;
    }

    @Override
    public DashboardSensorDataResponse getSensorDetailsForDashboard(Request request) {
        DashboardSensorDataResponse response = new DashboardSensorDataResponse();
        List<SensorDataResponse> sensorDataList = new ArrayList<>();
        ReportRequest req = new ReportRequest();
        if (request.getLoggedIn() == null)
            throw new TemsBadRequestException(USER_NOT_FOUND);
        if (request.getId() == null)
            throw new TemsBadRequestException(STN_NOT_FOUND);
        try {
            req.setStationId(request.getId());
            HeaderDataStatusResponse stationHeader = circuitBreakerWrapper.loadHeaderData(req);
            if (stationHeader != null && stationHeader.getDateTime() != null) {
                String paramDateStr = dateFormat1.format(stationHeader.getDateTime());
                LocalDateTime paramDate = commonService.getDateAfterSecCheck(paramDateStr);
                response.setLatestTime(paramDate.format(formatter2));
            } else {
                response.setLatestTime("");
            }
            List<SensorParamView> paramList = sensorParamViewRepository.findAllParametersByStationForDashboard(request.getId(), request.getLoggedIn());
            Map<String, SensorParamViewReq> resultMap = paramList.stream()
                    .collect(Collectors.groupingBy(
                            SensorParamView::getSensorCode,
                            Collectors.collectingAndThen(
                                    Collectors.toList(),
                                    records -> {
                                        String concatenatedParams = records.stream()
                                                .map(SensorParamView::getDataParamName)
                                                .collect(Collectors.joining(","));
                                        String sensorTableCode = records.get(0).getSensorStatusTableName();
                                        return new SensorParamViewReq(request.getId(), records.getFirst().getSensorCode(), concatenatedParams, sensorTableCode, false);
                                    }
                            )
                    ));
            resultMap.forEach((sensor, sensorParamViewReq) -> {
                SensorDataResponse sensorDataResponse = new SensorDataResponse();
                List<SensorParamView> spList = new ArrayList<>();
                Map<String, SensorParamView> sensorParamMap = sensorParamViewRepository
                        .findAllParametersByStationAndSensorForDashboard(request.getId(), sensor, request.getLoggedIn())
                        .stream()
                        .collect(Collectors.toMap(
                                SensorParamView::getDataParamName,
                                sensorParam -> sensorParam,
                                (existing, replacement) -> existing,
                                LinkedHashMap::new
                        ));
                if (stationHeader != null && stationHeader.getStationId() != null) {
                    Map<String, GenericResponse> cNameValPairsMap = circuitBreakerWrapper.getSensorDataWithCircuitBreaker(sensorParamViewReq)
                            .stream().collect(Collectors.toMap(GenericResponse::getCName, cell -> cell));
                    for (Map.Entry<String, SensorParamView> entry : sensorParamMap.entrySet()) {
                        SensorParamView spm = entry.getValue();
                        UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(request.getLoggedIn(), spm.getParamId());
                        if (cNameValPairsMap.containsKey(entry.getKey())) {
                            GenericResponse param = cNameValPairsMap.get(entry.getKey());
                            String data = null;
                            MetricsPojo metricsPojo = commonService.getDataMetrics(upu, spm, param.getCVal());
                            int roundOffNum = spm.getDisplayRoundTo() != null ? spm.getDisplayRoundTo() : 1;
                            String pattern = spm.getDisplayRoundTo() != null && spm.getDisplayRoundTo() == 0
                                    ? "#" : "#." + "#".repeat(roundOffNum);
                            DecimalFormat df = new DecimalFormat(pattern);
                            data = df.format(metricsPojo.getData());
                            spm.setData(data);
                            spm.setDanger(metricsPojo.getDanger());
                            spm.setWarn(metricsPojo.getWarn());
                        }
                        sensorDataResponse.setSensorName(spm.getSensorName());
                        sensorDataResponse.setSensorOrder(spm.getSensorOrderSeq());
                        spList.add(spm);
                    }
                } else {
                    for (Map.Entry<String, SensorParamView> entry : sensorParamMap.entrySet()) {
                        SensorParamView spm = entry.getValue();
                        UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(request.getLoggedIn(), spm.getParamId());
                        spm.setUnitSymbol(upu != null ? upu.getUnitSymbol() : "");
                        spList.add(spm);
                        sensorDataResponse.setSensorName(spm.getSensorName());
                        sensorDataResponse.setSensorOrder(spm.getSensorOrderSeq());
                    }
                }
                sensorDataResponse.setSensorCode(sensor);
                spList.sort(Comparator.comparing(SensorParamView::getParamOrderSeq));
                sensorDataResponse.setSensorData(spList);
                sensorDataList.add(sensorDataResponse);

            });
            sensorDataList.sort(Comparator.comparing(SensorDataResponse::getSensorOrder));
            response.setSensorDataList(sensorDataList);
        } catch (Exception e) {
            log.info("getSensorDetailsForDashboard exception" + e.getMessage());
            throw new TemsCustomException("load Dashboard failed " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<DailyDashboardData> loadDashboardDataForStation(DailyDashboardRequest request) {
        List<DailyDashboardData> dailyDashboardDataList = new ArrayList<>();
        try {
            if (request.getLoggedIn() == null)
                throw new TemsBadRequestException(USER_NOT_FOUND);
            if (request.getStationId() == null)
                throw new TemsBadRequestException(STN_NOT_FOUND);
            log.info("loadDashboardDataForStation entry ");
            DataRequest dataRequest = new DataRequest();
            List<ParamPojoReq> paramReqList = new ArrayList<>();
            String fromDate;
            String toDate;
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime from;
            LocalDateTime to;
            int user = Integer.parseInt(request.getLoggedIn());

            from = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
            to = now.withHour(23).withMinute(59).withSecond(59).withNano(0);
            fromDate = from.format(formatter1);
            toDate = to.format(formatter1);
//            fromDate = "2025-02-25 00:00:00";
//            toDate = "2025-02-25 23:59:59";
            List<SensorParamView> summaryParametersList = sensorParamViewRepository.findAllMETDataParamsForSummary(user);

            AtomicInteger count = new AtomicInteger();
            summaryParametersList.forEach(pm -> {
                ParamPojoReq pojoReq = new ParamPojoReq();
                UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(Integer.valueOf(request.getLoggedIn()), pm.getParamId());
                if (!pm.getParamName().trim().equalsIgnoreCase(RAINFALL) &&
                        !pm.getParamName().trim().equalsIgnoreCase(VISIBILITY)) {
                    if (upu != null && upu.getOperation() != null) {
                        pm.setUnitSymbol(upu.getUnitSymbol());
                        pojoReq = new ParamPojoReq(pm.getDataParamName(), upu.getOperation(), upu.getCalculatedValue(), pm.getDisplayRoundTo() != null ? pm.getDisplayRoundTo() : 2, false);
                    } else
                        pojoReq = new ParamPojoReq(pm.getDataParamName(), "*", BigDecimal.ONE, pm.getDisplayRoundTo() != null ? pm.getDisplayRoundTo() : 2, false);
                } else {
                    if (upu != null && upu.getOperation() != null) {
                        pm.setUnitSymbol(upu.getUnitSymbol());
                        pojoReq = new ParamPojoReq(pm.getDataParamName(), upu.getOperation(), upu.getCalculatedValue(), pm.getDisplayRoundTo() != null ? pm.getDisplayRoundTo() : 2, true);
                    } else
                        pojoReq = new ParamPojoReq(pm.getDataParamName(), "*", BigDecimal.ONE, pm.getDisplayRoundTo() != null ? pm.getDisplayRoundTo() : 2, true);
                }
                count.getAndIncrement();
                paramReqList.add(pojoReq);
            });
            Map<String, SensorParamView> paramMap = summaryParametersList.stream().collect(Collectors.toMap(SensorParamView::getDataParamName, SensorParamView -> SensorParamView));
            dataRequest.setFromDate(fromDate);
            dataRequest.setToDate(toDate);
            dataRequest.setParamReqList(paramReqList);
            dataRequest.setStationIds(String.valueOf(request.getStationId()));
            dataRequest.setTableCode("tems_sensor_data_c");
            String dashResponse = circuitBreakerWrapper.loadDashboardDataForStation(dataRequest);
            if (!StringUtils.isBlank(dashResponse)) {
                JSONArray jsonArray = new JSONArray(dashResponse);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray row = jsonArray.getJSONArray(i);
                    if (paramMap.containsKey(row.getString(0))) {
                        Date minDate = null;
                        Date maxDate = null;
                        try {
                            minDate = Objects.equals(row.get(3), null) ? null : dateFormat1.parse(row.getString(3));
                            maxDate = Objects.equals(row.get(5), null) ? null : dateFormat1.parse(row.getString(5));
                        } catch (ParseException e) {
                            throw new TemsBadRequestException("Load daily dashboard failed");
                        }
                        SensorParamView spvObj = paramMap.get(row.getString(0));
                        DailyDashboardData dailyDashboardData = new DailyDashboardData();
                        dailyDashboardData.setParamId(spvObj.getParamId());
                        dailyDashboardData.setParamName(spvObj.getParamName());
                        dailyDashboardData.setParamValAvg(row.getString(1));
                        dailyDashboardData.setParamValMin(row.getString(2));
                        dailyDashboardData.setParamValMinDate(minDate != null && !row.getString(1).equals("-") ? dateFormat6.format(minDate) : "-");
                        dailyDashboardData.setParamValMax(row.getString(4));
                        dailyDashboardData.setParamValMaxDate(maxDate != null && !row.getString(1).equals("-") ? dateFormat6.format(maxDate) : "-");
                        dailyDashboardData.setParamUnit(spvObj.getUnitSymbol());
                        dailyDashboardDataList.add(dailyDashboardData);
                    }
                }
            } else {
                paramMap.forEach((s, sensorParamView) -> {
                    DailyDashboardData dailyDashboardData = new DailyDashboardData();
                    dailyDashboardData.setParamId(sensorParamView.getParamId());
                    dailyDashboardData.setParamName(sensorParamView.getParamName());
                    dailyDashboardData.setParamValMaxDate("-");
                    dailyDashboardData.setParamValMax("-");
                    dailyDashboardData.setParamValAvg("-");
                    dailyDashboardData.setParamValMin("-");
                    dailyDashboardData.setParamValMinDate("-");
                    dailyDashboardDataList.add(dailyDashboardData);
                });
            }
        } catch (Exception e) {
            if (e instanceof TemsBadRequestException)
                throw e;
            throw new TemsCustomException("Load Daily dashboard failed");
        }
        return dailyDashboardDataList;
    }

    @Override
    public WindDashboardData loadDashboardWindDataForMET(DailyDashboardRequest request) {
        Map<String, SensorParamView> paramMap = new LinkedHashMap<>();
        WindDashboardData windDashboardData = new WindDashboardData();
        if (request.getLoggedIn() == null)
            throw new TemsBadRequestException(USER_NOT_FOUND);
        if (request.getStationId() == null)
            throw new TemsBadRequestException(STN_NOT_FOUND);
        log.info("loadDashboardWindDataForMET entry ");
        int user = Integer.parseInt(request.getLoggedIn());
        DataRequest dataRequest = new DataRequest();
        List<ParamPojoReq> paramReqList = new ArrayList<>();
        ParamPojoReq wsReq = getParamPojoReq(WINDSPEED, user, paramMap);
        ParamPojoReq wdReq = getParamPojoReq(WINDDIRECTION, user, paramMap);
        ParamPojoReq wgsReq = getParamPojoReq(WINDGUSTSPEED, user, paramMap);
        ParamPojoReq wgdReq = getParamPojoReq(WINDGUSTDIRECTION, user, paramMap);
        if (!StringUtils.isBlank(wsReq.getParameterName()))
            paramReqList.add(wsReq);
        if (!StringUtils.isBlank(wdReq.getParameterName()))
            paramReqList.add(wdReq);
        if (!StringUtils.isBlank(wgsReq.getParameterName()))
            paramReqList.add(wgsReq);
        if (!StringUtils.isBlank(wgsReq.getParameterName()))
            paramReqList.add(wgdReq);
        dataRequest.setInterval(request.getInterval());
        dataRequest.setParamReqList(paramReqList);
        dataRequest.setStationIds(String.valueOf(request.getStationId()));
        dataRequest.setTableCode("tems_sensor_data_c");
        String dashResponse = circuitBreakerWrapper.loadDashboardDataForMET(dataRequest);
        if (!StringUtils.isBlank(dashResponse)) {
            JSONArray jsonArray = new JSONArray(dashResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray row = jsonArray.getJSONArray(i);
                if (paramMap.containsKey(row.getString(0))) {
                    SensorParamView spvObj = paramMap.get(row.getString(0));
                    switch (spvObj.getParamName()) {
                        case WINDSPEED -> {
                            windDashboardData.setWindSpeedAvg(Objects.requireNonNullElse(row.getString(1), "-"));
                            windDashboardData.setWindSpeedMin(Objects.requireNonNullElse(row.getString(2), "-"));
                            windDashboardData.setWindSpeedMax(Objects.requireNonNullElse(row.getString(4), "-"));
                            windDashboardData.setWindSpeedUnit(spvObj.getUnitSymbol());
                        }
                        case WINDDIRECTION -> {
                            windDashboardData.setWindDirectionAvg(Objects.requireNonNullElse(row.getString(1), "-"));
                            windDashboardData.setWindDirectionMin(Objects.requireNonNullElse(row.getString(2), "-"));
                            windDashboardData.setWindDirectionMax(Objects.requireNonNullElse(row.getString(4), "-"));
                            windDashboardData.setWindDirectionUnit(spvObj.getUnitSymbol());
                        }
                        case WINDGUSTSPEED -> {
                            windDashboardData.setWindGustSpeedAvg(Objects.requireNonNullElse(row.getString(1), "-"));
                            windDashboardData.setWindGustSpeedUnit(spvObj.getUnitSymbol());
                        }
                        case WINDGUSTDIRECTION -> {
                            windDashboardData.setWindGustDirectionAvg(Objects.requireNonNullElse(row.getString(1), "-"));
                            windDashboardData.setWindGustDirectionUnit(spvObj.getUnitSymbol());
                        }
                    }

                }
            }

        }
        replaceNulls(windDashboardData);
        return windDashboardData;
    }

    @Override
    public MetDashboardResponse loadDashboardDataForMET(DailyDashboardRequest request) {
        MetDashboardResponse response = new MetDashboardResponse();
        Map<String, SensorParamView> paramMap = new LinkedHashMap<>();
        List<MetDashboardData> hourlyData = new ArrayList<>();
        List<MetDashboardData> tenMinData = new ArrayList<>();
        List<MetDashboardData> latestData = new ArrayList<>();

        List<String> latestParams = List.of("Air Temperature", "Relative Humidity", "Dew Point", "Grass Temp",
                "Visibility", "Rainfall", "Sunshine Duration", "Past Weather", "Present Weather", "Heat Index", "Wind Chill Index");
        List<String> hourlyParams = List.of("Air Temperature", "Relative Humidity", "Dew Point", "Visibility");
        List<String> grassHourlyParams = List.of("Grass Temp");
        List<String> tenMinParams = List.of("Visibility");
        List<ParamPojoReq> paramPojoReqList = new ArrayList<>();
        DataRequest dataRequest = new DataRequest();
        log.info("loadDashboardDataForMET entry ");
        int user = Integer.parseInt(request.getLoggedIn());
        if (request.getLoggedIn() == null)
            throw new TemsBadRequestException(USER_NOT_FOUND);
        if (request.getStationId() == null)
            throw new TemsBadRequestException(STN_NOT_FOUND);
        try {
            hourlyParams.forEach(param -> {
                ParamPojoReq paramPojoReq = getParamPojoReq(param, user, paramMap);
                paramPojoReqList.add(paramPojoReq);
            });
            request.setInterval("Hourly");
            getDataForInterval(request, dataRequest, paramPojoReqList, paramMap, hourlyData, "tems_sensor_data_c");

            paramPojoReqList.clear();
            paramMap.clear();
            grassHourlyParams.forEach(param -> {
                ParamPojoReq paramPojoReq = getParamPojoReq(param, user, paramMap);
                paramPojoReqList.add(paramPojoReq);
            });
            getDataForInterval(request, dataRequest, paramPojoReqList, paramMap, hourlyData, "tems_sensor_data_i");
            paramPojoReqList.clear();
            paramMap.clear();
            response.setHourly(hourlyData.stream().collect(Collectors.toMap(MetDashboardData::getParamId, MetDashboardData -> MetDashboardData)));
            tenMinParams.forEach(param -> {
                ParamPojoReq paramPojoReq = getParamPojoReq(param, user, paramMap);
                paramPojoReqList.add(paramPojoReq);
            });
            request.setInterval("10");
            getDataForInterval(request, dataRequest, paramPojoReqList, paramMap, tenMinData, "tems_sensor_data_c");
            response.setTenMin(tenMinData.stream().collect(Collectors.toMap(MetDashboardData::getParamId, MetDashboardData -> MetDashboardData)));
            paramMap.clear();
            getLatestDataForParam(request, latestParams, user, latestData, response);
        } catch (Exception e) {
            log.info("Load Met station details failed " + e.getMessage());
            throw new TemsCustomException("Load Met station details failed ");
        }
        return response;
    }

    private void getLatestDataForParam(DailyDashboardRequest request, List<String> latestParams, int user, List<MetDashboardData> latestData, MetDashboardResponse response) {
        List<SensorParamView> sensorParamViewList = sensorParamViewRepository.findAllDtlsOfParamList(latestParams, user);

        Map<String, SensorParamViewReq> resultMap = sensorParamViewList.stream()
                .collect(Collectors.groupingBy(
                        SensorParamView::getSensorCode,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                records -> {
                                    String concatenatedParams = records.stream()
                                            .map(SensorParamView::getDataParamName)
                                            .collect(Collectors.joining(","));
                                    String sensorTableCode = records.get(0).getSensorStatusTableName();
                                    return new SensorParamViewReq(request.getStationId(), records.getFirst().getSensorCode(), concatenatedParams, sensorTableCode, false);
                                }
                        )
                ));
        resultMap.forEach((sensor, sensorParamViewReq) -> {
            List<SensorParamView> sensorViewList = sensorParamViewRepository.findAllDtlsOfParamListAndSensor(latestParams, user, sensor);
            Map<String, SensorParamView> sensorParamViewMap = sensorViewList.stream().collect(Collectors.toMap(
                    SensorParamView::getDataParamName,
                    sensorParam -> sensorParam));
            Map<String, GenericResponse> cNameValPairsMap = circuitBreakerWrapper.getSensorDataWithCircuitBreaker(sensorParamViewReq).stream().collect(Collectors.toMap(GenericResponse::getCName, cell -> cell));
            for (Map.Entry<String, SensorParamView> entry : sensorParamViewMap.entrySet()) {
                SensorParamView spm = entry.getValue();
                MetDashboardData metDashboardData = new MetDashboardData();
                UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(user, spm.getParamId());
                if (cNameValPairsMap.containsKey(entry.getKey())) {
                    GenericResponse param = cNameValPairsMap.get(entry.getKey());
                    String data = null;
                    MetricsPojo metricsPojo = commonService.getDataMetrics(upu, spm, param.getCVal());
                    int roundOffNum = spm.getDisplayRoundTo() != null ? spm.getDisplayRoundTo() : 1;
                    String pattern = spm.getDisplayRoundTo() != null && spm.getDisplayRoundTo() == 0
                            ? "#" : "#." + "#".repeat(roundOffNum);
                    DecimalFormat df = new DecimalFormat(pattern);
                    data = df.format(metricsPojo.getData());
                    metDashboardData.setParamId(spm.getParamId());
                    metDashboardData.setParamName(spm.getParamName());
                    metDashboardData.setAvg(!StringUtils.isBlank(data) ? data : "-");
                    metDashboardData.setUnit(spm.getUnitSymbol());
                    latestData.add(metDashboardData);
                } else {
                    metDashboardData.setParamId(spm.getParamId());
                    metDashboardData.setParamName(spm.getParamName());
                    metDashboardData.setUnit(spm.getUnitSymbol());
                    metDashboardData.setAvg("-");
                    latestData.add(metDashboardData);
                }

            }
            response.setLatest(latestData.stream().collect(Collectors.toMap(MetDashboardData::getParamId, MetDashboardData -> MetDashboardData)));
            String latestDateTime = null;
            try {
                if (!cNameValPairsMap.isEmpty() && StringUtils.isNotEmpty(cNameValPairsMap.get("paramDate").getCVal()))
                    latestDateTime = dateFormat4.format(dateFormat1.parse(cNameValPairsMap.get("paramDate").getCVal()));
                else
                    latestDateTime = "";
            } catch (ParseException e) {
                log.info("Load Met station details failed " + e.getMessage());
                throw new TemsCustomException("Load Met station details failed ");
            }
            response.setLatestTime(latestDateTime);
        });
    }

    private void getDataForInterval(DailyDashboardRequest request, DataRequest dataRequest, List<ParamPojoReq> paramPojoReqList, Map<String, SensorParamView> paramMap, List<MetDashboardData> intData, String tableCode) {
        dataRequest.setInterval(request.getInterval());
        dataRequest.setParamReqList(paramPojoReqList);
        dataRequest.setStationIds(String.valueOf(request.getStationId()));
        dataRequest.setTableCode(tableCode);
        String dashResponse = circuitBreakerWrapper.loadDashboardDataForMET(dataRequest);
        if (!StringUtils.isBlank(dashResponse)) {
            JSONArray jsonArray = new JSONArray(dashResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray row = jsonArray.getJSONArray(i);
                if (paramMap.containsKey(row.getString(0))) {
                    SensorParamView spvObj = paramMap.get(row.getString(0));
                    MetDashboardData metData = new MetDashboardData();
                    metData.setParamId(spvObj.getParamId());
                    metData.setParamName(spvObj.getParameterDisplayName());
                    metData.setAvg(row.getString(1));
                    metData.setMin(row.getString(2));
                    metData.setMax(row.getString(4));
                    metData.setUnit(spvObj.getUnitSymbol());
                    intData.add(metData);
                }
            }
        } else {
            paramMap.forEach((s, sensorParamView) -> {
                MetDashboardData metData = new MetDashboardData();
                metData.setParamId(sensorParamView.getParamId());
                metData.setParamName(sensorParamView.getParamName());
                metData.setAvg("-");
                metData.setMin("-");
                metData.setMax("-");
                intData.add(metData);
            });
        }
    }

    @Override
    public List<CentricDashboardData> loadCentricDashboard(CentricDashboardRequest request) {
        log.info("loadCentricDashboard entry ");
        Map<String, SensorParamView> paramMap = new LinkedHashMap<>();
        List<CentricDashboardData> responseList = new ArrayList<>();
        CentricDashboardReq dataReq = new CentricDashboardReq();
        String sensorTableCode = "";
        List<Integer> stList = new ArrayList<>();
        try {
            if (request.getLoggedIn() == null)
                throw new TemsBadRequestException(USER_NOT_FOUND);
            if (request.getStationIdList().isEmpty())
                throw new TemsBadRequestException(STN_NOT_FOUND);
            if (request.getParamId() == null)
                throw new TemsBadRequestException(PARAM_NOT_FOUND);

            SensorParamView paramView = sensorParamViewRepository.findByParameterId(request.getParamId());
            if (paramView != null) {
                UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(Integer.valueOf(request.getLoggedIn()), paramView.getParamId());
                sensorTableCode = paramView.getSensorStatusTableName();
                dataReq.setParam(paramView);
                dataReq.setSensorTableCode(sensorTableCode);
                dataReq.setStationIdList(request.getStationIdList());
                List<CentricDataResponse> dataResponseList = circuitBreakerWrapper.loadCentricDashboardData(dataReq);
                dataResponseList.forEach(data -> {
                    stList.add(data.getStationId());
                    StationResponse station = stationRepo.findStationById(data.getStationId());
                    CentricDashboardData dashboardData = new CentricDashboardData();
                    if (data.getData() != null) {
                        MetricsPojo metricsPojo = commonService.getDataMetrics(upu, paramView, String.valueOf(data.getData()));
                        int roundOffNum = paramView.getDisplayRoundTo() != null ? paramView.getDisplayRoundTo() : 1;
                        String pattern = paramView.getDisplayRoundTo() != null && paramView.getDisplayRoundTo() == 0
                                ? "#" : "#." + "#".repeat(roundOffNum);
                        DecimalFormat df = new DecimalFormat(pattern);
                        String dataValue = df.format(metricsPojo.getData());
                        dashboardData.setData(dataValue);
                        dashboardData.setWarn(metricsPojo.getWarn());
                        dashboardData.setDanger(metricsPojo.getDanger());
                    } else
                        dashboardData.setData("-");

                    dashboardData.setWarnOp(paramView.getWarnOperation());
                    dashboardData.setDangerOp(paramView.getDangerOperation());
                    dashboardData.setDate(data.getDate());
                    dashboardData.setParamName(paramView.getParamName());
                    dashboardData.setStationId(station.getStationId());
                    dashboardData.setStationName(station.getStationName());
                    dashboardData.setUnit(upu!= null ? upu.getUnitSymbol() : paramView.getUnitSymbol());
                    responseList.add(dashboardData);
                });

                //compare station lists
                List<Integer> stations = request.getStationIdList().stream()
                        .filter(e -> !stList.contains(e)).toList();
                stations.forEach(st -> {
                    StationResponse station = stationRepo.findStationById(st);
                    CentricDashboardData dashboardData = new CentricDashboardData();
                    dashboardData.setData("-");
                    dashboardData.setWarnOp(paramView.getWarnOperation());
                    dashboardData.setDangerOp(paramView.getDangerOperation());
                    dashboardData.setDate("");
                    dashboardData.setParamName(paramView.getParamName());
                    dashboardData.setStationId(station.getStationId());
                    dashboardData.setStationName(station.getStationName());
                    dashboardData.setUnit(upu!= null ? upu.getUnitSymbol() : paramView.getUnitSymbol());
                    responseList.add(dashboardData);
                });
            }
        } catch (Exception e) {
            log.info("loadCentricDashboard failed exception  " + e.getMessage());
            if (e instanceof TemsBadRequestException)
                throw e;
            throw new TemsCustomException("Load Centric dashboard failed");
        }
        log.info("loadCentricDashboard exit ");
        return responseList;
    }

    private ParamPojoReq getParamPojoReq(String paramName, Integer user, Map<String, SensorParamView> paramMap) {
        SensorParamView paramView = sensorParamViewRepository.findAllByParamName(paramName, user);
        if (paramView != null) {
            UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(user, paramView.getParamId());
            paramMap.put(paramView.getDataParamName(), paramView);
            if (upu != null && upu.getOperation() != null) {
                paramView.setUnitSymbol(upu.getUnitSymbol());
                return new ParamPojoReq(
                        paramView.getDataParamName(),
                        upu.getOperation(),
                        upu.getCalculatedValue(),
                        paramView.getDisplayRoundTo() != null ? paramView.getDisplayRoundTo() : 2,
                        paramView.getParamName().equalsIgnoreCase(WINDGUSTSPEED) | paramView.getParamName().equalsIgnoreCase(WINDGUSTDIRECTION) | paramView.getParamName().equalsIgnoreCase(VISIBILITY)
                );
            }
            return new ParamPojoReq(
                    paramView.getDataParamName(),
                    "*",
                    BigDecimal.ONE,
                    paramView.getDisplayRoundTo() != null ? paramView.getDisplayRoundTo() : 2,
                    paramView.getParamName().equalsIgnoreCase(WINDGUSTSPEED) | paramView.getParamName().equalsIgnoreCase(WINDGUSTDIRECTION) | paramView.getParamName().equalsIgnoreCase(VISIBILITY)
            );
        }
        return new ParamPojoReq();
    }

    private void replaceNulls(WindDashboardData data) {
        if (data != null) {
            data.setWindSpeedMin(data.getWindSpeedMin() == null ? "-" : data.getWindSpeedMin());
            data.setWindSpeedMax(data.getWindSpeedMax() == null ? "-" : data.getWindSpeedMax());
            data.setWindSpeedAvg(data.getWindSpeedAvg() == null ? "-" : data.getWindSpeedAvg());
            data.setWindDirectionMin(data.getWindDirectionMin() == null ? "-" : data.getWindDirectionMin());
            data.setWindDirectionMax(data.getWindDirectionMax() == null ? "-" : data.getWindDirectionMax());
            data.setWindDirectionAvg(data.getWindDirectionAvg() == null ? "-" : data.getWindDirectionAvg());
            data.setWindGustSpeedAvg(data.getWindGustSpeedAvg() == null ? "-" : data.getWindGustSpeedAvg());
            data.setWindGustDirectionAvg(data.getWindGustDirectionAvg() == null ? "-" : data.getWindGustDirectionAvg());
        }
    }

}
