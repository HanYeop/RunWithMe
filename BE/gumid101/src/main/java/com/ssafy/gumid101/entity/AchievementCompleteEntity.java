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


@Entity
@Table(name="t_ahievement_complete")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AchievementCompleteEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ac_seq")
	private Long acSeq; 
	
	@ManyToOne
	@JoinColumn(name="achieve_seq")
	private AchievementEntity achieveEntity;
	
	@ManyToOne
	@JoinColumn(name="user_seq")
	private UserEntity userEntity;

	@CreatedDate
	@Column(name="achieve_complete_reg_time")
	private LocalDateTime achieveCompleteRegTime;
}
