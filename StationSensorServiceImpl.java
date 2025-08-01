package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.entity.StationData;
import com.tridel.tems_sensor_service.entity.master.StationLegends;
import com.tridel.tems_sensor_service.entity.master.StationType;
import com.tridel.tems_sensor_service.exception.TemsBadRequestException;
import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.model.SensorParamViewReq;
import com.tridel.tems_sensor_service.model.UserSensorParamUnitsPojo;
import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.request.StationDtlRequest;
import com.tridel.tems_sensor_service.model.request.StationRequest;
import com.tridel.tems_sensor_service.model.response.*;
import com.tridel.tems_sensor_service.repository.*;
import com.tridel.tems_sensor_service.util.CommonUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.tridel.tems_sensor_service.util.CommonUtil.*;

@Service
@Slf4j
public class StationSensorServiceImpl implements StationSensorService{
    StationRepository stationRepo;
    SensorParamViewRepository sensorParamViewRepository;
    CommonService commonService;
    UserSensorParameterUnitRepository upuRepo;
    ParameterUnitRepository parameterUnitRepository;
    FileService fileSvc;
    UserStationSensorParamRepository userStationSensorParamRepo;
    DataServiceCircuitBreakerWrapper dataServiceCircuitBreakerWrapper;
    StationTypeRepository stationTypeRepo;
    StationLegendRepository stationLegendRepo;

    @Value("${station.image.path}")
    String imgPath;
    StationSensorServiceImpl(StationRepository stationRepo,SensorParamViewRepository sensorParamViewRepository,
                             CommonService commonService,ParameterUnitRepository parameterUnitRepository,
                             StationTypeRepository stationTypeRepo,
                             UserSensorParameterUnitRepository upuRepo, StationLegendRepository stationLegendRepo,
                             FileService fileSvc, DataServiceCircuitBreakerWrapper dataServiceCircuitBreakerWrapper,
                             UserStationSensorParamRepository userStationSensorParamRepo){
        this.stationRepo = stationRepo;
        this.sensorParamViewRepository = sensorParamViewRepository;
        this.commonService = commonService;
        this.upuRepo= upuRepo;
        this.parameterUnitRepository = parameterUnitRepository;
        this.fileSvc = fileSvc;
        this.stationLegendRepo = stationLegendRepo;
        this.stationTypeRepo = stationTypeRepo;
        this.userStationSensorParamRepo = userStationSensorParamRepo;
        this.dataServiceCircuitBreakerWrapper = dataServiceCircuitBreakerWrapper;
    }
    @Override
    public List<StationResponse> getAllStations(Request request) {
        List<StationResponse> stationResponseList = new ArrayList<>();
        log.info("getAllStations entry ");
        try {
            if (request.getLoggedIn() == null)
                throw new TemsBadRequestException(USER_NOT_FOUND);
            stationResponseList  = stationRepo.findAllByStationUser(request.getLoggedIn());
        }catch (ExpiredJwtException e) {
            throw new TemsCustomException(e.getMessage());
        } catch (Exception ex) {
            log.info("getAllStations exception"+ ex.getMessage());
            if(ex instanceof TemsBadRequestException || ex instanceof TemsCustomException)
                throw ex;
            throw new TemsCustomException(ex.getMessage());
        }
        log.info("getAllStations exit ");
        return stationResponseList;
    }
    @Override
    public List<StationResponse> getAllStationByUserAndType(Request request) {
        List<StationResponse> stationResponseList = new ArrayList<>();
        log.info("getAllStationByUserAndType entry ");
        try {
            if (request.getLoggedIn() == null)
                throw new TemsBadRequestException(USER_NOT_FOUND);
            if (request.getType() == null)
                throw new TemsBadRequestException(STATION_TYPE_NOT_FOUND);
            stationResponseList  = stationRepo.findAllStationByUserAndType(request.getLoggedIn(),request.getType());
        }catch (ExpiredJwtException e) {
            throw new TemsCustomException(e.getMessage());
        } catch (Exception ex) {
            throw new TemsBadRequestException(ex.getMessage());
        }
        log.info("getAllStationByUserAndType exit ");
        return stationResponseList;
    }
    @Override
    public List<StationResponse> getAllStationByUserAndParam(Request request) {
        List<StationResponse> stationResponseList = new ArrayList<>();
        log.info("getAllStationByUserAndParam entry ");
        try {
            if (request.getLoggedIn() == null)
                throw new TemsBadRequestException(USER_NOT_FOUND);
            if (request.getParamId() == null)
                throw new TemsBadRequestException(PARAM_NOT_FOUND);
            stationResponseList  = stationRepo.findStationDtlsByUserAndParam(request.getLoggedIn(),request.getParamId());
        }catch (ExpiredJwtException e) {
            throw new TemsCustomException(e.getMessage());
        } catch (Exception ex) {
            throw new TemsBadRequestException(ex.getMessage());
        }
        log.info("getAllStationByUserAndParam exit ");
        return stationResponseList;
    }

