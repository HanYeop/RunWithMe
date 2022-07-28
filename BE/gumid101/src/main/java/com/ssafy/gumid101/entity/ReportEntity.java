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

import com.ssafy.gumid101.customercenter.QuestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_report")
@EntityListeners(AuditingEntityListener.class)
public class ReportEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_seq")
	private Long reportSeq;

	@Column(nullable = false, name = "report_content")
	private String reportContent;
	
	@Column(nullable = false, name = "report_status")
	private String reportStatus;

	// 이거 일부러 외래키 안 한댔음.
	@Column(nullable = false, name = "report_crew_board_seq")
	private Long reportCrewBoardSeq;
	
	//@ManyToOne
	//@JoinColumn("report_crew_board_seq")
	//private CrewBoardEntity reportCrewBoard;

	@ManyToOne
	@JoinColumn(name = "reporter_seq")
	private UserEntity userReporterEntity;
	
	@ManyToOne
	@JoinColumn(name = "target_seq")
	private UserEntity userTargetEntity;
	
	@Column(nullable = false, name = "report_reg_time")
	@CreatedDate
	private LocalDateTime reportRegTime;
}
