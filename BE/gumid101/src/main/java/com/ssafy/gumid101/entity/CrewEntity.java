package com.ssafy.gumid101.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Table(name="t_crew")
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class CrewEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "crew_seq")
	private long crewSeq;
	
	@Column(name="crew_name")
	private String crewName;
	
	@Column(name="crew_description")
	private String crewDescription;
	
	
	@Column(name="crew_goal_days") //1주일에 목표로하는 일 수
	private int crewGoalDays;
	
	@Column(name="crew_goal_type")
	private String crewGoalType; //목표 타입
	@Column(name="crew_goal_amount")
	private int crewGoalAmount; //목표 설정치
	@Column(name="crew_date_start")
	private LocalDateTime crewDateStart; //크루 시작날짜
	@Column(name="crew_date_end")
	private LocalDateTime crewDateEnd; //크루 끝나는 날짜
	
	
	@Column(name="crew_time_start")
	private LocalTime crewTimeStart;//크루의 러닝 시작 시간
	
	@Column(name="crew_time_end")
	private LocalTime crewTimeEnd; //크루의 러닝 끝나는 시간
	
	@Column(name="crew_passwrod")
	private String crewPasswrod;
	@Column(name="crew_cost")
	private int crewCost; //크루 참가비
	
	@Column(name="crew_max_member")
	private int crewMaxMember; //참가인원
	
	@CreatedDate
	@Column(name="crew_reg_time")
	private LocalDateTime crewRegTime; //크루 생성시간
	
}
