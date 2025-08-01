package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.model.request.CentricDashboardRequest;
import com.tridel.tems_sensor_service.model.request.DailyDashboardRequest;
import com.tridel.tems_sensor_service.model.request.Request;
import com.tridel.tems_sensor_service.model.response.*;

import java.util.List;

public interface DashboardService {
   DashboardSensorDataResponse getSensorDetailsForDashboard(Request request);

    List<DailyDashboardData> loadDashboardDataForStation(DailyDashboardRequest request) ;

    WindDashboardData loadDashboardWindDataForMET(DailyDashboardRequest request);

    MetDashboardResponse loadDashboardDataForMET(DailyDashboardRequest request);

    List<CentricDashboardData> loadCentricDashboard(CentricDashboardRequest request);
}
