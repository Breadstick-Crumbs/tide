package com.tridel.tems_sensor_service.repository;

import com.tridel.tems_sensor_service.entity.master.ReportInterval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportIntervalRepository extends JpaRepository<ReportInterval, Integer> {

    List<ReportInterval> findAllByReportIntervalFlagIsTrue();

    List<ReportInterval> findAllByReportClimateIntervalFlagIsTrue();
}
