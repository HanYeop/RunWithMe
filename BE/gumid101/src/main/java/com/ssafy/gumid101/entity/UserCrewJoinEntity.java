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

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_crew_user")
public class UserCrewJoinEntity {
	
	@Id
	@Column(name="crew_user_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long crewUserSeq;

	@ManyToOne
	@JoinColumn(name="user_seq")
	private UserEntity userEntity;
	
	@ManyToOne
	@JoinColumn(name = "crew_seq")
	private CrewEntity crewEntity;
	
	@Column(nullable = false, name="crew_user_reg_time")
	@CreatedDate
	private LocalDateTime crewUserRegTime;
	
}
