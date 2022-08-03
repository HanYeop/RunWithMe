package com.ssafy.gumid101.crew.activity;

import java.util.List;

import com.ssafy.gumid101.dto.CrewTotalRecordDto;
import com.ssafy.gumid101.dto.RankingParamsDto;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.res.GraphRecordDto;
import com.ssafy.gumid101.res.RankingDto;

public interface CrewActivityService {
	List<RunRecordDto> getCrewRecordList(RecordParamsDto recordParamsDto);
	List<RunRecordDto> getMyRecordList(RecordParamsDto recordParamsDto);
	Boolean getRunabletoday(Long userSeq, Long crewSeq) throws Exception;
	List<RunRecordDto> getMyCrewRecordsByParam(RecordParamsDto recordParamsDto)throws Exception;
	List<RankingDto> getCrewRankingByParam(RankingParamsDto rankingParamsDto)throws Exception;
	CrewTotalRecordDto getMyCrewTotalRecord(Long userSeq, Long crewSeq) throws Exception;
	List<GraphRecordDto> getCrewMyGraphData(UserDto userDto, Long crewSeq, String goalType) throws Exception;
}
