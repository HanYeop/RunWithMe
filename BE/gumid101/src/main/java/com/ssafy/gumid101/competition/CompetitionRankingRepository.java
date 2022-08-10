package com.ssafy.gumid101.competition;

import java.util.List;

import com.ssafy.gumid101.res.RankingDto;

public interface CompetitionRankingRepository {

	List<RankingDto> getCompetitionRankingList(Long size, Long offset);

	RankingDto getCompetitionUserRanking(Long competitionSeq, Long userSeq);

}
