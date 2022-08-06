package com.ssafy.gumid101.crew;

import java.util.List;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.CoordinateDto;
import com.ssafy.gumid101.dto.RecordCoordinateDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.req.PasswordDto;
import com.ssafy.gumid101.res.CrewUserDto;
import com.ssafy.gumid101.res.RunRecordResultDto;

public interface CrewService {

	CrewUserDto joinCrew(Long userSeq, Long crewId, Optional<PasswordDto> password) throws Exception;


	RunRecordResultDto insertUserRunRecordAsCrew(Long userSeq, Long crewId, RunRecordDto runRecord, MultipartFile imgFile) throws Exception;


	int setRecordCooldinate(Long recordSeq, List<CoordinateDto> cooldinates)throws Exception;


	List<RecordCoordinateDto> getCoordinateByRunRecordSeq(Long recordSeq) throws Exception;

}
