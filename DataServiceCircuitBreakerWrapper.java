package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.SensorParamViewReq;
import com.tridel.tems_sensor_service.model.request.*;
import com.tridel.tems_sensor_service.model.response.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataServiceCircuitBreakerWrapper {

    private final DataServiceFeignClient feignClient;

    public DataServiceCircuitBreakerWrapper(DataServiceFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackHeader")
    public Map<String,List<GenericResponse>> getHeaderDataWithCircuitBreaker(StationDtlRequest request) {
        return feignClient.getHeaderDataForStations(request);
    }
    public Map<String,List<GenericResponse>> fallbackHeader(Throwable throwable) {
        System.out.println("Fallback triggered from header station fetch: " + throwable);
        return new HashMap<String,List<GenericResponse>>();
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackSensorData")
    public  List<GenericResponse> getSensorDataWithCircuitBreaker(SensorParamViewReq request) {
        return feignClient.getSensorDataDetailsForHome(request);
    }
    public  List<GenericResponse> fallbackSensorData(Throwable throwable) {
        System.out.println("Fallback triggered from sensor details from home: " + throwable);
        return new ArrayList<GenericResponse>();
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackDashboardDataForMET")
    public  String loadDashboardDataForMET(DataRequest request) {
        return feignClient.loadDashboardDataForMET(request);
    }
    public  String fallbackDashboardDataForMET(Throwable throwable) {
        System.out.println("Fallback triggered from dashboard MET: " + throwable);
        return "";
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackOnlineStations")
    public  StationStatus getAllOnlineStations() {
        return feignClient.getAllOnlineStations();
    }
    public  StationStatus fallbackOnlineStations(Throwable throwable) {
        System.out.println("Fallback triggered from online stations: " + throwable);
        return new StationStatus();
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackDashboardDataForCentric")
    public  List<CentricDataResponse> loadCentricDashboardData(CentricDashboardReq request) {
        return feignClient.loadCentricDashboardData(request);
    }
    public  List<CentricDataResponse> fallbackDashboardDataForCentric(Throwable throwable) {
        System.out.println("Fallback triggered from centric dashboard: " + throwable);
        return new ArrayList<CentricDataResponse>();
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackloadHeaderData")
    public  HeaderDataStatusResponse loadHeaderData(ReportRequest request) {
        return feignClient.loadHeaderData(request);
    }
    public  HeaderDataStatusResponse fallbackloadHeaderData(Throwable throwable) {
        System.out.println("Fallback triggered from load header data: " + throwable);
        return new HeaderDataStatusResponse();
    }

    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackSummary")
    public  String loadSummaryData(DataRequest request) {
        return feignClient.loadSummaryData(request);
    }
    public  String fallbackSummary(Throwable throwable) {
        System.out.println("Fallback triggered from summary: " + throwable);
        return "";
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackOneMinRpt")
    public  String loadReportsDataOneMinute(ReportRequest request) {
        return feignClient.loadReportsDataOneMinute(request);
    }
    public  String fallbackOneMinRpt(Throwable throwable) {
        System.out.println("Fallback triggered from one min report: " + throwable);
        return new JSONArray().toString();
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackClimate")
    public  String loadClimateReportsDataOneMin(ReportRequest request) {
        return feignClient.loadClimateReportsDataOneMin(request);
    }
    public  String fallbackClimate(Throwable throwable) {
        System.out.println("Fallback triggered from climate report one min: " + throwable);
        return new JSONArray().toString();
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackStatistics")
    public  String loadStatisticsData(StatisticsParamDataRequest request) {
        return feignClient.loadStatisticsData(request);
    }
    public  String fallbackStatistics(Throwable throwable) {
        System.out.println("Fallback triggered from statistics: " + throwable);
        return new JSONArray().toString();
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackAdvStatistics")
    public  String loadAdvStatisticsData(StatisticsParamDataRequest request) {
        return feignClient.loadAdvStatisticsData(request);
    }
    public  String fallbackAdvStatistics(Throwable throwable) {
        System.out.println("Fallback triggered from advanced statistics: " + throwable);
        return new JSONArray().toString();
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackCommunication")
    public  Map<Integer, CommunicationPojo> getStationCommunicationDetails(StationDtlRequest request) {
        return feignClient.getStationCommunicationDetails(request);
    }
    public  Map<Integer, CommunicationPojo> fallbackCommunication(Throwable throwable) {
        System.out.println("Fallback triggered from communication: " + throwable);
        return new HashMap<Integer, CommunicationPojo>();
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackRepHourly")
    public  String loadReportsDataHourly(ReportRequest request) {
        return feignClient.loadReportsDataHourly(request);
    }
    public  String fallbackRepHourly(Throwable throwable) {
        System.out.println("Fallback triggered from Report hourly: " + throwable);
        return new JSONArray().toString();
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackRepInterval")
    public  String getReportByInterval(ReportRequest request) {
        return feignClient.getReportByInterval(request);
    }
    public  String fallbackRepInterval(Throwable throwable) {
        System.out.println("Fallback triggered from Report interval: " + throwable);
        return new JSONArray().toString();
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackGenLog")
    public  String generateLog(List<SensorParamViewReq> request) {
        return feignClient.generateLog(request);
    }
    public  String fallbackGenLog(Throwable throwable) {
        System.out.println("Fallback triggered from generate log: " + throwable);
        return "";
    }
    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackDashStn")
    public  String loadDashboardDataForStation(DataRequest request) {
        return feignClient.loadDashboardDataForStation(request);
    }
    public  String fallbackDashStn(Throwable throwable) {
        System.out.println("Fallback triggered from Dashboard for stations: " + throwable);
        return "";
    }

    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackStatistics")
    public Double getLatestDataForHomeParam(StatisticsDataRequest dataRequest) {
        return feignClient.getLatestDataForHomeParam(dataRequest);
    }

    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackRepInterval")
    public String loadClimateReportsDataInterval(ReportRequest request) {
        return feignClient.loadClimateReportsDataInterval(request);
    }

    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackStatistics")
    public String loadParameterDataGraph(StatisticsDataRequest statisticsDataRequest) {
        return feignClient.loadParameterDataGraph(statisticsDataRequest);
    }

    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackStatistics")
    public String getStationStatus(Request request) {
        return feignClient.getStationStatus(request);
    }

    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackMobileRep")
    public List<MobileReport> loadReportsData(ReportRequest request) {
        return feignClient.loadReportsData(request);
    }

    @CircuitBreaker(name = "dataService", fallbackMethod = "fallbackMobileRep")
    public List<MobileReport> loadReportsMinData(ReportRequest req) {
        return feignClient.loadReportsMinData(req);
    }

    public List<MobileReport> fallbackMobileRep(Throwable throwable) {
        System.out.println("Fallback triggered from Report interval: " + throwable);
        return new ArrayList<>();
    }

    @CircuitBreaker(name="dataService", fallbackMethod="fallbackAllQC")
    public String loadAllQCData(ReportRequest req){ return feignClient.loadAllQCData(req);}
    public String fallbackAllQC(Throwable t){ return "[]"; }

    @CircuitBreaker(name = "dataService")
    public String editQCData(EditQCRequest req) {
        return feignClient.editQCData(req);
    }
}