package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.master.CommunicationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunicationTypeRepository extends JpaRepository<CommunicationType, Integer> {

}
