package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.entity.Sensors;
import com.tridel.tems_sensor_service.entity.StationData;
import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.model.UserSensorParamUnitsPojo;
import com.tridel.tems_sensor_service.model.WindRoseRange;
import com.tridel.tems_sensor_service.model.request.*;
import com.tridel.tems_sensor_service.model.response.SensorParamView;
import com.tridel.tems_sensor_service.model.response.StationResponse;
import com.tridel.tems_sensor_service.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService{
    StationRepository stationRepository;
    SensorParamViewRepository sensorParamViewRepository;
    UserSensorParameterUnitRepository upuRepo;
    SensorRepository sensorRepository;
    WindRoseRangeRepository wrRepo;
    DataServiceCircuitBreakerWrapper circuitBreakerWrapper;
    StatisticsServiceImpl(StationRepository stationRepository,SensorParamViewRepository sensorParamViewRepository,
                          UserSensorParameterUnitRepository upuRepo,
                          SensorRepository sensorRepository,WindRoseRangeRepository wrRepo,
                          DataServiceCircuitBreakerWrapper circuitBreakerWrapper){
        this.stationRepository = stationRepository;
        this.upuRepo = upuRepo;
        this.sensorParamViewRepository = sensorParamViewRepository;
        this.sensorRepository = sensorRepository;
        this.wrRepo = wrRepo;
        this.circuitBreakerWrapper = circuitBreakerWrapper;
    }
    @Override
    public String loadParameterDataGraph(Request request) {
        log.info("loadParameterDataGraph entry ");
        String response = null;
        StatisticsDataRequest statisticsDataRequest = new StatisticsDataRequest();
        if (request.getId() == null)
            throw new TemsCustomException("Station Not Found");
        if (request.getParamId() == null)
            throw new TemsCustomException("Parameter not found");
        Optional<StationData> stationData = stationRepository.findById(request.getId());
        if (stationData.isPresent()) {
            SensorParamView parameter = sensorParamViewRepository.findParameterDtlsById(request.getId(),request.getLoggedIn(),request.getParamId());
            UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(request.getLoggedIn(), request.getParamId());
            Double min = parameter.getMin()!=null?parameter.getMin():10;
            parameter.setGraphYAxisMin(min);
            if (upu != null && upu.getOperation() != null) {
                parameter.setUnitSymbol(upu.getUnitSymbol());
                parameter.setOperation(upu.getOperation());
                parameter.setCalculatedValue(String.valueOf(upu.getCalculatedValue()));
                parameter.setGraphYAxisMin(upu.getGraphYAxisMin()!=null?upu.getGraphYAxisMin():min);
            }
            parameter.setDisplayRoundTo(parameter.getDisplayRoundTo() != null?parameter.getDisplayRoundTo():1);
            statisticsDataRequest.setSensorTableCode(parameter.getSensorTableName());
            statisticsDataRequest.setParamDtls(parameter);
            statisticsDataRequest.setStationId(request.getId());
            response = circuitBreakerWrapper.loadParameterDataGraph(statisticsDataRequest);
        }
        log.info("loadParameterDataGraph exit ");
        return response;
    }
    @Override
    public String loadStatisticsData(StatisticsRequest request) {
        String response = null;
        log.info("loadStatisticsData entry ");
        try {
            StatisticsParamDataRequest dataRequest = new StatisticsParamDataRequest();
            if (request.getLoggedIn() == null)
                throw new TemsCustomException("User not found");
            if (request.getFromDate() == null || request.getToDate() == null)
                throw new TemsCustomException("Date not found");
            if (request.getStationIds() == null)
                throw new TemsCustomException("Station not found");
            if (request.getSensorCode() == null)
                throw new TemsCustomException("Sensor not found");
            Sensors sensors = sensorRepository.findBySensorCode(request.getSensorCode());
            List<StationResponse> stationResponseList = stationRepository.findStationDtlsById(request.getLoggedIn(), request.getStationIds());
            if (!request.getParameters().isEmpty()) {
                List<SensorParamView> graphParameters = new ArrayList<>();
                for (Integer param : request.getParameters()) {
                    SensorParamView paramView = sensorParamViewRepository.findByParameterId(param);
                    if (paramView != null) {
                        Double min = paramView.getMin()!=null?paramView.getMin():10;
                        paramView.setGraphYAxisMin(min);
                        paramView.setDisplayRoundTo(paramView.getDisplayRoundTo() != null?paramView.getDisplayRoundTo():1);
                        UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(request.getLoggedIn(), paramView.getParamId());
                        if (upu != null && upu.getOperation() != null) {
                            paramView.setOperation(upu.getOperation());
                            paramView.setCalculatedValue(String.valueOf(upu.getCalculatedValue()));
                            paramView.setUnitSymbol(upu.getUnitSymbol());
                            paramView.setGraphYAxisMin(upu.getGraphYAxisMin()!=null?upu.getGraphYAxisMin():min);
                        }
                        graphParameters.add(paramView);
                    }
                }
                dataRequest.setGraphParameters(graphParameters);
            }
            if (!request.getWindRoseParam().isEmpty()) {
                List<StatisticsWindroseParam> windroseParamList = new ArrayList<>();
                for (String param : request.getWindRoseParam()) {
                    StatisticsWindroseParam windroseParam = new StatisticsWindroseParam();
                    SensorParamView pmS = sensorParamViewRepository.findByParameterName(param + " Speed");
                    SensorParamView pmD = sensorParamViewRepository.findByParameterName(param + " Direction");
                    List<WindRoseRange> windRoseArray = wrRepo.findAllWindRoseRangeByParameter(pmS.getParamId());
                    windroseParam.setParameterOne(pmS);
                    windroseParam.setParameterTwo(pmD);
                    windroseParam.setParamName(param);
                    windroseParam.setWindRoseRanges(windRoseArray);
                    windroseParamList.add(windroseParam);
                }
                dataRequest.setWindRoseParams(windroseParamList);
            }
            dataRequest.setSensorTableCode(sensors.getSensorTableName());
            dataRequest.setFromDate(request.getFromDate());
            dataRequest.setToDate(request.getToDate());
            dataRequest.setStationIds(stationResponseList);
            response = circuitBreakerWrapper.loadStatisticsData(dataRequest);
        }catch(Exception e){
            log.debug("loadStatistics failed with exception "+e.getMessage());
            throw new TemsCustomException("Load Statistics failed");
        }
        log.info("loadStatisticsData exit ");
        return response;
    }

    @Override
    public String loadAdvStatisticsData(StatisticsRequest request) {
        String response = null;
        log.info("loadAdvStatisticsData entry ");
        try {
            StatisticsParamDataRequest dataRequest = new StatisticsParamDataRequest();
            if (request.getLoggedIn() == null)
                throw new TemsCustomException("User not found");
            if (request.getFromDate() == null || request.getToDate() == null)
                throw new TemsCustomException("Date not found");
            if (request.getStationIds() == null)
                throw new TemsCustomException("Station not found");
            if (request.getParameters() == null || request.getParameters().isEmpty())
                throw new TemsCustomException("Parameter not found");
            if (request.getSensorCode() == null)
                throw new TemsCustomException("Sensor not found");
            Sensors sensors = sensorRepository.findBySensorCode(request.getSensorCode());
            List<StationResponse> stationResponseList = stationRepository.findStationDtlsById(request.getLoggedIn(), request.getStationIds());

            List<SensorParamView> graphParameters = new ArrayList<>();
            for (Integer param : request.getParameters()) {
                SensorParamView paramView = sensorParamViewRepository.findByParameterId(param);
                if (paramView != null) {
                    paramView.setDisplayRoundTo(paramView.getDisplayRoundTo() != null?paramView.getDisplayRoundTo():1);
                    Double min = paramView.getMin()!=null?paramView.getMin():10;
                    paramView.setGraphYAxisMin(min);
                    UserSensorParamUnitsPojo upu = upuRepo.findAllByUserAndParamForHome(request.getLoggedIn(), paramView.getParamId());
                    if (upu != null && upu.getOperation() != null) {
                        paramView.setOperation(upu.getOperation());
                        paramView.setCalculatedValue(String.valueOf(upu.getCalculatedValue()));
                        paramView.setUnitSymbol(upu.getUnitSymbol());
                        paramView.setGraphYAxisMin(upu.getGraphYAxisMin()!=null?upu.getGraphYAxisMin():min);
                    }
                    graphParameters.add(paramView);
                }
            }
            dataRequest.setGraphParameters(graphParameters);
            dataRequest.setSensorTableCode(sensors.getSensorTableName());
            dataRequest.setFromDate(request.getFromDate());
            dataRequest.setToDate(request.getToDate());
            dataRequest.setStationIds(stationResponseList);
            response = circuitBreakerWrapper.loadAdvStatisticsData(dataRequest);
        }catch(Exception e){
            log.debug("loadAdvStatisticsData failed with exception "+e.getMessage());
            throw new TemsCustomException("Load Advanced Statistics failed");
        }
        log.info("loadAdvStatisticsData exit ");
        return response;
    }

}
