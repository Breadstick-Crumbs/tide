package com.tridel.tems_sensor_service.entity;

import com.tridel.tems_sensor_service.entity.master.StationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SqlResultSetMapping(name="StationResultSet", classes={
		@ConstructorResult(targetClass=com.tridel.tems_sensor_service.model.StationDetails.class,
				columns={
						@ColumnResult(name="station_id"), @ColumnResult(name="is_buoy_type"),
						@ColumnResult(name="buoy_watch_circle_warn"),@ColumnResult(name="buoy_watch_circle_danger"),
						@ColumnResult(name="parameter_datetime"), @ColumnResult(name="maintenance_flag"),
						@ColumnResult(name="lat"), @ColumnResult(name="lng"), @ColumnResult(name="switch_state"),
						@ColumnResult(name="station_name"), @ColumnResult(name="station_code"),
						@ColumnResult(name="mobility_flag"), @ColumnResult(name="disname"), @ColumnResult(name="legend")
				}
		)
}
)
@Table(name="tems_station")
public class StationData{
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "station_id",nullable = false )
	private Integer id;

	@Column(name = "station_name")
	private String stationName;

	@Column(name = "station_code",unique = true)
	private String stationCode;

//	@Column(name="station_type_id")
	@ManyToOne
	@JoinColumn(name="station_type_id", referencedColumnName="station_type_id")
	private StationType stationType;

	@Column(name = "location_details")
	private String locationDetails;

	@Column(name = "station_img")
	private String stationImg;

	@Column(name = "station_lat")
	private Double latitude;

	@Column(name = "station_lng")
	private Double longitude;

	@Column(name="station_created_at")
	@CreationTimestamp
	private Date created;

	@Column(name="station_updated_at")
	@UpdateTimestamp
	private Date updated;

	@Column(name = "station_bg_color",unique = true)
	private String stationBgColor;

	@Column(name = "buoy_height",precision = 13)
	private Double buoyHeight;

	@Column(name = "buoy_depth",precision = 13)
	private Double buoyDepth;

	@Column(name = "buoy_burn_pts",precision = 13)
	private Double buoyBurnPts;

	@Column(name = "aws_id")
	private String awsId;

	@Column(name = "cam_id")
	private String camId;

	@Column(name = "gprs_id")
	private String gprsId;

	@Column(name = "gsm")
	private String gsm;

	@Column(name = "buoy_watch_circle_warn")
	private Integer buoyWatchCircleWarn;

	@Column(name = "buoy_watch_circle_danger")
	private Integer buoyWatchCircleDanger;

	@Column(name = "no_signal_flag")
	private Boolean noSignalFlag;

	@Column(name  = "mobility_flag")
	private Boolean mobilityFlag;

	@Column(name  = "maintenance_flag" , columnDefinition="bit default 0")
	private Boolean maintenanceFlag;

	@Column(name="maintenance_start_date")
	private Date maintenanceStartDate;

	@Column(name="maintenance_end_date")
	private Date maintenanceEndDate;

	@Column(name = "station_timezone")
	private String stationTimeZone;

	@Column(name = "is_buoy_type")
	private Boolean isBuoyType;

	@Column(name = "synop_flag")
	private Boolean synopFlag;
}
