package com.ssafy.gumid101.crew;

import com.ssafy.gumid101.res.CrewUserDto;

public interface CrewService {

	CrewUserDto joinCrew(Long userSeq, long crewId, String passwrod) throws Exception;

	void insertUserRunRecordAsCrew(Long userSeq, Long crewId);

}
