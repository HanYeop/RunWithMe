package com.ssafy.gumid101.res;

import com.ssafy.gumid101.competition.CompetitionResultStatus;

import lombok.Data;

@Data
public class RankingDto {

	private String userName;
	private Long userSeq;
	private CompetitionResultStatus competitionResult;
	private Integer rankingIndex;
	private Integer rankingValue;
	private Long imgSeq;
}
