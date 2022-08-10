package com.ssafy.gumid101.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ssafy.gumid101.dto.CompetitionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="t_competition")
@Builder
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "competition_seq")
	private Long competitionSeq;
	
	@Column(nullable = false, name="competition_date_start")
	private LocalDateTime competitionDateStart; //크루 시작날짜
	
	@Column(nullable = false, name="competition_date_end")
	private LocalDateTime competitionDateEnd; //크루 끝나는 날짜
	
	@Column(nullable = false, name = "competition_name")
	private String competitionName;
	
	@Column(nullable = false, name = "competition_content")
	private String competitionContent;

	@JoinColumn(name = "img_seq")
	@OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
	private ImageFileEntity competitionImageFile;
	
	@Column(nullable = false, name = "check_yn")
	private String checkYn;
	
	@OneToMany(mappedBy = "competitionEntity")
	private List<CompetitionUserRecordEntity> competitionTotalRecordEntitys;
	
	@PrePersist
	public void setting() {
		this.checkYn = "N";
	}
	
	public static CompetitionEntity of(CompetitionDto competitionDto) {
		if (competitionDto == null) {
			return null;
		}
		return CompetitionEntity.builder() //
				.competitionSeq(competitionDto.getCompetitionSeq()) //
				.competitionDateStart(competitionDto.getCompetitionDateStart()) //
				.competitionDateEnd(competitionDto.getCompetitionDateEnd()) //
				.competitionName(competitionDto.getCompetitionName()) //
				.competitionContent(competitionDto.getCompetitionContent()) //
				.build(); //
	}
}
