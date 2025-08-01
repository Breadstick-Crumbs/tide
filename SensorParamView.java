package com.tridel.tems_sensor_service.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorParamView {

    private Integer paramId;
    private String paramName;
    private Double min;
    private Double max;
    private Double warn;
    private Double danger;
    private Integer homeOrder;
    private Integer displayRoundTo;
    private Boolean appParamEnabled;
    private Integer paramUnitId;
    private Integer unitId;
    private String unitSymbol;
    private Integer sensorId;
    private String sensorCode;
    private String sensorName;
    private Integer sensorOrder;
    private String sensorTableName;
    private String sensorStatusTableName;
    private String sensorTableCode;
    private Boolean notifyFlag;
    private String parameterDisplayName;
    private String data;
    private String loggedIn;
    private String operation;
    private String calculatedValue;
    private String dataParamName;
    private Integer sensorOrderSeq;
    private Integer paramOrderSeq;
    private String dangerOperation;
    private String warnOperation;
    private Double graphYAxisMin;

    public SensorParamView(SensorParamView param, String data) {
        this.paramId = param.getParamId();
        this.paramName = param.getParameterDisplayName()!=null ? param.getParameterDisplayName() : param.getParamName();
        this.min = param.getMin();
        this.max = param.getMax();
        this.danger = param.getDanger();
        this.warn = param.getWarn();
        this.sensorCode = param.getSensorCode();
        this.data = data != null ? data : "";
    }

    public SensorParamView(Integer paramId, String paramName, String dataParamName,Double min,
                           Double max, Double warn, Double danger,  Integer displayRoundTo,
                           String unitSymbol, Integer sensorId, String sensorCode, String sensorTableName,
                           String sensorStatusTableName,String sensorName, Boolean notifyFlag, Integer sensorOrder){
        this.paramId = paramId;
        this.paramName = paramName;
        this.dataParamName = dataParamName;
        this.min = min;
        this.max = max;
        this.warn = warn;
        this.danger = danger;
        this.displayRoundTo = displayRoundTo;
        this.unitSymbol = unitSymbol;
        this.sensorId = sensorId;
        this.sensorCode = sensorCode;
        this.sensorTableName = sensorTableName;
        this.sensorStatusTableName = sensorStatusTableName;
        this.sensorName = sensorName;
        this.sensorOrder = sensorOrder;
        this.notifyFlag = notifyFlag;
    }

    public SensorParamView(Integer paramId, String paramName,  Double min,
                           Double max, Double warn, Double danger, String unitSymbol, String sensorCode){
        this.paramId = paramId;
        this.paramName = paramName;
        this.min = min;
        this.max = max;
        this.warn = warn;
        this.danger = danger;
        this.unitSymbol = unitSymbol;
        this.sensorCode = sensorCode;
    }

    public SensorParamView(Integer paramId, String paramName, Double warn, Double danger, String unitSymbol, String sensorCode, Boolean notifyFlag, String parameterDisplayName,Integer displayRoundTo,String dataParamName) {
        this.paramId = paramId;
        this.paramName = paramName;
        this.warn = warn;
        this.danger = danger;
        this.unitSymbol = unitSymbol;
        this.sensorCode = sensorCode;
        this.notifyFlag = notifyFlag;
        this.parameterDisplayName = parameterDisplayName;
        this.displayRoundTo = displayRoundTo;
        this.dataParamName = dataParamName;
    }

    public SensorParamView(Integer paramId, String paramName, String dataParamName){
        this.paramId = paramId;
        this.paramName = paramName;
        this.dataParamName = dataParamName;
    }
    public SensorParamView(Integer paramId, String paramName){
        this.paramId = paramId;
        this.paramName = paramName;
    }

    /*public SensorParamView(Integer paramId, String paramName, String parameterDisplayName, String paramCode){
        this.paramId = paramId;
        this.paramName = paramName;
        this.paramCode = paramCode;
        this.parameterDisplayName = parameterDisplayName;
    }*/

    public SensorParamView(String paramName, String parameterDisplayName, Double min, Double max,
                           Integer displayRoundTo, String operation, BigDecimal calculatedValue, String unitSymbol) {
        this.paramName = paramName;
        this.parameterDisplayName = parameterDisplayName;
        this.min = min;
        this.max = max;
        this.displayRoundTo = displayRoundTo;
        this.operation = operation;
        this.calculatedValue = String.valueOf(calculatedValue);
        this.unitSymbol = unitSymbol;
    }
    public SensorParamView( String paramName, String parameterDisplayName, Double min, Double max,
                           Integer displayRoundTo, String operation, BigDecimal calculatedValue, String unitSymbol,
                           String sensorTableCode, Integer paramUnitId) {
        this.paramName = paramName;
        this.parameterDisplayName = parameterDisplayName;
        this.min = min;
        this.max = max;
        this.displayRoundTo = displayRoundTo;
        this.operation = operation;
        this.calculatedValue = String.valueOf(calculatedValue);
        this.unitSymbol = unitSymbol;
        this.sensorTableCode = sensorTableCode;
        this.paramUnitId = paramUnitId;
    }
    public SensorParamView(Integer sensorId,String sensorCode,String dataParamName,String sensorTableCode,String sensorTableName) {
        this.sensorId = sensorId;
        this.sensorCode = sensorCode;
        this.dataParamName = dataParamName;
        this.sensorTableCode = sensorTableCode;
        this.sensorTableName = sensorTableName;
    }
    public SensorParamView(Integer paramId,String paramName,String dataParamName,Double min, Double max,Double warn, Double danger,Integer displayRoundTo,
                           String unitSymbol,Integer sensorId,String sensorCode,String sensorName,String sensorTableName,
                           String sensorStatusTableName,Integer sensorOrderSeq,Integer paramOrderSeq,String dangerOperation,String warnOperation) {
        this.paramId = paramId;
        this.paramName = paramName;
        this.dataParamName = dataParamName;
        this.min = min;
        this.max = max;
        this.warn = warn;
        this.danger = danger;
        this.displayRoundTo = displayRoundTo;
        this.unitSymbol = unitSymbol;
        this.sensorId = sensorId;
        this.sensorCode = sensorCode;
        this.sensorName = sensorName;
        this.sensorTableName = sensorTableName;
        this.sensorStatusTableName = sensorStatusTableName;
        this.sensorOrderSeq = sensorOrderSeq;
        this.paramOrderSeq = paramOrderSeq;
        this.dangerOperation = dangerOperation;
        this.warnOperation = warnOperation;

    }
    public SensorParamView(Integer paramId,String paramName,String dataParamName,Double min, Double max,Double warn, Double danger,Integer displayRoundTo,
                           String unitSymbol,String sensorTableName,
                           String sensorStatusTableName,String dangerOperation,String warnOperation, Boolean notifyFlag) {
        this.paramId = paramId;
        this.paramName = paramName;
        this.dataParamName = dataParamName;
        this.min = min;
        this.max = max;
        this.warn = warn;
        this.danger = danger;
        this.displayRoundTo = displayRoundTo;
        this.unitSymbol = unitSymbol;
        this.sensorTableName = sensorTableName;
        this.sensorStatusTableName = sensorStatusTableName;
        this.dangerOperation = dangerOperation;
        this.warnOperation = warnOperation;
        this.notifyFlag = notifyFlag;

    }



}
