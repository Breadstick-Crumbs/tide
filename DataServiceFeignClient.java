package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.SensorParamViewReq;
import com.tridel.tems_sensor_service.model.request.*;
import com.tridel.tems_sensor_service.model.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@FeignClient(name="tems-data-service")
public interface DataServiceFeignClient {
    @RequestMapping(method = RequestMethod.POST, value ="/api/data/loadAllStations",consumes = "application/json", produces = "application/json")
    Map<String,List<GenericResponse>> getHeaderDataForStations(@RequestBody StationDtlRequest request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/data/getSensorDataDetailsForHome",consumes = "application/json", produces = "application/json")
    List<GenericResponse> getSensorDataDetailsForHome(@RequestBody SensorParamViewReq request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/datasummary/loadSummaryData",consumes = "application/json", produces = "application/json")
    String loadSummaryData(@RequestBody DataRequest request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/datareport/loadReportsDataOneMinute",consumes = "application/json", produces = "application/json")
    String loadReportsDataOneMinute(@RequestBody ReportRequest request);
    /*@RequestMapping(method = RequestMethod.POST, value ="/api/datareport/loadReportsDataByInterval",consumes = "application/json", produces = "application/json")
    String loadReportsDataByInterval(@RequestBody ReportRequest request);*/
    @RequestMapping(method = RequestMethod.POST, value ="/api/datareport/loadReportsDataHourly",consumes = "application/json", produces = "application/json")
    String loadReportsDataHourly(@RequestBody ReportRequest request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/datareport/loadClimateReportsDataOneMin",consumes = "application/json", produces = "application/json")
    String loadClimateReportsDataOneMin(@RequestBody ReportRequest request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/datareport/getClimateReportByInterval",consumes = "application/json", produces = "application/json")
    String loadClimateReportsDataInterval(@RequestBody ReportRequest request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/datastat/loadParameterDataGraph",consumes = "application/json", produces = "application/json")
    String loadParameterDataGraph(@RequestBody StatisticsDataRequest request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/data/getLatestDataForHomeParam",consumes = "application/json", produces = "application/json")
    Double getLatestDataForHomeParam(@RequestBody StatisticsDataRequest request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/datastat/loadStatisticsData",consumes = "application/json", produces = "application/json")
    String loadStatisticsData(@RequestBody StatisticsParamDataRequest request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/datastat/loadAdvStatisticsData",consumes = "application/json", produces = "application/json")
    String loadAdvStatisticsData(@RequestBody StatisticsParamDataRequest request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/stnmaster/getStationCommunicationDetails",consumes = "application/json", produces = "application/json")
    Map<Integer, CommunicationPojo> getStationCommunicationDetails(@RequestBody StationDtlRequest request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/stnmaster/generateLog",consumes = "application/json", produces = "application/json")
    String generateLog(@RequestBody List<SensorParamViewReq> request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/datadashboard/loadHeaderData",consumes = "application/json", produces = "application/json")
    HeaderDataStatusResponse loadHeaderData(@RequestBody ReportRequest request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/datadashboard/loadDashboardDataForStation",consumes = "application/json", produces = "application/json")
    String loadDashboardDataForStation(@RequestBody DataRequest request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/datadashboard/loadDashboardDataForMET",consumes = "application/json", produces = "application/json")
    String loadDashboardDataForMET(@RequestBody DataRequest request);

    @RequestMapping(method = RequestMethod.POST, value ="/api/datadashboard/loadCentricDashboardData",consumes = "application/json", produces = "application/json")
    List<CentricDataResponse> loadCentricDashboardData(@RequestBody CentricDashboardReq request);
    @RequestMapping(method = RequestMethod.POST, value ="/api/datareport/getReportByInterval",consumes = "application/json", produces = "application/json")
    String getReportByInterval(ReportRequest req);
    @RequestMapping(method = RequestMethod.GET, value ="/api/stnmaster/onlineStations",consumes = "application/json", produces = "application/json")
    StationStatus getAllOnlineStations();
    @RequestMapping(method = RequestMethod.GET, value ="/api/mobileapi/getStationStatus",consumes = "application/json", produces = "application/json")
    String getStationStatus(Request request);
    @RequestMapping(method = RequestMethod.GET, value ="/api/mobileapi/loadReportsData",consumes = "application/json", produces = "application/json")
    List<MobileReport> loadReportsData(ReportRequest request);
    @RequestMapping(method = RequestMethod.GET, value ="/api/mobileapi/loadReportsMinData",consumes = "application/json", produces = "application/json")
    List<MobileReport> loadReportsMinData(ReportRequest req);

    @RequestMapping(method = RequestMethod.POST, value = "/api/dataqa/loadAllQCData",consumes = "application/json",produces = "application/json")
    String loadAllQCData(@RequestBody ReportRequest req);

    @RequestMapping(method   = RequestMethod.POST, value = "/api/dataqa/editQCData", consumes = "application/json", produces = "application/json")
    String editQCData(@RequestBody EditQCRequest req);


}
