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
@Table(name = "t_crew_total_record")
@EntityListeners(AuditingEntityListener.class)
public class CrewTotalRecordEntity {
	
	@Id
	@Column(name="total_record_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long totalRecoredSeq;
	
	@Column(nullable = false, name = "total_calorie")
	private Double totalCalorie;
	
	@Column(nullable = false, name = "total_distance")
	private Integer totalDistance;

	@Column(nullable = false, name = "total_time")
	private Integer totalTime;
	
	@Column(nullable = false, name = "total_longest_time")
	private Integer totalLongestTime;
	
	@Column(nullable = false, name = "total_longest_distance")
	private Integer totalLongestDistance;

	@Column(nullable = false, name = "total_avg_speed")
	private Double totalAvgSpeed;

	@ManyToOne
	@JoinColumn(name = "user_seq")
	private UserEntity userEntity;
	
	@ManyToOne
	@JoinColumn(name = "crew_seq")
	private CrewEntity crewEntity;
	
	@Column(nullable = false, name = "total_record_reg_time")
	@CreatedDate
	private LocalDateTime totalRecordRegTime;
}
