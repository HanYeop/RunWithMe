package com.ssafy.gumid101.totalranking;

import java.util.List;

import com.ssafy.gumid101.res.RankingDto;

public interface CrewTotalRecordCustomRepository {

	public List<RankingDto> getUserTotalRankingOptionType(String type, Long size, Long offset) ;
	public RankingDto getMyTotalRanking(String type,Long userSeq);
}
