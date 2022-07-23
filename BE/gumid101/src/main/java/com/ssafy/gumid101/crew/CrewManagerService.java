package com.ssafy.gumid101.crew;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.UserDto;

public interface CrewManagerService {

	List<CrewDto> getMyCurrentCruew(Long userSeq) throws Exception;


}
