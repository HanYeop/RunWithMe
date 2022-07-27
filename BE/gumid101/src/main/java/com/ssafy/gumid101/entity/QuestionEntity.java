package com.ssafy.gumid101.entity;

import java.time.LocalDateTime;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "t_question")
@EntityListeners(AuditingEntityListener.class)
public class QuestionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "question_seq")
	private Long questionSeq;

	@Column(nullable = false, name = "question_content")
	private String questionContent;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "question_status")
	private QuestStatus questionStatus;
	
	@ManyToOne
	@JoinColumn(name = "user_seq")
	private UserEntity userEntity;
	
	@Column(nullable = false, name = "question_reg_time")
	@CreatedDate
	private LocalDateTime questionRegTime;
}
