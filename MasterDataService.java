package com.tridel.tems_sensor_service.service;


import com.tridel.tems_sensor_service.entity.master.*;

import java.util.List;

public interface MasterDataService {
    List<SensorCalibrationType> getAllCalibrationTypes();

    SensorCalibrationType getCalibrationTypeById(int id);

    List<CommunicationType> getAllCommunicationTypes();
    List<StationLegends> getAllStationLegends();
    List<StationType> getAllStationTypes();

    List<SensorType> getAllSensorTypes();

    List<SensorRange> getAllSensorRanges();

    SensorRange getSensorRangeByParameterId(int id);

    List<ReportPeriod> getAllReportPeriods();

    ReportPeriod getReportPeriodsById(int id);

    List<ReportInterval> getAllReportIntervals(String module);

    ReportInterval getReportIntervalById(int id);

    List<ReportFunctions> getAllReportFunctions();

    ReportFunctions getReportFunctionsById(int id);

    List<AQIReference> getAllAQIreference();

    ApplicationSettings getAppSettings();
}
