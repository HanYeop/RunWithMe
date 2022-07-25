package com.ssafy.gumid101.totalranking;

import java.util.List;

import com.ssafy.gumid101.res.RankingDto;

public interface TotalRankingService {

	List<RankingDto> getRankingByType(String rankingType, Long size, Long offset)throws Exception;

}
