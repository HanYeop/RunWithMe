package com.ssafy.gumid101.crew.manager;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.RecruitmentParamsDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.res.CrewFileDto;
import com.ssafy.gumid101.res.EndCrewFileDto;

public interface CrewManagerService {
	CrewFileDto createCrew(MultipartFile image, CrewDto crewDto, UserDto tokenUser) throws Exception;
	
	List<?> getMyCurrentCrew(Long userSeq) throws Exception;

	int deleteCrew(long crewSeq, long userSeq)throws Exception;

	int exitCrew(long crewSeq, Long userSeq)throws Exception;

	List<CrewFileDto> crewSearcheByRecruitmentParams(RecruitmentParamsDto paramsDto)throws Exception;


	CrewFileDto getCrewDetail(Long crewId) throws Exception;

	Boolean isUserCrewMember(Long userSeq, Long crewSeq) throws Exception;

	Boolean crewFinishPoint(Long crewSeq) throws Exception;

	List<Long> getFinishAndNonDistributeCrews() throws Exception;

	List<EndCrewFileDto> getMyEndCrew(Long userSeq);

	
}
