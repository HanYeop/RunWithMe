package com.ssafy.gumid101.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_run_record")
@EntityListeners(AuditingEntityListener.class)
public class RunRecordEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "run_record_seq")
	private Long runRecordSeq;

	@Column(nullable = true, name = "run_record_start_time")
	private LocalDateTime runRecordStartTime;
	
	@Column(nullable = true, name = "run_record_end_time")
	private LocalDateTime runRecordEndTime;

	@Column(nullable = true, name = "run_record_running_time")
	private Integer runRecordRunningTime;

	@Column(nullable = true, name = "run_record_running_distance")
	private Integer runRecordRunningDistance;

	@Column(nullable = true, name = "run_record_calorie")
	private Double runRecordCalorie;

	@Column(nullable = true, name = "run_record_lat")
	private Double runRecordLat;

	@Column(nullable = true, name = "run_record_lng")
	private Double runRecordLng;

	@Column(nullable = true, name = "run_record_complete_YN", columnDefinition = "varchar(10) default 'N'")
	private String runRecordCompleteYN;

	@ManyToOne
	@JoinColumn(name = "user_seq")
	private UserEntity userEntity;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="img_seq")
	private ImageFileEntity imageFile;
	
	@ManyToOne
	@JoinColumn(name = "crew_seq")
	private CrewEntity crewEntity;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "runRecord")
	private List<RecordCoordinateEnitity> coordinate;
	
	@Column(nullable = false, name = "run_record_reg_time")
	@CreatedDate
	private LocalDateTime runRecordRegTime;

}
