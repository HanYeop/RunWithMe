package com.ssafy.gumid101.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_daily_check")
@NoArgsConstructor
public class DailyCheckEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "daily_check_seq")
	private Long dailyCheckSeq;
	
	@Column(nullable = false, name = "user_seq")
	private Long userSeq;
	
	@CreatedDate
	@Column(nullable = false, name = "daily_check_time")
	private LocalDateTime dailyCheckTime;
}