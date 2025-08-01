package com.tridel.tems_sensor_service.service;


import com.tridel.tems_sensor_service.model.request.DataMgmtRequest;
import com.tridel.tems_sensor_service.model.request.EditQCRequest;

public interface DataManagementService {

    String loadAllQCData(DataMgmtRequest request);
    String editQCData(EditQCRequest req);


}
