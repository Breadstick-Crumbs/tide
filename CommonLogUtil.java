package com.tridel.tems_sensor_service.util;

import java.text.SimpleDateFormat;

public class CommonLogUtil {
    public static String GEN_EXCEPTION_CODE="genexcpn";
    public static String GEN_EXCEPTION_MSG="Error Occurred";
    public static String USER_AUD_FAIL_CODE ="usrauditfailed";
    public static String USER_AUD_FAIL_MSG ="Save user action log failed";
    public static String TRAN_AUD_FAIL_CODE ="tranauditfailed";
    public static String TRAN_AUD_FAIL_MSG ="Save Transaction log failed";
    public static final String USER_NOT_FOUND_MSG = "User not found";
    public static final String INVALID_DATE_FORMAT ="Invalid date format. Please use the correct format: yyyy-MM-dd HH:mm:ss";
    public static final String  NO_NOTIFICATION_MSG= "No Notification with this id";
    public static final String  NO_NOTIFICATION_CODE= "nonotif";
    //DATE FORMATS
    public static final SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static final SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static final String PARAM_NOT_FOUND= "Parameter not found";
    public static final String STN_NOT_FOUND = "Station not found";
    public static final String SENSOR_CODE_NOT_FOUND = "Sensor Code not found";
    public static final String NOTIFICATION_NOT_FOUND = "Notification not found";
    public static final String DATE_NOT_FOUND = "Date not found";

}
