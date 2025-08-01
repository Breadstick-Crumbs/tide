package com.tridel.tems_sensor_service.service;

import com.tridel.tems_sensor_service.entity.NotificationDetails;
import com.tridel.tems_sensor_service.entity.Sensors;
import com.tridel.tems_sensor_service.entity.StationData;
import com.tridel.tems_sensor_service.exception.TemsBadRequestException;
import com.tridel.tems_sensor_service.exception.TemsCustomException;
import com.tridel.tems_sensor_service.model.request.NotificationRecipient;
import com.tridel.tems_sensor_service.model.request.SensorDataMailRequest;
import com.tridel.tems_sensor_service.model.response.MailDetails;
import com.tridel.tems_sensor_service.repository.NotificationDetailsRepository;
import com.tridel.tems_sensor_service.repository.SensorRepository;
import com.tridel.tems_sensor_service.repository.StationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.tridel.tems_sensor_service.util.CommonUtil.*;

@Service
@Transactional
@Slf4j
public class NotificationServiceImpl implements NotificationService{
    StationRepository stationRepo;
    NotificationDetailsRepository notificationDetailsRepo;
    SensorRepository sensorRepo;
    CommonService commonService;
    NotificationServiceImpl(StationRepository stationRepo,NotificationDetailsRepository notificationDetailsRepo,
                            SensorRepository sensorRepo,CommonService commonService){
        this.stationRepo=stationRepo;
        this.notificationDetailsRepo = notificationDetailsRepo;
        this.sensorRepo = sensorRepo;
        this.commonService = commonService;
    }

