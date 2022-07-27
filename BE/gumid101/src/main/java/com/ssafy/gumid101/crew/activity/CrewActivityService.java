package com.ssafy.gumid101.crew.activity;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.RecordParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.UserDto;

public interface CrewActivityService {
	List<RunRecordDto> getCrewRecordList(RecordParamsDto recordParamsDto);
	CrewBoardDto writeBoard(MultipartFile image, UserDto tokenUser, CrewBoardDto content) throws Exception;
	List<RunRecordDto> getMyRecordList(RecordParamsDto recordParamsDto);
	Boolean getRunabletoday(Long userSeq, Long crewSeq) throws Exception;
}
