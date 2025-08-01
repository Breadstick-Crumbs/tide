package com.tridel.tems_sensor_service.dao;

import com.tridel.tems_sensor_service.model.request.ReportRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class ReportDaoImpl implements ReportDao{
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<String> getIntervalClimateReportsHeader(ReportRequest request) {
        List<String> headerlist = new ArrayList<>();
        StringBuilder procedure = new StringBuilder("EXEC tems_climate_report_header '"+request.getStation()+"','"+request.getSensorCode()+
                "','"+request.getInterval()+"','"+request.getLoggedIn()+"'");
        procedure.trimToSize();
        try{
            headerlist = entityManager.createNativeQuery(procedure.toString()).getResultList();
        }catch(NoResultException ignored){
        }
        return headerlist;
    }
}
