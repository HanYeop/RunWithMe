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

/**
 * 업적
 * @author start
 *
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="t_achievement")
@EntityListeners(AuditingEntityListener.class)
public class AchievementEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="achieve_seq")
	private Long achiveSeq;
	
	@Column(name="achieve_name")
	private String achieveName;
	
	@Column(name="achieve_type")
	private String achieveType;
	
	@Column(name="achieve_value")
	private Double achiveValue;
	
	@CreatedDate
	@Column(name="achieve_reg_time")
	private LocalDateTime achieveRegTime;
}
