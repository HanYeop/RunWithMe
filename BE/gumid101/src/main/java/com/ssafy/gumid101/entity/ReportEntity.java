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
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_report")
@EntityListeners(AuditingEntityListener.class)
public class ReportEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_seq")
	private long reportSeq;

	@Column(name = "report_content")
	private String reportContent;
	
	@Column(name = "report_status")
	private String reportStatus;

	// 이거 일부러 외래키 안 한댔음.
	@Column(name = "report_crew_board_seq")
	private int reportCrewBoardSeq;

	@ManyToOne
	@JoinColumn(name = "reporter_seq")
	private UserEntity userReporterEntity;
	
	@ManyToOne
	@JoinColumn(name = "target_seq")
	private UserEntity userTargetEntity;
	
	@Column(name = "report_reg_time")
	@CreatedDate
	private LocalDateTime reportRegTime;
}
