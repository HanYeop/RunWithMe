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
@Table(name = "t_question")
@EntityListeners(AuditingEntityListener.class)
public class QuestionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "question_seq")
	private long questionSeq;

	@Column(name = "question_content")
	private String questionContent;
	
	@Column(name = "question_status")
	private String questionStatus;
	
	@ManyToOne
	@JoinColumn(name = "user_seq")
	private UserEntity userEntity;
	
	@Column(name = "question_reg_time")
	@CreatedDate
	private LocalDateTime questionRegTime;
}