    @Override
    public List<NotificationRecipient> getAllNotificationRecipients(NotificationRecipient notificationRecipient){
        List<NotificationRecipient> notificationRecipients = new ArrayList<>();
        try {
            if (StringUtils.isBlank(notificationRecipient.getLoggedIn()))
                throw new TemsBadRequestException("User not found");
            if (StringUtils.isBlank(notificationRecipient.getStationId()))
                throw new TemsBadRequestException("Station not found");
            if (!notificationRecipient.getNotifyTypeId().equalsIgnoreCase("3") && StringUtils.isBlank(notificationRecipient.getSensorId()))
                throw new TemsBadRequestException("Sensor not found");
            List<String> list = Arrays.asList(notificationRecipient.getNotifyTypeId().split(","));
            if (StringUtils.isBlank(notificationRecipient.getSensorId()))
                notificationRecipients = notificationDetailsRepo.findAllGeneral(Integer.valueOf(notificationRecipient.getStationId()));
            else
                notificationRecipients = notificationDetailsRepo.findAllByStationAndSensor(Integer.valueOf(notificationRecipient.getStationId()), Integer.valueOf(notificationRecipient.getSensorId()),list);
        }catch(Exception e){
            log.info("Get All Notification Recipients failed with exception "+e.getMessage());
            if(e instanceof  TemsBadRequestException)
                throw e;
            else throw new TemsCustomException("Get All Notification Recipients failed");
        }
        return notificationRecipients;
    }
    @Override
    public String addNotificationRecipients(NotificationRecipient notificationRecipient, HttpServletRequest httpServletRequest){
        StringBuilder reqDtls = new StringBuilder();
        try {
            if (StringUtils.isBlank(notificationRecipient.getLoggedIn()))
                throw new TemsBadRequestException("User not found");
            if (StringUtils.isBlank(notificationRecipient.getStationId()))
                throw new TemsBadRequestException("Station not found");
            if (!notificationRecipient.getNotifyTypeId().equalsIgnoreCase("3") && StringUtils.isBlank(notificationRecipient.getSensorId()))
                throw new TemsBadRequestException("Sensor not found");
            if (StringUtils.isBlank(notificationRecipient.getNotifyUserName()))
                throw new TemsBadRequestException("Name cannot be empty");
            if (StringUtils.isBlank(notificationRecipient.getNotifyUserEmail()))
                throw new TemsBadRequestException("Email cannot be empty");
            Optional<StationData> station = stationRepo.findById(Integer.valueOf(notificationRecipient.getStationId()));
            List<String> notTypes = List.of(notificationRecipient.getNotifyTypeId().split(","));

            List<Integer> recipient = notificationDetailsRepo.findAllByStationAndSensorAndTypeAndNameAndEmail(Integer.parseInt(notificationRecipient.getStationId()), notificationRecipient.getSensorId()!=null?Integer.parseInt(notificationRecipient.getSensorId()):null, notTypes, notificationRecipient.getNotifyUserName(), notificationRecipient.getNotifyUserEmail());
            if (!recipient.isEmpty())
                throw new TemsBadRequestException("Notification Detail already exists");
            Optional<Sensors> sensor = Optional.empty();
            if(notificationRecipient.getSensorId()!= null)
                sensor=sensorRepo.findById(Integer.valueOf(notificationRecipient.getSensorId()));

            for (String notification : notTypes) {
                NotificationDetails notificationDetails = new NotificationDetails();
                Optional<StationData> finalStation = station;
                station.ifPresent(st -> notificationDetails.setStationData(finalStation.get()));
                Optional<Sensors> finalSensor = sensor;
                sensor.ifPresentOrElse(sen->notificationDetails.setSensor(finalSensor.get()),()->notificationDetails.setSensor(null));
                notificationDetails.setEmail(notificationRecipient.getNotifyUserEmail());
                notificationDetails.setName(notificationRecipient.getNotifyUserName());
                notificationDetails.setNotificationModeName(notificationRecipient.getNotifyModeName());
                notificationDetails.setNotificationModeId(1);
                notificationDetails.setNotificationType(Integer.valueOf(notification));
                notificationDetails.setCreatedAt(new Date());
                notificationDetailsRepo.save(notificationDetails);
            }
            reqDtls.append(REMOTE_ADDR).append(httpServletRequest.getRemoteAddr()).
                    append(httpServletRequest.getRemoteHost()).append(USER_AGENT_STR).
                    append(httpServletRequest.getHeader(USER_AGENT));

            StringBuilder sensorStr = new StringBuilder(sensor.isPresent() ? sensor.get().getSensorName() : "");

            sensorStr.trimToSize();

            commonService.saveAuditDetails(Integer.parseInt(notificationRecipient.getLoggedIn()), "Notification Recipient details added", reqDtls.toString(),
                    "for " + sensorStr + " Sensor - " + station.get().getStationName(), "Notification Recipients", new Date());
            sensor = null;
            station = null;
            recipient = null;
            sensorStr= null;
        }catch(Exception e){
            log.info("Add notification recipient failed with exception "+e.getMessage());
            if(e instanceof TemsBadRequestException)
                throw e;
            else
                throw new TemsCustomException("Add notification recipient failed");
        }

        return SUCCESS_ADD;

    }
    @Override
    public String deleteNotificationRecipients(NotificationRecipient notificationRecipient, HttpServletRequest httpServletRequest) {
        StringBuilder reqDtls = new StringBuilder();
        try {
            if (StringUtils.isBlank(notificationRecipient.getLoggedIn()))
                throw new TemsBadRequestException("User not found");
            if (notificationRecipient.getNotificationDetailId() == null)
                throw new TemsBadRequestException("Notification detail id cannot be empty");
            Optional<NotificationDetails> nd = notificationDetailsRepo.findById(notificationRecipient.getNotificationDetailId());
            NotificationDetails ndt = null;
            if(nd.isPresent())
                 ndt = nd.get();
            else
                throw new TemsBadRequestException("No notification entry for the Id passed");
            notificationDetailsRepo.delete(ndt);
            StringBuilder sensorStr = new StringBuilder(ndt.getSensor()!=null ? ndt.getSensor().getSensorName() : "");

            sensorStr.trimToSize();
            commonService.saveAuditDetails(Integer.parseInt(notificationRecipient.getLoggedIn()), "Notification Recipient details deleted", reqDtls.toString(),
                    "for " + sensorStr + " Sensor - " + ndt.getStationData().getStationName(), "Notification Recipients", new Date());
        } catch (Exception e) {
            log.info("Delete notification recipient failed with exception "+e.getMessage());
            if (e instanceof TemsBadRequestException)
                throw e;
            else
                throw new TemsCustomException("Delete notification recipient failed");
        }
        return SUCCESS_DELETE;
    }

    @Override
    public MailDetails getMailDetails(SensorDataMailRequest request) {
        log.info("SensorData Mail service method entry {}",request);
        if (request.getStationId() == null)
            throw new TemsBadRequestException("Station not found");
        if (request.getSensorId() == null)
            throw new TemsBadRequestException("Sensor not found");

        List<String> emails =  notificationDetailsRepo.findAllMailIdByStationSensor(request.getStationId(), request.getSensorId(), request.getNotifyTypeId());
        MailDetails details = new MailDetails();
        if(!emails.isEmpty()){
            String[] emailIds = emails.toArray(new String[0]);
            log.info("Email ids : "+ Arrays.toString(emailIds));
            details.setEmails(emailIds);
            Optional<StationData> sd = stationRepo.findById(request.getStationId());
            sd.ifPresent(e -> {
                details.setLocationDetails(e.getLocationDetails());
                details.setStationCode(e.getStationCode());
            });
        }
        log.info("SensorData Mail service method exit ");
        return details;
    }

}
