package com.ssafy.gumid101.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssafy.gumid101.competition.CompetitionStatus;
import com.ssafy.gumid101.entity.CompetitionEntity;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionDto implements Serializable {

	@ApiParam(value = "대회 번호")
	private Long competitionSeq;

	@ApiParam(value = "대회 이름", required = true)
	private String competitionName;
	
	@ApiParam(value = "대회 내용", required = true)
	private String competitionContent;

	@ApiParam(value = "대회 시작 날짜. (yyyy-MM-dd 00:00:00)", required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime competitionDateStart;

	@ApiParam(value = "대회 종료 날짜. (yyyy-MM-dd 23:59:59)", required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime competitionDateEnd;

	@ApiParam(value = "개인별 상태(유저별 참가여부 등)")
	@Enumerated(EnumType.STRING)
	private CompetitionStatus competitionStatus;

	public static CompetitionDto of(CompetitionEntity competitionEntity) {
		if (competitionEntity == null)
			return null;

		return CompetitionDto.builder() //
				.competitionSeq(competitionEntity.getCompetitionSeq()) //
				.competitionDateStart(competitionEntity.getCompetitionDateStart()) //
				.competitionDateEnd(competitionEntity.getCompetitionDateEnd()) //
				.competitionName(competitionEntity.getCompetitionName()) //
				.competitionContent(competitionEntity.getCompetitionContent()) //
				.build(); //
	}

}