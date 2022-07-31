package com.ssafy.gumid101.crew.manager;

import java.util.List;

import com.ssafy.gumid101.dto.RecruitmentParamsDto;
import com.ssafy.gumid101.res.CrewFileDto;

public interface CrewManagerCustomRepository {
	List<CrewFileDto> crewSearcheByRecruitmentParams(RecruitmentParamsDto paramsDto);
}
