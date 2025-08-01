package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.entity.*;
import com.tridel.tems_sensor_service.exception.TemsBadRequestException;
import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.model.request.usersettings.SequenceSettingRequest;
import com.tridel.tems_sensor_service.model.request.usersettings.UserStationSensorRequest;
import com.tridel.tems_sensor_service.model.response.usersettings.SensorParamResponse;
import com.tridel.tems_sensor_service.model.response.usersettings.StationParamResponse;
import com.tridel.tems_sensor_service.model.response.usersettings.UserSettingsResponse;
import com.tridel.tems_sensor_service.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserSettingsServiceImpl implements UserSettingsService {

    StationSensorParamRepository stationSensorParamRepo;
    UserStationSensorParamRepository usspRepo;
    UserStationRepository usRepo;
    StationRepository stationRepo;
    UserDashboardSettingRepository udsRepo;
    UserDashboardParameterSettingRepository udpsRepo;
    UserHomeSettingRepository uhsRepo;
    UserHomeParameterSettingRepository uhpsRepo;
    SensorParamRepository sensorParamRepo;
    SensorRepository sensorRepo;
    CommonService commonService;

    UserSettingsServiceImpl(StationSensorParamRepository stationSensorParamRepo,
                            UserStationSensorParamRepository usspRepo, UserStationRepository usRepo,
                            StationRepository stationRepo, UserDashboardSettingRepository udsRepo,
                            UserDashboardParameterSettingRepository udpsRepo, UserHomeSettingRepository uhsRepo,
                            UserHomeParameterSettingRepository uhpsRepo, SensorParamRepository sensorParamRepo,
                            SensorRepository sensorRepo, CommonService commonService) {
        this.stationSensorParamRepo = stationSensorParamRepo;
        this.usspRepo = usspRepo;
        this.usRepo = usRepo;
        this.stationRepo = stationRepo;
        this.udsRepo = udsRepo;
        this.udpsRepo = udpsRepo;
        this.uhsRepo = uhsRepo;
        this.uhpsRepo = uhpsRepo;
        this.sensorParamRepo = sensorParamRepo;
        this.sensorRepo = sensorRepo;
        this.commonService = commonService;
    }

    @Override
    public List<StationParamResponse> getAllParameters(UserStationSensorRequest req) {
        if (req.getStationId() == null)
            throw new TemsBadRequestException("Station not found");
        return stationSensorParamRepo.findAllStationParam(req.getStationId());
    }

    @Override
    public String addUserStationSensor(List<UserStationSensorRequest> requsets) {
        log.info("addUserStationSensor entry");
        if (requsets.isEmpty())
            throw new TemsBadRequestException("User not found");

        //delete existing
        usRepo.deleteAllByUserId(requsets.get(0).getUserId());
        usspRepo.deleteAllUser(requsets.get(0).getUserId());

        requsets.forEach(req -> {
            if (req.getLoggedIn() == null)
                throw new TemsBadRequestException("User not found");
            if (req.getUserId() == null)
                throw new TemsBadRequestException("User not found");
            if (req.getStationId() == null)
                throw new TemsBadRequestException("Station not found");
            if (req.getStationParamIds().isEmpty())
                throw new TemsBadRequestException("Sensor Param not found");

            Optional<StationData> sd = stationRepo.findById(req.getStationId());
            if (sd.isPresent()) {
                UserStations ust = new UserStations();
                ust.setStationId(sd.get());
                ust.setUser(req.getUserId());
                ust.setDisplayInAnalytics(!req.getModules().isEmpty() && req.getModules().contains("Data Analytics"));
                ust.setDisplayInhome(!req.getModules().isEmpty() && req.getModules().contains("Home"));
                ust.setDisplayIndashboard(!req.getModules().isEmpty() && req.getModules().contains("Dashboard"));
                usRepo.save(ust);

                List<UserStationSensorParams> usspList = req.getStationParamIds().stream()
                        .map(stParamId -> setUserStationSensorParams(req.getUserId(), stParamId))
                        .filter(Objects::nonNull).toList();

                usspRepo.saveAll(usspList);
            }
        });
        commonService.saveAuditDetails(requsets.get(0).getLoggedIn(), "Updated", "",
                "User " +requsets.get(0).getUserName() , "Station & Sensor Assignment", new Date());
        log.info("addUserStationSensor exit");
        return "Successfully Added";
    }

    @Override
    public String getUserStationSensorDetail(UserStationSensorRequest req) {
        JSONArray array = new JSONArray();
        if (req.getUserId() == null)
            throw new TemsBadRequestException("User not found");

        List<UserStationSensorParams> usspList = usspRepo.findAllByUser(req.getUserId());


        Map<StationData, List<StationParamResponse>> resultMap = usspList.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getStationParamId().getStationData(),
                        Collectors.mapping(student -> new StationParamResponse(student.getStationParamId().getStationParamId(), student.getStationParamId().getSensorParamId().getParamName()), Collectors.toList()) // Get userName by userId
                ));

        resultMap.forEach((st, lists) -> {
            JSONObject obj = new JSONObject();
            JSONObject stObj = new JSONObject();
            stObj.put("stationId", st.getId());
            stObj.put("stationCode", st.getStationCode());
            stObj.put("stationName", st.getStationName());
            obj.put("station", stObj);
            obj.put("parameters", lists);
            UserStations us = usRepo.findAllByUserAndStationId(req.getUserId(), st);
            List<String> modules = new ArrayList<>();
            if (us.getDisplayInAnalytics()) modules.add("Data Analytics");
            if (us.getDisplayInhome()) modules.add("Home");
            if (us.getDisplayIndashboard()) modules.add("Dashboard");
            obj.put("module", modules);
            array.put(obj);
        });

        return array.toString();
    }

    @Override
    public List<UserSettingsResponse> getCustomizedDashboardDetails(SequenceSettingRequest req) {
        List<UserSettingsResponse> userSettingsResponses = new ArrayList<>();
        if (req.getLoggedIn() == null)
            throw new TemsBadRequestException("User not found");

        List<UserDashboardSettings> udsList = udsRepo.findAllByUserIdOrderBySensorOrderSeq(req.getLoggedIn());
        udsList.forEach(element -> {
            UserSettingsResponse response = new UserSettingsResponse();
            response.setLoggedIn(req.getLoggedIn());
            response.setSensorCode(element.getSensor().getSensorCode());
            response.setSensorName(element.getSensor().getSensorName());
            List<UserDashboardParameterSettings> udpsLists = udpsRepo.findAllByUserDashboardSettingsIdOrderByParamOrderSeq(element, req.getLoggedIn());
            response.setSensorSeq(element.getSensorOrderSeq());
            List<SensorParamResponse> sensorParamResponses = udpsLists.stream()
                    .map(ele -> new SensorParamResponse(ele.getSensorParams().getId(), ele.getSensorParams().getParamName(), ele.getParamOrderSeq())).toList();
            response.setParameters(sensorParamResponses);
            userSettingsResponses.add(response);
        });
        return userSettingsResponses;
    }

    @Override
    public String addCustomizedDashboard(SequenceSettingRequest req) {
        if (req.getLoggedIn() == null)
            throw new TemsBadRequestException("User not found");
        if (req.getSensorCode() == null)
            throw new TemsBadRequestException("Sensor not found");
        if (req.getSensorSeq() == null)
            throw new TemsBadRequestException("Sensor Sequence not found");
        if (req.getParameters() == null || req.getParameters().isEmpty())
            throw new TemsBadRequestException("Parameters not found");

        UserDashboardSettings ud = udsRepo.findBySensorCodeAndUserId(req.getSensorCode(), req.getLoggedIn());
        if (ud != null) {
            return "Data already exists";
        }

        Sensors sensor = sensorRepo.findBySensorCode(req.getSensorCode());
        if (sensor == null)
            throw new TemsBadRequestException("Sensor not found");

        UserDashboardSettings uds = new UserDashboardSettings();
        uds.setUserId(req.getLoggedIn());
        uds.setSensor(sensor);
        uds.setSensorOrderSeq(req.getSensorSeq());
        udsRepo.save(uds);

        req.getParameters().forEach(val -> {
            Optional<SensorParams> param = sensorParamRepo.findById(val.getParamId());
            param.ifPresent(pm -> {
                UserDashboardParameterSettings udps = new UserDashboardParameterSettings();
                udps.setSensorParams(pm);
                udps.setParamOrderSeq(val.getParamSeq());
                udps.setUserDashboardSettingsId(uds);
                udpsRepo.save(udps);
            });
        });

        return "Successfully Added";
    }

    @Override
    public String updateCustomizedDashboard(SequenceSettingRequest req) {
        if (req.getLoggedIn() == null)
            throw new TemsBadRequestException("User not found");
        if (req.getSensorCode() == null)
            throw new TemsBadRequestException("Sensor not found");
        if (req.getParameters() == null || req.getParameters().isEmpty())
            throw new TemsBadRequestException("Parameters not found");

        udpsRepo.deleteAllSensorParamDashboardSettings(req.getSensorCode(), req.getLoggedIn());

        List<UserDashboardParameterSettings> udpsList = udpsRepo.findAllBySensorAndUser(req.getSensorCode(), req.getLoggedIn());
        if(udpsList.isEmpty()){
            UserDashboardSettings uds = udsRepo.findBySensorCodeAndUserId(req.getSensorCode(), req.getLoggedIn());
            if (uds != null) {
                req.getParameters().forEach(val -> {
                    Optional<SensorParams> param = sensorParamRepo.findById(val.getParamId());
                    param.ifPresent(pm -> {
                        UserDashboardParameterSettings udps = new UserDashboardParameterSettings();
                        udps.setSensorParams(pm);
                        udps.setParamOrderSeq(val.getParamSeq());
                        udps.setUserDashboardSettingsId(uds);
                        udpsRepo.save(udps);
                    });
                });
            }
        }else {
            return "Please try again";
        }



        return "Successfully Updated";
    }

    @Override
    public String deleteCustomizedDashboard(SequenceSettingRequest req) {
        try{
            if (req.getLoggedIn() == null)
                throw new TemsBadRequestException("User not found");
            if (req.getSensorCode() == null)
                throw new TemsBadRequestException("Sensor not found");

            udpsRepo.deleteAllSensorParamDashboardSettings(req.getSensorCode(), req.getLoggedIn());

            List<UserDashboardParameterSettings> udpsList = udpsRepo.findAllBySensorAndUser(req.getSensorCode(), req.getLoggedIn());
            if(udpsList.isEmpty())
                udsRepo.deleteSensorDashboardSettings(req.getSensorCode(), req.getLoggedIn());
        }catch(Exception e){
            log.debug("deleteCustomizedDashboard exception "+e.getMessage());
            if(e instanceof TemsBadRequestException)
                throw e;
            else
                throw new TemsCustomException(e.getMessage());
        }
        return "Successfully Deleted";
    }

    @Override
    public List<UserSettingsResponse> getCustomizedHomeDetails(SequenceSettingRequest req) {
        List<UserSettingsResponse> userSettingsResponses = new ArrayList<>();
        if (req.getLoggedIn() == null)
            throw new TemsBadRequestException("User not found");

        List<UserHomeSettings> uhsList = uhsRepo.findAllByUserIdOrderBySensorOrderSeq(req.getLoggedIn());
        uhsList.forEach(element -> {
            UserSettingsResponse response = new UserSettingsResponse();
            response.setLoggedIn(req.getLoggedIn());
            response.setSensorCode(element.getSensor().getSensorCode());
            response.setSensorName(element.getSensor().getSensorName());
            response.setSensorSeq(element.getSensorOrderSeq());
            List<UserHomeParameterSettings> uhpsLists = uhpsRepo.findAllByUserHomeSettingsIdOrderByParamOrderSeq(element, req.getLoggedIn());
            List<SensorParamResponse> sensorParamResponses = uhpsLists.stream()
                    .map(ele -> new SensorParamResponse(ele.getSensorParams().getId(), ele.getSensorParams().getParamName(), ele.getParamOrderSeq())).toList();
            response.setParameters(sensorParamResponses);
            userSettingsResponses.add(response);
        });
        return userSettingsResponses;
    }

    @Override
    public String addCustomizedHome(SequenceSettingRequest req) {
        if (req.getLoggedIn() == null)
            throw new TemsBadRequestException("User not found");
        if (req.getSensorCode() == null)
            throw new TemsBadRequestException("Sensor not found");
        if (req.getSensorSeq() == null)
            throw new TemsBadRequestException("Sensor Sequence not found");
        if (req.getParameters() == null || req.getParameters().isEmpty())
            throw new TemsBadRequestException("Parameters not found");

        UserHomeSettings uh = uhsRepo.findBySensorCodeAndUserId(req.getSensorCode(), req.getLoggedIn());
        if (uh != null) {
            return "Data already exists";
        }

        Sensors sensor = sensorRepo.findBySensorCode(req.getSensorCode());
        if (sensor == null)
            throw new TemsBadRequestException("Sensor not found");

        UserHomeSettings uhs = new UserHomeSettings();
        uhs.setUserId(req.getLoggedIn());
        uhs.setSensor(sensor);
        uhs.setSensorOrderSeq(req.getSensorSeq());
        uhsRepo.save(uhs);

        req.getParameters().forEach(val -> {
            Optional<SensorParams> param = sensorParamRepo.findById(val.getParamId());
            param.ifPresent(pm -> {
                UserHomeParameterSettings uhps = new UserHomeParameterSettings();
                uhps.setSensorParams(pm);
                uhps.setParamOrderSeq(val.getParamSeq());
                uhps.setUserHomeSettingsId(uhs);
                uhpsRepo.save(uhps);
            });
        });

        return "Successfully Added";
    }

    @Override
    public String updateCustomizedHome(SequenceSettingRequest req) {
        if (req.getLoggedIn() == null)
            throw new TemsBadRequestException("User not found");
        if (req.getSensorCode() == null)
            throw new TemsBadRequestException("Sensor not found");
        if (req.getParameters() == null || req.getParameters().isEmpty())
            throw new TemsBadRequestException("Parameters not found");

        uhpsRepo.deleteAllSensorParamHomeSettings(req.getSensorCode(), req.getLoggedIn());

        List<UserHomeParameterSettings> uhpsList = uhpsRepo.findAllBySensorAndUser(req.getSensorCode(), req.getLoggedIn());
        if(uhpsList.isEmpty()){
            UserHomeSettings uhs = uhsRepo.findBySensorCodeAndUserId(req.getSensorCode(), req.getLoggedIn());
            if (uhs != null) {
                req.getParameters().forEach(val -> {
                    Optional<SensorParams> param = sensorParamRepo.findById(val.getParamId());
                    param.ifPresent(pm -> {
                        UserHomeParameterSettings uhps = new UserHomeParameterSettings();
                        uhps.setSensorParams(pm);
                        uhps.setParamOrderSeq(val.getParamSeq());
                        uhps.setUserHomeSettingsId(uhs);
                        uhpsRepo.save(uhps);
                    });
                });
            }
        }

        return "Successfully Updated";
    }

    @Override
    public String deleteCustomizedHome(SequenceSettingRequest req) {
        try{
            if (req.getLoggedIn() == null)
                throw new TemsBadRequestException("User not found");
            if (req.getSensorCode() == null)
                throw new TemsBadRequestException("Sensor not found");

            uhpsRepo.deleteAllSensorParamHomeSettings(req.getSensorCode(), req.getLoggedIn());

            List<UserHomeParameterSettings> uhpsList = uhpsRepo.findAllBySensorAndUser(req.getSensorCode(), req.getLoggedIn());
            if(uhpsList.isEmpty())
                uhsRepo.deleteSensorHomeSettings(req.getSensorCode(), req.getLoggedIn());
        }catch(Exception e){
            log.debug("deleteCustomizedDashboard exception "+e.getMessage());
            if(e instanceof TemsBadRequestException)
                throw e;
            else
                throw new TemsCustomException(e.getMessage());
        }
        return "Successfully Deleted";
    }

    private UserStationSensorParams setUserStationSensorParams(Integer userId, Integer id) {
        Optional<StationSensorParams> ssp = stationSensorParamRepo.findById(id);
        if (ssp.isPresent()) {
            UserStationSensorParams ussp = new UserStationSensorParams();
            ussp.setUser(userId);
            ussp.setStationParamId(ssp.get());
            return ussp;
        }
        return null;
    }
}
