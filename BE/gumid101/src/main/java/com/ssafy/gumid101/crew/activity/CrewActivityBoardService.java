package com.ssafy.gumid101.crew.activity;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.res.CrewBoardFileDto;

public interface CrewActivityBoardService {

	CrewBoardFileDto writeBoard(UserDto writerDto, MultipartFile image, CrewBoardDto crewBoardDto, Long crewSeq)
			throws Exception;


	boolean deleteCrewBoard(Long crewSeq, Long boardSeq) throws Exception;

	List<CrewBoardFileDto> getCrewBoards(Long crewSeq, Integer size,Long maxCrewBoardSeq) throws Exception;

}
