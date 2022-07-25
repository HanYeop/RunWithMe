package com.ssafy.gumid101.crew.manager;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.RecruitmentParamsDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.res.CrewFileDto;

public interface CrewManagerService {
	CrewFileDto createCrew(MultipartFile image, CrewDto crewDto, UserDto tokenUser) throws Exception;
	
	List<?> getMyCurrentCruew(Long userSeq) throws Exception;

	int deleteCrew(long crewSeq, long userSeq)throws Exception;

	int exitCrew(long crewSeq, Long userSeq)throws Exception;

	List<CrewDto> crewSearcheByRecruitmentParams(RecruitmentParamsDto paramsDto)throws Exception;
}
