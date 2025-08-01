package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.StationData;
import com.tridel.tems_sensor_service.entity.UserStations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserStationRepository extends JpaRepository<UserStations, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM UserStations e WHERE e.user = :userId")
    void deleteAllByUserId(Integer userId);

    List<UserStations> findAllByUser(Integer userId);

    UserStations findAllByUserAndStationId(Integer userId, StationData st);
}
