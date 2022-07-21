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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Table(name="t_crew_board")
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class CrewBoardEntity {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long crewBoardSeq;
	
	@Column(name="crew_board_content")
	private String crewBoardContent;
	
	@CreatedDate
	@Column(name="crew_board_reg_time")
	private LocalDateTime crewBoardRegTime;
	
	@OneToOne
	@JoinColumn(name="img_seq")
	private ImageFileEntity imgFile;
	
	@ManyToOne
	@JoinColumn(name="crew_seq")
	private CrewEntity crewEntity;
	
	@ManyToOne
	@JoinColumn(name="user_seq")
	private UserEntity userEntity;
}
