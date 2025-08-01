/*
package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.ReportIntervalRequest;
import com.tridel.tems_sensor_service.model.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name="tems-master-data-service")
public interface MasterServiceFeignClient {
    @GetMapping("/api/master/stationLegends")
    List<StationLegendsPojo> getLegendsMasterDataService();
    @GetMapping("/api/master/stationTypes")
    List<StationTypePojo> getSTypeMasterDataService();
    @PostMapping("/api/master/reportIntervals")
    List<ReportIntervalResponse> getAllReportIntervals(ReportIntervalRequest request);
    @PostMapping("/api/master/reportInterval")
    ReportIntervalResponse getAllReportInterval(ReportIntervalRequest request);
    @GetMapping("/api/master/appSettings")
    ApplicationSettingsPojo  getDataFromMasterDataService();
}
*/
