package com.ssafy.gumid101;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.gumid101.config.QueryDslConfig;
import com.ssafy.gumid101.crew.UserCrewJoinRepository;
import com.ssafy.gumid101.crew.manager.CrewManagerRepository;
import com.ssafy.gumid101.entity.CrewBoardEntity;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.UserCrewJoinEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.user.UserCustomRepositoryImpl;
import com.ssafy.gumid101.user.UserRepository;

@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@EnableJpaRepositories
@Import(QueryDslConfig.class)
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
	void crewManagerRepositoryFindUserSeqActiveTest() {

		// given
		UserEntity user = UserEntity.builder().email("test@kr.coke").nickName("테스터").height(20).weight(10).build();

		CrewEntity endCrew = CrewEntity.builder().crewCost(10).crewDateEnd(LocalDateTime.now())
				.crewDescription("테스트 크루").crewGoalAmount(3).crewGoalDays(3).crewMaxMember(30).crewName("훠")
				.crewGoalType("distance").crewTimeStart(LocalTime.of(9, 10)).crewTimeEnd(LocalTime.of(10, 1))
				.crewDateStart(LocalDateTime.now().minusMonths(1L)).build();

		CrewEntity notEndCrew = CrewEntity.builder().crewCost(100).crewDateStart(LocalDateTime.now()).crewGoalAmount(3)
				.crewGoalDays(3).crewMaxMember(30).crewGoalType("distance").crewName("하와아ㅘ")
				.crewTimeStart(LocalTime.of(9, 10)).crewTimeEnd(LocalTime.of(10, 1))
				.crewDateEnd(LocalDateTime.now().plusMonths(1)).build();

		crRepo.save(endCrew);
		crRepo.save(notEndCrew);
		userRepo.save(user);

		UserCrewJoinEntity ucj = UserCrewJoinEntity.builder().build();
		ucj.setCrewEntity(endCrew);
		ucj.setUserEntity(user);
		ucrRepo.save(ucj);

		ucj = UserCrewJoinEntity.builder().build();
		ucj.setCrewEntity(notEndCrew);
		ucj.setUserEntity(user);
		ucrRepo.save(ucj);

		// given
		List<UserCrewJoinEntity> ss = ucrRepo.findAll();

		ss.forEach((item) -> {
			System.out.println(item.getCrewUserRegTime() + "----" + item.getCrewUserSeq());
		});

		assertNotEquals(0, userRepo.findAll().size());
		assertEquals(2, crRepo.findAll().size());

		assertEquals(2, ucrRepo.findAll().size());

		// when1
		List<CrewEntity> crew1 = crRepo.findAll();
		// then1
		assertEquals(2, crew1.size());
		// when2

		System.out.println(LocalDateTime.now());
		System.out.println(crew1.get(0).getCrewDateEnd());
		System.out.println(crew1.get(1).getCrewDateEnd());
		List<CrewEntity> crews = crRepo.findByUserSeqActive(user, LocalDateTime.now());

		// then2
		if(crews.size() == 0) {
			System.out.println("s");
			System.out.println();
		}
		assertEquals(1, crews.size());

		
		
	}

	@DisplayName("크루 삭제 정상 시나리오")
	@Test
	void crewDelete() throws Exception {

		// given
		UserEntity user = UserEntity.builder().point(0).email("test@kr.coke").nickName("테스터크루장").height(20).weight(10)
				.build();
		UserEntity user2 = UserEntity.builder().point(0).email("te222st@kr.coke").nickName("테스터22").height(20)
				.weight(10).build();

		CrewEntity crew = CrewEntity.builder().crewCost(5).crewDateStart(LocalDateTime.now().plusDays(1))
				.crewGoalAmount(3).crewGoalDays(3).crewMaxMember(30).crewGoalType("distance").crewName("하와아ㅘ")
				.crewTimeStart(LocalTime.of(9, 10)).crewTimeEnd(LocalTime.of(10, 1))
				.crewDateEnd(LocalDateTime.now().plusMonths(1)).build();
		crew.setManagerEntity(user); // 크루장 설정

		userRepo.save(user);
		userRepo.save(user2);

		crRepo.save(crew);

		UserCrewJoinEntity ucj = UserCrewJoinEntity.builder().build();
		ucj.setUserEntity(user);
		ucj.setCrewEntity(crew);
		ucrRepo.save(ucj);

		ucj = UserCrewJoinEntity.builder().build();
		ucj.setUserEntity(user2);
		ucj.setCrewEntity(crew);
		ucrRepo.save(ucj);
		//벌크
		int k = ucrRepo.pointRefunds(crew, crew.getCrewCost());
System.out.println("영향 행 " + k);

		userRepo.flush();
		userRepo.findAll().forEach((qwe)->{System.out.println("asd:"+qwe.getPoint());});

		assertEquals(user.getPoint(), 5);
		assertEquals(user2.getPoint(), 5);
	}
	
	@Test
	public void localDateTimeTest() {
		LocalDateTime lt = LocalDateTime.parse("2016-10-31");
		
		System.out.println(lt);
	}
	
	@Test
	void findUserBoardsWithOffestAndSize() {
		
		UserEntity user1 = UserEntity.builder().email("test@kr.coke").nickName("테스터").height(20).weight(10).build();
		UserEntity user2 = UserEntity.builder().email("t2222@kr.coke").nickName("테2222스터").height(20).weight(103).build();

		userRepo.save(user1);
		userRepo.save(user2);
		
		CrewBoardEntity cbe1 = new CrewBoardEntity();
		cbe1.setCrewBoardRegTime(LocalDateTime.now());
		cbe1.setCrewBoardContent("보드11 \n");
		cbe1.setImgFile(null);
		cbe1.setUserEntity(user1);
		
		
		CrewBoardEntity cbe2 = new CrewBoardEntity();
		cbe1.setCrewBoardRegTime(LocalDateTime.now());
		cbe1.setCrewBoardContent("보드22 \n");
		cbe1.setImgFile(null);
		cbe1.setUserEntity(user2);
		
		//crew
		userRepo.findUserBoardsWithOffestAndSize(user1, 0L, 10L);
		
	}
}
