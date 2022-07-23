package com.ssafy.gumid101.crew;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewManagerServiceImpl implements CrewManagerService {

	private final CrewManagerRepository cmRepo;
	private final UserRepository userRepo;

	/**
	 * userDto...
	 */
	@Override
	public List<CrewDto> getMyCurrentCruew(Long userSeq) throws Exception {
		// new jpabook.jpashop.repository.order.simplequery.
		// OrderSimpleQueryDto(o.id, m.name, o.status, o.orderDate, d.address)

		UserEntity user = userRepo.findById(userSeq).orElseThrow(() -> {
			return new NotFoundUserException("나의 현재 진행중 크루를 찾는 중, 유저를 특정할 수 없습니다.");
		});
		
		List<CrewEntity> crews = cmRepo.findByUserSeqActive(user, LocalDateTime.now());

		List<CrewDto> crewList = crews.stream().map((entity) -> {
			return CrewDto.of(entity);
		}).collect(Collectors.toList());

		return crewList;
	}

}
