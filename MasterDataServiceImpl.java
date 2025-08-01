package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.entity.master.*;
import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tridel.tems_sensor_service.util.CommonMasterUtil.*;


@Service
public class MasterDataServiceImpl implements MasterDataService{
    SensorCalibrationRepository sensorCalibrationRepository;
    CommunicationTypeRepository communicationTypeRepository;
    StationLegendRepository stationLegendRepository;
    StationTypeRepository stationTypeRepository;
    SensorTypeRepository sensorTypeRepository;
    SensorRangeRepository sensorRangeRepository;
    ReportPeriodRepository reportPeriodRepository;
    ReportIntervalRepository reportIntervalRepository;
    ReportFunctionsRepository reportFunctionsRepository;
    AQIReferenceRepository aqiReferenceRepository;
    ApplicationSettingsRepository applicationSettingsRepository;

    @Autowired
    MasterDataServiceImpl(SensorCalibrationRepository sensorCalibrationRepository,CommunicationTypeRepository communicationTypeRepository,
                       StationLegendRepository stationLegendRepository,StationTypeRepository stationTypeRepository,SensorTypeRepository sensorTypeRepository,
                       SensorRangeRepository sensorRangeRepository,ReportPeriodRepository reportPeriodRepository,ReportIntervalRepository reportIntervalRepository,
                       ReportFunctionsRepository reportFunctionsRepository,AQIReferenceRepository aqiReferenceRepository,
                       ApplicationSettingsRepository applicationSettingsRepository){
        this.sensorCalibrationRepository = sensorCalibrationRepository;
        this.communicationTypeRepository = communicationTypeRepository;
        this.stationLegendRepository = stationLegendRepository;
        this.stationTypeRepository = stationTypeRepository;
        this.sensorTypeRepository = sensorTypeRepository;
        this.sensorRangeRepository = sensorRangeRepository;
        this.reportPeriodRepository = reportPeriodRepository;
        this.reportIntervalRepository = reportIntervalRepository;
        this.reportFunctionsRepository = reportFunctionsRepository;
        this.aqiReferenceRepository = aqiReferenceRepository;
        this.applicationSettingsRepository = applicationSettingsRepository;
    }
    @Override
    public List<SensorCalibrationType> getAllCalibrationTypes() {
        List<SensorCalibrationType> calibrationTypes = new ArrayList<>();
        try {
            calibrationTypes= sensorCalibrationRepository.findAll();
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return calibrationTypes;
    }
    @Override
    public SensorCalibrationType getCalibrationTypeById(int id) {
        SensorCalibrationType calibrationTypes = null;
        try {
            Optional<SensorCalibrationType> calibrationType = sensorCalibrationRepository.findById(id);
            if(calibrationType.isPresent())
                calibrationTypes = calibrationType.get();
            else
                throw new TemsCustomException(NO_REC_CODE, NO_REC_MSG);
        }catch(TemsCustomException ex){
                throw ex;
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return calibrationTypes;
    }
    @Override
    public List<CommunicationType> getAllCommunicationTypes() {
        List<CommunicationType> communicationTypes = new ArrayList<>();
        try {
            communicationTypes= communicationTypeRepository.findAll();
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return communicationTypes;
    }
    @Override
    public List<StationLegends> getAllStationLegends() {
        List<StationLegends> stationLegends = new ArrayList<>();
        try {
            stationLegends= stationLegendRepository.findAll();
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return stationLegends;
    }
    @Override
    public List<StationType> getAllStationTypes() {
        List<StationType> stationTypes = new ArrayList<>();
        try {
            stationTypes= stationTypeRepository.findAll();
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return stationTypes;
    }
    @Override
    public List<SensorType> getAllSensorTypes() {
        List<SensorType> sensorType = new ArrayList<>();
        try {
            sensorType= sensorTypeRepository.findAll();
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return sensorType;
    }
    @Override
    public List<SensorRange> getAllSensorRanges() {
        List<SensorRange> sensorRange = new ArrayList<>();
        try {
            sensorRange= sensorRangeRepository.findAll();
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return sensorRange;
    }
    @Override
    public SensorRange getSensorRangeByParameterId(int id) {
        SensorRange sensorRange = null;
        try {
            Optional<SensorRange> sensorRangeObj= sensorRangeRepository.findBySensorParameterId(id);
            if(sensorRangeObj.isPresent())
                sensorRange = sensorRangeObj.get();
            else
                throw new TemsCustomException(NO_REC_CODE, NO_REC_MSG);
        }catch(TemsCustomException ex){
            throw ex;
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return sensorRange;
    }
    @Override
    public List<ReportPeriod> getAllReportPeriods() {
        List<ReportPeriod> reportPeriod = new ArrayList<>();
        try {
            reportPeriod= reportPeriodRepository.findAll();
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return reportPeriod;
    }
    @Override
    public ReportPeriod getReportPeriodsById(int id) {
        ReportPeriod reportPeriod = null;
        try {
            Optional<ReportPeriod> reportPeriodObj= reportPeriodRepository.findById(id);
            if(reportPeriodObj.isPresent())
                reportPeriod = reportPeriodObj.get();
            else
                throw new TemsCustomException(NO_REC_CODE, NO_REC_MSG);
        }catch(TemsCustomException ex){
            throw ex;
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return reportPeriod;
    }
    @Override
    public List<ReportInterval> getAllReportIntervals(String module) {
        List<ReportInterval> reportIntervals = new ArrayList<>();
        try {
            if(module.equalsIgnoreCase("reports"))
                reportIntervals= reportIntervalRepository.findAllByReportIntervalFlagIsTrue();
            else if(module.equalsIgnoreCase("climatereports"))
                reportIntervals= reportIntervalRepository.findAllByReportClimateIntervalFlagIsTrue();
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return reportIntervals;
    }
    @Override
    public ReportInterval getReportIntervalById(int id) {
        ReportInterval reportInterval = null;
        try {
            Optional<ReportInterval> reportIntObj= reportIntervalRepository.findById(id);
            if(reportIntObj.isPresent())
                reportInterval = reportIntObj.get();
            else
                throw new TemsCustomException(NO_REC_CODE, NO_REC_MSG);
        }catch(TemsCustomException ex){
            throw ex;
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return reportInterval;
    }
    @Override
    public List<ReportFunctions> getAllReportFunctions() {
        List<ReportFunctions> reportFunctions = new ArrayList<>();
        try {
            reportFunctions= reportFunctionsRepository.findAll();
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return reportFunctions;
    }
    @Override
    public ReportFunctions getReportFunctionsById(int id) {
        ReportFunctions reportFunctions = null;
        try {
            Optional<ReportFunctions> reportFunctionObj= reportFunctionsRepository.findById(id);
            if(reportFunctionObj.isPresent())
                reportFunctions = reportFunctionObj.get();
            else
                throw new TemsCustomException(NO_REC_CODE, NO_REC_MSG);
        }catch(TemsCustomException ex){
            throw ex;
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return reportFunctions;
    }
    @Override
    public List<AQIReference> getAllAQIreference() {
        List<AQIReference> aqiReference = new ArrayList<>();
        try {
            aqiReference= aqiReferenceRepository.findAll();
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return aqiReference;
    }
    @Override
    public ApplicationSettings getAppSettings() {
        ApplicationSettings applicationSettings = null;
        try {
            applicationSettings= applicationSettingsRepository.findAll().getFirst();
        }catch(Exception ex){
            throw new TemsCustomException(GEN_EXCEPTION_CODE, GEN_EXCEPTION_MSG);
        }
        return applicationSettings;
    }
}