    @Override
    public StationResponse getStationDetailsById(Request request) {
        StationResponse stationResponse = null;
        log.info("getStationDetailsById entry ");
        try {
            if (request.getLoggedIn() == null)
                throw new TemsBadRequestException(USER_NOT_FOUND);
            if (request.getId() == null)
                throw new TemsBadRequestException(STN_NOT_FOUND);
            Optional<StationData> stationData = stationRepo.findById(request.getId());
            if (stationData.isPresent()) {
                StationData station = stationData.get();
                List<StationType> stTypeList = stationTypeRepo.findAll();
                Map<Integer, StationType> stTypesMap = stTypeList.stream()
                        .collect(Collectors.toMap(StationType::getStationTypeId, type -> type));
                if(!stTypesMap.isEmpty()) {
                    stationResponse = new StationResponse(station);
                    stationResponse.setStationTypeName(stTypesMap.getOrDefault(station.getStationType().getStationTypeId(), new StationType()).getStationTypeName());
                }else{
                    log.info("Master service is down : Station type fetch");
                    throw new TemsCustomException("Failed to load station type");
                }
            }
        }catch (ExpiredJwtException e) {
            throw new TemsCustomException(e.getMessage());
        } catch (Exception ex) {
            log.info("getStationDetailsById exception "+ex.getMessage());
            if(ex instanceof TemsCustomException)
                throw ex;
            throw new TemsBadRequestException(ex.getMessage());
        }
        log.info("getStationDetailsById exit ");
        return stationResponse;
    }
//    @CircuitBreaker(name = "dataServiceCB", fallbackMethod = "fallbackHeaderData")
    @Override
    public List<StationResponse> listAllStationData(Integer loggedIn) {
        List<StationResponse> response = new ArrayList<>();
        log.info("listAllStationData entry ");
        StationDtlRequest req = new StationDtlRequest();
        if (loggedIn == null)
            throw new TemsBadRequestException(USER_NOT_FOUND);
        try {
            List<StationLegends> legendsList = stationLegendRepo.findAll();
            Map<String, StationLegends> legendsMap = legendsList.stream()
                    .collect(Collectors.toMap(StationLegends::getLegendName, legend -> legend));

            List<StationType> stTypeList = stationTypeRepo.findAll();
            Map<Integer, StationType> stTypesMap = stTypeList.stream()
                    .collect(Collectors.toMap(StationType::getStationTypeId, type -> type));

            List<StationData> stationIds = stationRepo.findAllStationsByUserForHome(loggedIn);
            List<Integer> stationIdList = stationIds.stream()
                    .map(StationData::getId)
                    .toList();
            if(!stationIdList.isEmpty()) {
                Map<String, SensorParamView> sensorParamViewMap = sensorParamViewRepository.findDtlsOfParamList().stream().collect(Collectors.toMap(SensorParamView::getDataParamName, sensorParam -> sensorParam));
                req.setStationIds(stationIdList);
                req.setParams(String.join(",", sensorParamViewMap.keySet()));
                Map<String, List<GenericResponse>> stationParamMap = new HashMap<>();
                stationParamMap = dataServiceCircuitBreakerWrapper.getHeaderDataWithCircuitBreaker(req);
                Map<String, List<GenericResponse>> finalStationParamMap = stationParamMap;
                stationIds.forEach(station -> {
                    log.info("listAllStationData fetching details for station " + station.getStationName());
                    StationResponse thisStnResp = new StationResponse(station);
                    thisStnResp.setBuoyWatchCircleDanger(String.valueOf(station.getIsBuoyType() ? station.getBuoyWatchCircleDanger() : ""));
                    thisStnResp.setBuoyWatchCircleWarn(String.valueOf(station.getIsBuoyType() ? station.getBuoyWatchCircleWarn() : ""));

                    if (finalStationParamMap.containsKey(station.getId().toString())) {
                        List<GenericResponse> thisStationDtlsList = finalStationParamMap.get(station.getId().toString());
                        String paramDateStr = thisStationDtlsList.getLast().getCVal();
                        LocalDateTime paramDate = commonService.getDateAfterSecCheck(paramDateStr);
                        thisStationDtlsList.forEach(param -> {
                            if (sensorParamViewMap.containsKey(param.getCName())) {
                                SensorParamView paramView = sensorParamViewMap.get(param.getCName());
                                if (station.getMobilityFlag()) {
                                    thisStnResp.setLatitude(paramView.getParamName().contains(LATITUDE) ? Double.valueOf(param.getCVal()) : thisStnResp.getLatitude());
                                    thisStnResp.setLongitude(paramView.getParamName().contains(LONGITUDE) ? Double.valueOf(param.getCVal()) : thisStnResp.getLongitude());
                                }
                                if (station.getMaintenanceFlag())
                                    thisStnResp.setLegend(MAINTENACELEGEND);
                                else {
                                    setLegendBasedOnTime(paramDate, station, thisStnResp, legendsMap, stTypesMap);
                                }
                                if (paramView.getParamName().equalsIgnoreCase(SWITCH_STATE)) {
                                    if (param.getCVal().equalsIgnoreCase(IS_TRUE)) {
                                        thisStnResp.setSwitchState(true);
                                    }
                                }
                            }
                        });
                    } else {
                        thisStnResp.setLegend(OFFLINELEGEND);
                    }
                    response.add(thisStnResp);
                });
            }
        }catch(Exception ex){
            log.info("listAllStationData exception "+ex.getMessage());
            System.out.println("listAllStationData exception "+ex.getMessage());
            if(ex instanceof TemsBadRequestException)
                throw ex;
            throw new TemsCustomException(LOAD_STATIONS_FAILED);
        }
        log.info("listAllStationData exit ");
        return response;

    }
    public void setLegendBasedOnTime(LocalDateTime paramDate, StationData station, StationResponse thisStnResp,
                                     Map<String, StationLegends> legendsMap, Map<Integer, StationType> stTypesMap) {
        if (!paramDate.toLocalDate().equals(LocalDate.now())) {
            thisStnResp.setLegend(OFFLINELEGEND);
            return;
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        Duration duration = Duration.between(paramDate, currentDateTime);
        long durMinutes = duration.toMinutes();

        String legend = switch ((int) (durMinutes / 15)) {
            case 0 -> {
                System.out.println("Station type "+station.getStationType());
                StationLegends legendsPojo = legendsMap.get(stTypesMap.get(station.getStationType().getStationTypeId()).getStationTypeName());
                yield legendsPojo!=null?legendsPojo.getLegendImgName():"";
            }
            case 1 ->  MAGENTALEGEND;

            default -> DANGERLEGEND;

        };
        thisStnResp.setLegend(legend);
    }


    @Override
    public HomeSensorDataResponse getSensorDetailsForHome(Request request) {
        StationDtlRequest req = new StationDtlRequest();
        HomeSensorDataResponse response = new HomeSensorDataResponse();
        log.info("getSensorDetailsForHome entry ");
        if (request.getLoggedIn() == null)
            throw new TemsBadRequestException(USER_NOT_FOUND);
        if (request.getId() == null)
            throw new TemsBadRequestException(STN_NOT_FOUND);
        try{
            Optional<StationData> stationData = stationRepo.findById(request.getId());
            if (stationData.isPresent()) {
                StationData station = stationData.get();
//                Map<Integer, StationTypePojo> stTypesMap = commonService.getStTypesFromCache();
                List<StationType> stTypeList = stationTypeRepo.findAll();
                Map<Integer, StationType> stTypesMap = stTypeList.stream()
                        .collect(Collectors.toMap(StationType::getStationTypeId, type -> type));
                /*Get Header Status Details for the station*/
                Map<String, SensorParamView> sensorParamFViewMap = sensorParamViewRepository.findDtlsOfParamList().stream().collect(Collectors.toMap(SensorParamView::getDataParamName, sensorParam -> sensorParam));
                req.setStationIds(List.of(station.getId()));
                req.setParams(String.join(",", sensorParamFViewMap.keySet()));
                response = getStationDataForHome(req, station, response, stationData.get(), stTypesMap);
                getSensorParamDetailsBySensorForHome(request, station, response);

            }else
                throw new TemsBadRequestException(STN_NOT_FOUND);

        }catch(Exception ex){
            log.info("getSensorDetailsForHome exception "+ex.getMessage());
            if(ex instanceof TemsBadRequestException)
                throw ex;
            throw new TemsCustomException(FETCH_SENSOR_FAILED);
        }
        log.info("getSensorDetailsForHome exit ");
        return response;
    }

    private HomeSensorDataResponse getStationDataForHome(StationDtlRequest req, StationData station, HomeSensorDataResponse response, StationData stationData, Map<Integer, StationType> stTypesMap) {
        Map<String, List<GenericResponse>> stationHeaderParamMap  = dataServiceCircuitBreakerWrapper.getHeaderDataWithCircuitBreaker(req);
        if (stationHeaderParamMap.containsKey(station.getId().toString())) {
            List<GenericResponse> thisStationDtlsList = stationHeaderParamMap.get(station.getId().toString());
            String paramDateStr = thisStationDtlsList.getLast().getCVal();
            LocalDateTime paramDate = commonService.getDateAfterSecCheck(paramDateStr);
            response = new HomeSensorDataResponse(station);
            response.setStationStatus(false);
            response.setLatestTime(paramDate.format(formatter2));
            if (stationData.getIsBuoyType()) {
                Map<String, SensorParamView> sensorParamViewMap = sensorParamViewRepository.findDtlsOfParamList().stream().collect(Collectors.toMap(SensorParamView::getParamName, sensorParam -> sensorParam));
                Map<String, String> cNameToCVal = thisStationDtlsList.stream()
                        .filter(stationParamDtl -> sensorParamViewMap.get("GPS Latitude").getDataParamName().equals(stationParamDtl.getCName()) ||
                                sensorParamViewMap.get("GPS Longitude").getDataParamName().equals(stationParamDtl.getCName()))
                        .collect(Collectors.toMap(GenericResponse::getCName, GenericResponse::getCVal));
                response.setLatestLat(cNameToCVal.get(sensorParamViewMap.get("GPS Latitude").getDataParamName()));
                response.setLatestLong(cNameToCVal.get(sensorParamViewMap.get("GPS Longitude").getDataParamName()));
            }
            response.setStationType(stTypesMap.get(station.getStationType().getStationTypeId()).getStationTypeName());
            response.setStationStatus(paramDate.toLocalDate().equals(LocalDate.now()));
        }
        return response;
    }

    private void getSensorParamDetailsBySensorForHome(Request request, StationData station, HomeSensorDataResponse response) {
        List<SensorDataResponse> sensorDataList = new ArrayList<>();
        log.info("getSensorParamDetailsBySensorForHome entry");
        try {
            List<SensorParamView> paramList = sensorParamViewRepository.findAllParametersByStationForHome(station.getId(), request.getLoggedIn());
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
                                        return new SensorParamViewReq(station.getId(), records.getFirst().getSensorCode(), concatenatedParams, sensorTableCode, false);
                                    }
                            )
                    ));
            resultMap.forEach((sensor, sensorParamViewReq) -> {
                SensorDataResponse sensorDataResponse = new SensorDataResponse();
                List<SensorParamView> spList = new ArrayList<>();
                Map<String, SensorParamView> sensorParamMap = sensorParamViewRepository
                        .findAllParametersByStationAndSensorForHome(station.getId(), sensor, request.getLoggedIn())
                        .stream()
                        .collect(Collectors.toMap(
                                SensorParamView::getDataParamName,
                                sensorParam -> sensorParam,
                                (existing, replacement) -> existing,
                                LinkedHashMap::new
                        ));
                if (response!= null && response.getLatestTime()!=null) {
                    Map<String, GenericResponse> cNameValPairsMap = dataServiceCircuitBreakerWrapper.getSensorDataWithCircuitBreaker(sensorParamViewReq)
                            .stream().collect(Collectors.toMap(GenericResponse::getCName, cell -> cell));
                    for (Map.Entry<String, SensorParamView> entry : sensorParamMap.entrySet()) {
                        SensorParamView spm = entry.getValue();
                        UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(request.getLoggedIn(), spm.getParamId());
                        if (cNameValPairsMap.containsKey(entry.getKey())) {
                            GenericResponse param = cNameValPairsMap.get(entry.getKey());
                            String data = null;
//                            double dat = 0.00;
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
                        spm.setUnitSymbol(upu != null ? upu.getUnitSymbol() : spm.getUnitSymbol());
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
            log.info("getSensorParamDetailsBySensorForHome exit");
        }catch(Exception e){
            log.info("getSensorParamDetailsBySensorForHome exception"+e.getMessage());
            throw new TemsCustomException("getSensorParamDetailsBySensorForHome exception "+e.getMessage());
        }
    }
//    @CircuitBreaker(name = "dataServiceCB", fallbackMethod = "fallbackHeaderData")
    @Override
    public List<StationWeatherDataResponse> loadAllStationWeatherData(Request request) {
        SensorParamViewReq sensorParamViewReq = new SensorParamViewReq();
        StationDtlRequest req = new StationDtlRequest();
        List<StationWeatherDataResponse> weatherList = new ArrayList<>();
        log.info("loadAllStationWeatherData entry");
        try {
            List<StationData> stationIds = stationRepo.findAllStationsByUserForHome(request.getLoggedIn());
            List<SensorParamView> sensorParamViewList = sensorParamViewRepository.findAllMETDataParamsForHome(request.getLoggedIn());
            StringJoiner paramCodesJoiner = new StringJoiner(",");
            Map<String, SensorParamView> summaryParametersMap = new HashMap<>();
            sensorParamViewList.forEach(sensorParam -> {
                String paramCode = sensorParam.getDataParamName();
                paramCodesJoiner.add(paramCode);
                summaryParametersMap.put(paramCode, sensorParam);
            });
            sensorParamViewReq.setParams(paramCodesJoiner.toString());
            sensorParamViewReq.setSensorTableCode("tems_sensor_status_c");
            stationIds.forEach(station -> {
                StationWeatherDataResponse weather = new StationWeatherDataResponse("///");
                weather.setLatitude(String.valueOf(station.getLatitude()));
                weather.setLongitude(String.valueOf(station.getLongitude()));
//                LocalDate parsedDate = null;
//                LocalDate today = LocalDate.now();
                if (station.getMobilityFlag()) {
                    Map<String, SensorParamView> sensorParamViewMap = sensorParamViewRepository.findDtlsOfParamList().stream().collect(Collectors.toMap(SensorParamView::getDataParamName, sensorParam -> sensorParam));
                    req.setStationIds(List.of(station.getId()));
                    req.setParams(String.join(",", sensorParamViewMap.keySet()));
                    Map<String, List<GenericResponse>> stationParamMap = new HashMap<>();

                    stationParamMap = dataServiceCircuitBreakerWrapper.getHeaderDataWithCircuitBreaker(req);
                    if (stationParamMap.containsKey(station.getId().toString())) {
                        List<GenericResponse> thisStationDtlsList = stationParamMap.get(station.getId().toString());
                        Map<String, SensorParamView> sensorParamMap = sensorParamViewRepository.findDtlsOfParamList().stream().collect(Collectors.toMap(SensorParamView::getParamName, sensorParam -> sensorParam));
                        Map<String, String> cNameToCVal = thisStationDtlsList.stream()
                                .filter(stationParamDtl -> sensorParamMap.get("GPS Latitude").getParamName().equals(stationParamDtl.getCName()) ||
                                        sensorParamMap.get("GPS Longitude").getParamName().equals(stationParamDtl.getCName())
                                        )
                                .collect(Collectors.toMap(GenericResponse::getCName, GenericResponse::getCVal));
                        weather.setLatitude(cNameToCVal.get(sensorParamMap.get("GPS Latitude").getParamName())!=null?cNameToCVal.get(sensorParamMap.get("GPS Latitude").getParamName()):String.valueOf(station.getLatitude()));
                        weather.setLongitude(cNameToCVal.get(sensorParamMap.get("GPS Longitude").getParamName())!=null?cNameToCVal.get(sensorParamMap.get("GPS Longitude").getParamName()):String.valueOf(station.getLongitude()));
                    }
                }
                sensorParamViewReq.setStationId(station.getId());
                sensorParamViewReq.setWeather(true);
                    List<GenericResponse> cNameValPairsList = dataServiceCircuitBreakerWrapper.getSensorDataWithCircuitBreaker(sensorParamViewReq);
                    cNameValPairsList.forEach(param -> {
                        if (summaryParametersMap.containsKey(param.getCName())) {
                            SensorParamView spm = summaryParametersMap.get(param.getCName());
                            Optional.ofNullable(spm).ifPresent(s -> {
                                double roundingFactor = Math.pow(10, (s.getDisplayRoundTo() != null ? s.getDisplayRoundTo() : 1));
                                String paramName = spm.getParamName();
                                String data = param.getCVal() != null ? String.format("%.0f", Math.round(Double.parseDouble(param.getCVal()) * roundingFactor) / roundingFactor) : "///";
                                switch (paramName.toLowerCase()) {
                                    case "air temperature" -> weather.setAirTemperature(data);
                                    case "relative humidity" -> weather.setRelativeHumidity(data);
                                    case "wind speed" -> weather.setWindSpeed(data);
                                    case "wind direction" -> weather.setWindDirection(data);
                                    case "rainfall" -> weather.setRainfall(data);
                                    case "visibility" -> weather.setVisibility(data);
                                    case "qnh" -> weather.setQnh(data);
                                    default -> {
                                    }
                                }
                            });
                        } else {
                            weather.setLatestTime(param.getCVal() != null ? param.getCVal() : "");
                        }
                    });

                weather.setStationId(station.getId());
                weather.setStationName(station.getStationName());
                weather.setMobilityFlag(station.getMobilityFlag());
                weather.setStationCode(Optional.ofNullable(station.getStationCode()).orElse(""));
                weather.setDisplayName(Optional.ofNullable(station.getStationName())
                        .filter(name -> station.getStationCode() != null)
                        .map(name -> name.trim() + "-" + station.getStationCode().split("WMO")[1])
                        .orElse(""));
                weatherList.add(weather);
            });
        }catch(Exception e){
            log.info("loadAllStationWeatherData exception"+e.getMessage());
            throw new TemsCustomException(e.getMessage());
        }
        log.info("loadAllStationWeatherData exit");
        return weatherList;
    }

    @Override
    public String getCommunicationDetails(Request request) {
        JSONArray array = new JSONArray();
        try {
            StationDtlRequest stationDtlRequest = new StationDtlRequest();
            if (request.getLoggedIn() == null)
                throw new TemsBadRequestException("User not found");
            List<StationResponse> stationDataList = stationRepo.findAllByStationUser(request.getLoggedIn());
            if (!stationDataList.isEmpty()) {
                List<Integer> stationIds = stationDataList.stream().map(StationResponse::getStationId).toList();
                SensorParamView param = sensorParamViewRepository.findByParameterName(CommonUtil.PARAMNAME);
                stationDtlRequest.setStationIds(stationIds);
                stationDtlRequest.setParams(param.getDataParamName());
                Map<Integer, CommunicationPojo> stationCommMap = dataServiceCircuitBreakerWrapper.getStationCommunicationDetails(stationDtlRequest);
                for (StationResponse stationData : stationDataList) {
                    StationCommunicationResponse response = new StationCommunicationResponse(stationData);
                    if (stationCommMap != null && stationCommMap.containsKey(stationData.getStationId())) {
                        CommunicationPojo communicationPojo = stationCommMap.get(stationData.getStationId());
                        String todayDate = dateFormat5.format(new Date());
                        String parameterDate = dateFormat5.format(communicationPojo.getParameterDatetime());
                        response.setActiveFlag(todayDate.equals(parameterDate));
                        response.setLatestDateTime(communicationPojo.getParameterDatetime() == null ? "-" :
                                dateFormat4.format(communicationPojo.getParameterDatetime()));
                        response.setBatteryVoltage(communicationPojo.getBatteryVoltage());
                        response.setBatteryMin(String.valueOf(param.getMin()));
                        response.setBatteryMax(String.valueOf(param.getMax()));
                        response.setBatteryUnit(param.getUnitSymbol());
                    } else {
                        response.setActiveFlag(false);
                        response.setLatestDateTime("-");
                        response.setBatteryVoltage("");
                        response.setBatteryMin("");
                        response.setBatteryMax("");
                        response.setBatteryUnit("");
                    }
                    array.put(new JSONObject(response));
                }
            }
        }catch(Exception e){
            log.info("Load Communication failed with exception "+e.getMessage());
            throw new TemsCustomException("Load Communication details failed");
        }
        return array.toString();
    }
    @Override
    public String generateLog(Request req) {
        List<SensorParamViewReq> sensorList = userStationSensorParamRepo.findAllSensorByStation(req.getId(), req.getLoggedIn());
//        Map<String, SensorParamViewReq> resultMap = paramList.stream()
//                .collect(Collectors.groupingBy(
//                        SensorParamView::getSensorCode,
//                        Collectors.collectingAndThen(
//                                Collectors.toList(),
//                                records -> {
//                                    String concatenatedParams = records.stream()
//                                            .map(SensorParamView::getDataParamName)
//                                            .collect(Collectors.joining(","));
//                                    String sensorTableCode = records.get(0).getSensorTableName();
//                                    return new SensorParamViewReq(req.getId(), records.getFirst().getSensorName(), concatenatedParams, sensorTableCode, false);
//                                }
//                        )
//                ));
//        List<SensorParamViewReq> sensorList = new ArrayList<>(resultMap.values());
        return dataServiceCircuitBreakerWrapper.generateLog(sensorList);
    }
    @Override
    public String updateStation(StationRequest stationRequest)  {
        var obj = new JSONObject();
        try {
            if (StringUtils.isBlank(stationRequest.getLoggedIn())) {
                throw new TemsBadRequestException("User not found");
            }
            Optional<StationData> stationOpt = stationRepo.findById(stationRequest.getStationId());
            stationOpt.ifPresent(station -> {
                station.setStationName(stationRequest.getStationName().toUpperCase());
                station.setLatitude(stationRequest.getLatitude());
                station.setLongitude(stationRequest.getLongitude());
                station.setLocationDetails(stationRequest.getLocationDetails());
                station.setMaintenanceFlag(stationRequest.getMaintenanceFlag());
                station.setMaintenanceStartDate(parseDate(stationRequest.getMaintenanceStartDate()));
                station.setMaintenanceEndDate(parseDate(stationRequest.getMaintenanceEndDate()));

                station.setNoSignalFlag(stationRequest.getNoSignalFlag());
                if (stationRequest.getImgChanged()) {
                    double size = stationRequest.getStationImg().getSize()/(1024.0 * 1024.0);
                    if(size > 10)
                        throw new TemsBadRequestException("Maximum image size can be 10mb");
                    deleteOldImage(station.getStationImg());
                    String fileName = fileSvc.saveStationImage(stationRequest.getStationImg(), station.getStationCode());
                    if (StringUtils.isNotBlank(fileName)) {
                        station.setStationImg(getFileName(fileName));
                    }
                }
                station.setIsBuoyType(stationRequest.getIsBuoyFlag());
                if (stationRequest.getIsBuoyFlag()) {
                    station.setBuoyWatchCircleWarn(stationRequest.getBuoyWatchCircleWarn());
                    station.setBuoyWatchCircleDanger(stationRequest.getBuoyWatchCircleDanger());
                }
                stationRepo.save(station);
                obj.put(CommonUtil.MSG, CommonUtil.SUCCESS);
                commonService.saveAuditDetails(Integer.parseInt(stationRequest.getLoggedIn()), "Updated", "",
                        "Station " + station.getStationName(), "Station Management", new Date());
            });
        }catch(Exception e){
            log.info("Update station failed exception "+e.getMessage());
            throw new TemsCustomException("Update station failed");
        }
        return obj.toString();
    }

    @Override
    public List<Integer> getAllUserStations(Request request) {
        if(request.getId() == null)
            throw new TemsBadRequestException(USER_NOT_FOUND);
        return stationRepo.findAllStationForUser(request.getId());
    }

    private Date parseDate(String dateStr) {
        try {
            return StringUtils.isNotBlank(dateStr) ? dateFormat1.parse(dateStr.replace("T", " ")) : null;
        }catch(Exception e){
            log.info("Update station failed exception "+e.getMessage());
            throw new TemsCustomException("Update station failed");
        }
    }

    private void deleteOldImage(String stationImg) {
        if (StringUtils.isNotBlank(stationImg)) {
            File file = new File(imgPath+stationImg);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private String getFileName(String fileName) {
        return fileName.contains(":") ? fileName.split(":")[1] : fileName;
    }
}
