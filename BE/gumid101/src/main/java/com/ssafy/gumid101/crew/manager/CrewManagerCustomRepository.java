package com.ssafy.gumid101.crew.manager;

import java.util.List;

import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.RecruitmentParamsDto;

public interface CrewManagerCustomRepository {
	List<CrewDto> crewSearcheByRecruitmentParams(RecruitmentParamsDto paramsDto);
}
