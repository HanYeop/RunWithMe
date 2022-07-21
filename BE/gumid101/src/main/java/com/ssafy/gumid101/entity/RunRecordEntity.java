package com.ssafy.gumid101.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_run_record")
@EntityListeners(AuditingEntityListener.class)
public class RunRecordEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "run_record_seq")
	private long runRecordSeq;

	@Column(name = "run_record_start_time")
	private LocalDateTime runRecordStartTime;
	
	@Column(name = "run_record_end_time")
	private LocalDateTime runRecordEndTime;

	@Column(name = "run_record_running_time")
	private int runRecordRunningTime;

	@Column(name = "run_record_running_distance")
	private double runRecordRunningDistance;

	@Column(name = "run_record_avg_speed")
	private double runRecordAvgSpeed;

	@Column(name = "run_record_calorie")
	private double runRecordCalorie;

	@Column(name = "run_record_lat")
	private double runRecordLat;

	@Column(name = "run_record_lng")
	private double runRecordLng;

	@Column(name = "run_record_complete_YN", columnDefinition = "varchar(10) default 'N'")
	private String runRecordCompleteYN;

	@ManyToOne
	@JoinColumn(name = "user_seq")
	private UserEntity userEntity;

	@OneToOne
	@JoinColumn(name="img_seq")
	private ImageFileEntity imageFile;
	/**
	 * 관계 더 남음
	 */

	@Column(name = "run_record_reg_time")
	@CreatedDate
	private LocalDateTime runRecordRegTime;
}
