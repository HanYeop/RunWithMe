package com.ssafy.gumid101.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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

@Table(name="t_crew")
@Builder()
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class CrewEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "crew_seq")
	private Long crewSeq;
	
	@Column(nullable = false, name="crew_name")
	private String crewName;
	
	@Column(nullable = true, name="crew_description")
	private String crewDescription;
	
	@Column(nullable = false, name="crew_goal_days") //1주일에 목표로하는 일 수
	private Integer crewGoalDays;
	
	@Column(nullable = false, name="crew_goal_type")
	private String crewGoalType; //목표 타입
	
	@Column(nullable = false, name="crew_goal_amount")
	private Integer crewGoalAmount; //목표 설정치
	
	@Column(nullable = false, name="crew_date_start")
	private LocalDateTime crewDateStart; //크루 시작날짜
	
	@Column(nullable = false, name="crew_date_end")
	private LocalDateTime crewDateEnd; //크루 끝나는 날짜
	
	@Column(nullable = false, name="crew_time_start")
	private LocalTime crewTimeStart;//크루의 러닝 시작 시간
	
	@Column(nullable = false, name="crew_time_end")
	private LocalTime crewTimeEnd; //크루의 러닝 끝나는 시간
	
	@Column(nullable = true, name="crew_password")
	private String crewPassword;
	
	@Column(nullable = true, name="crew_cost")
	private Integer crewCost; //크루 참가비
	
	@Column(nullable = false, name="crew_max_member")
	private Integer crewMaxMember; //참가인원
	
	@JoinColumn(name = "img_seq")
	@OneToOne
	private ImageFileEntity imageFile;
	
	@ManyToOne
	@JoinColumn(name="user_seq")
	private UserEntity managerEntity;
	
	@OneToMany(mappedBy = "crewEntity")
	private List<UserCrewJoinEntity> userCrewJoinEntitys;
	
	@OneToMany(mappedBy = "crewEntity")
	private List<CrewTotalRecordEntity> crewTotalRecordEntitys;

	@OneToMany(mappedBy = "crewEntity")
	private List<RunRecordEntity> runRecordEntitys;
	
	@OneToMany(mappedBy = "crewEntity")
	private List<CrewBoardEntity> crewBoardEntitys;
	
	@CreatedDate
	@Column(nullable = false, name="crew_reg_time")
	private LocalDateTime crewRegTime; //크루 생성시간
	
}
