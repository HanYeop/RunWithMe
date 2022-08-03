package com.ssafy.gumid101.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ssafy.gumid101.dto.CrewBoardDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="t_crew_board")
@Entity
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class CrewBoardEntity {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long crewBoardSeq;
	
	@Column(name="crew_board_content")
	private String crewBoardContent;
	
	@CreatedDate
	@Column(name="crew_board_reg_time")
	private LocalDateTime crewBoardRegTime;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="img_seq")
	private ImageFileEntity imgFile;
	
	@ManyToOne
	@JoinColumn(name="crew_seq")
	private CrewEntity crewEntity;
	
	@ManyToOne
	@JoinColumn(name="user_seq")
	private UserEntity userEntity;
	
	// 다른 Entity는 dto에 없는데 어떡하지?
	public static CrewBoardEntity of(CrewBoardDto dto) {
		return new CrewBoardEntityBuilder()
				.crewBoardSeq(dto.getCrewBoardSeq())
				.crewBoardContent(dto.getCrewBoardContent())
				.build();
	}
}
