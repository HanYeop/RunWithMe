package com.ssafy.gumid101.crew;

import org.springframework.stereotype.Service;

import com.ssafy.gumid101.crew.manager.CrewManagerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewServiceImpl implements CrewService {

	private final CrewManagerRepository crewManagerRepo;
	private final UserCrewJoinRepository ucJoinRepo;
	
	@Override
	public int joinCrew(Long userSeq, long crewId, String passwrod) throws Exception {
		
		return 0;
	}

}
