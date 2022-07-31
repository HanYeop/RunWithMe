package com.ssafy.gumid101.crew.activity;

import java.util.List;

import com.ssafy.gumid101.dto.CrewTotalRecordDto;
import com.ssafy.gumid101.dto.RankingParamsDto;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.entity.RunRecordEntity;
import com.ssafy.gumid101.res.RankingDto;

public interface CrewActivityRunDslRepository{

	List<RunRecordEntity> getRunRecords(RecordParamsDto condition);

	List<RunRecordDto> selectByCrewAndUserSeqWithOffsetSize(RecordParamsDto recordParamsDto);

	List<RankingDto> selectAllCrewRanking(RankingParamsDto rankingParamsDto);


	/**
	 * 크루에서 나의 종합 기록
	 */
	CrewTotalRecordDto selectByCrewSeqAnduserSeq(Long crewSeq, Long userSeq);

}
