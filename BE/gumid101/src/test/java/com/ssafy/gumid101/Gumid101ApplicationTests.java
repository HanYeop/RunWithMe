package com.ssafy.gumid101;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.ssafy.gumid101.crew.CrewManagerRepository;
import com.ssafy.gumid101.crew.UserCrewJoinRepository;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.UserCrewJoinEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.user.UserRepository;

@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class Gumid101ApplicationTests {

	@Autowired
	private CrewManagerRepository crRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserCrewJoinRepository ucrRepo;
	
	
	@Test
	void test() {
		System.out.println("qwe");
	}
	
	@DisplayName("특정 유저, 현재 진행중 크루")
	@Test
	@Transactional
	void crewManagerRepositoryFindUserSeqActiveTest() {
		
		//given
		UserEntity user = UserEntity.builder().email("test@kr.coke").nickName("테스터").height(20).weight(10).build();
		CrewEntity endCrew = CrewEntity.builder().
				crewCost(10).
				crewDateEnd(LocalDateTime.now())
				.crewDescription("테스트 크루")
				.crewDateStart(LocalDateTime.now().minusMonths(1L))
				.build();
		CrewEntity notEndCrew = CrewEntity.builder()
				.crewCost(100)
				.crewDateEnd(LocalDateTime.now().plusMonths(1))
				.build();
		crRepo.save(endCrew);
		crRepo.save(notEndCrew);
		userRepo.save(user);
		
		UserCrewJoinEntity ucj = new UserCrewJoinEntity();
		ucj.setCrewEntity(endCrew);
		ucj.setUserEntity(user);
		ucrRepo.save(ucj);
		ucj = new UserCrewJoinEntity();
		ucj.setCrewEntity(notEndCrew);
		ucj.setUserEntity(user);
		
		ucrRepo.save(ucj);
		//given
		List<UserCrewJoinEntity> ss =ucrRepo.findAll();
		
		ss.forEach((item)->{
			System.out.println(item.getCrewUserRegTime()+"----"+item.getCrewUserSeq());
		});
		
		
		assertNotEquals(0, userRepo.findAll().size());
		assertEquals(2, crRepo.findAll().size());
		
		
		assertEquals(2, ucrRepo.findAll().size());
		
		//when1
		List<CrewEntity> crew1= crRepo.findAll();
		//then1
		assertEquals(2, crew1.size());
		//when2
		List<CrewEntity> crews = crRepo.findByUserSeqActive(user, LocalDateTime.now());
		//then2
		assertEquals(1, crews.size());
		
	}

}
