package com.ssafy.gumid101.crew;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ssafy.gumid101.crew.manager.CrewManagerRepository;
import com.ssafy.gumid101.customexception.CrewNotFoundException;
import com.ssafy.gumid101.customexception.PasswrodNotMatchException;
import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.UserCrewJoinEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.res.CrewUserDto;
import com.ssafy.gumid101.user.UserRepository;

import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewServiceImpl implements CrewService {

	private final CrewManagerRepository crewManagerRepo;
	private final UserCrewJoinRepository ucJoinRepo;
	private final UserRepository userRepo;
	
	@Transactional
	@Override
	public CrewUserDto joinCrew(Long userSeq, long crewId, String password) throws Exception {
		
		UserEntity user = userRepo.findById(userSeq).orElseThrow(()->new UsernameNotFoundException("크루 가입 중 유저를 특정할 수 없습니다."));
		CrewEntity crew = crewManagerRepo.findById(crewId).orElseThrow(()->new CrewNotFoundException("크루 가입 중, 크루를 특정할 수 없습니다."));
		
		//가입비 내는 거 구현 안되있다.####
		
		if(Strings.hasLength(crew.getCrewPassword()) ) {
			if(crew.getCrewPassword().compareTo(password)!=0) {
				throw new PasswrodNotMatchException("크루 패스워드 불일치");
			}
		}
		
		
		UserCrewJoinEntity ucjEntity = UserCrewJoinEntity.builder().build();
		ucjEntity.setCrewEntity(crew);
		ucjEntity.setUserEntity(user);
		ucJoinRepo.save(ucjEntity);
		
		UserDto userDto = UserDto.of(ucjEntity.getUserEntity());
		CrewDto crewDto = CrewDto.of(ucjEntity.getCrewEntity());
		CrewUserDto crewUserDto = new CrewUserDto(crewDto,userDto,ucjEntity.getCrewUserRegTime());
		return crewUserDto;
	}

}
