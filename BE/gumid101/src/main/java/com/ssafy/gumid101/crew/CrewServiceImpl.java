package com.ssafy.gumid101.crew;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.achievement.AchieveType;
import com.ssafy.gumid101.achievement.AchievementCompleteRepository;
import com.ssafy.gumid101.achievement.AchievementRepository;
import com.ssafy.gumid101.aws.S3FileService;
import com.ssafy.gumid101.competition.CompetitionRepository;
import com.ssafy.gumid101.competition.CompetitionUserRecordRepository;
import com.ssafy.gumid101.crew.manager.CrewManagerRepository;
import com.ssafy.gumid101.crew.manager.CrewManagerService;
import com.ssafy.gumid101.customexception.CrewNotFoundException;
import com.ssafy.gumid101.customexception.CrewPermissonDeniedException;
import com.ssafy.gumid101.customexception.IllegalParameterException;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.customexception.PasswrodNotMatchException;
import com.ssafy.gumid101.dto.AchievementDto;
import com.ssafy.gumid101.dto.CoordinateDto;
import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.CrewTotalRecordDto;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.RecordCoordinateDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.AchievementCompleteEntity;
import com.ssafy.gumid101.entity.AchievementEntity;
import com.ssafy.gumid101.entity.CompetitionEntity;
import com.ssafy.gumid101.entity.CompetitionUserRecordEntity;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.CrewTotalRecordEntity;
import com.ssafy.gumid101.entity.ImageFileEntity;
import com.ssafy.gumid101.entity.RunRecordEntity;
import com.ssafy.gumid101.entity.UserCrewJoinEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.imgfile.ImageDirectory;
import com.ssafy.gumid101.imgfile.ImageFileRepository;
import com.ssafy.gumid101.req.PasswordDto;
import com.ssafy.gumid101.res.CrewUserDto;
import com.ssafy.gumid101.res.RunRecordResultDto;
import com.ssafy.gumid101.user.UserRepository;

import io.jsonwebtoken.lang.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewServiceImpl implements CrewService {

	private final CrewManagerRepository crewManagerRepo;
	private final CrewManagerService crewManagerServ;
	private final UserCrewJoinRepository ucJoinRepo;
	private final UserRepository userRepo;
	private final UserCrewRunRecordRepository userCrewTotalRunRepo;// (유저,크루) 누적
	private final RunRecordRepository runRecordRepo;
	private final S3FileService s3FileService;
	private final ImageFileRepository imageRepo;
	private final AchievementRepository achiveRepo;
	private final AchievementCompleteRepository accRepo;
	private final CompetitionUserRecordRepository competitionUserRecordRepo;
	private final CompetitionRepository competitionRepo;

	@Transactional
	@Override
	public CrewUserDto joinCrew(Long userSeq, Long crewSeq, Optional<PasswordDto> passwordDto) throws Exception {

		CrewEntity crew = crewManagerRepo.findById(crewSeq)
				.orElseThrow(() -> new CrewNotFoundException("크루 가입 중, 크루를 특정할 수 없습니다."));
		
		LocalDateTime crewStartDateAndTime = LocalDateTime.of(crew.getCrewDateStart().toLocalDate(), crew.getCrewTimeStart());
		if (crewStartDateAndTime.isBefore(LocalDateTime.now())) {
			throw new CrewPermissonDeniedException("이미 시작한 크루는 가입할 수 없습니다.");
		}
		
		if (crewManagerServ.isUserCrewMember(userSeq, crewSeq)) {
			throw new CrewPermissonDeniedException("이미 해당 크루원입니다.");
		}

		// 인원수 체크
		if (ucJoinRepo.findAllByCrewEntity(crew).size() >= crew.getCrewMaxMember()) {
			throw new CrewPermissonDeniedException("크루 인원 수를 초과했습니다.");
		}

		UserEntity user = userRepo.findWithLockingByUserSeq(userSeq)
				.orElseThrow(() -> new UsernameNotFoundException("크루 가입 중 유저를 특정할 수 없습니다."));
		// 가입비 체크
		if (user.getPoint() < crew.getCrewCost()) {
			throw new CrewPermissonDeniedException("포인트가 부족하여 가입이 불가능 합니다.");
		}
		user.setPoint(user.getPoint() - crew.getCrewCost());

		String password = null;
		if (passwordDto.isPresent()) {
			password = passwordDto.get().getPassword();
		}
		// 패스워드 체크
		if (Strings.hasLength(crew.getCrewPassword())) {
			if (password == null || crew.getCrewPassword().compareTo(password) != 0) {
				throw new PasswrodNotMatchException("크루 패스워드 불일치");
			}
		}

		UserCrewJoinEntity ucjEntity = UserCrewJoinEntity.builder().build();
		ucjEntity.setCrewEntity(crew);
		ucjEntity.setUserEntity(user);

		ucJoinRepo.save(ucjEntity);

		UserDto userDto = UserDto.of(ucjEntity.getUserEntity());
		CrewDto crewDto = CrewDto.of(ucjEntity.getCrewEntity());

		crewDto.setCrewMemberCount(ucJoinRepo.findCountCrewUser(crewSeq));

		CrewUserDto crewUserDto = new CrewUserDto(crewDto, userDto, ucjEntity.getCrewUserRegTime());

		return crewUserDto;
	}

	@Transactional
	@Override
	public RunRecordResultDto insertUserRunRecordAsCrew(Long userSeq, Long crewId, RunRecordDto runRecordDto,
			MultipartFile imgFile) throws Exception {

		if (imgFile == null) {
			throw new IllegalParameterException("저장할 경로 사진이 필요합니다.");
		}

		if (runRecordDto.getRunRecordRunningTime() == null || runRecordDto.getRunRecordRunningTime() == 0) {
			throw new IllegalParameterException("달린 시간이 0인 기록은 저장할 수 없습니다.");
		}

		UserEntity userEntity = userRepo.findById(userSeq)
				.orElseThrow(() -> new NotFoundUserException("러닝 완료 중, 유저를 특정할 수 없습니다."));

		CrewEntity crewEntity = crewManagerRepo.findById(crewId)
				.orElseThrow(() -> new CrewNotFoundException("러닝 완료 중 , 크루를 특정할 수 없습니다."));

		if (crewEntity.getCrewDateStart().isAfter(runRecordDto.getRunRecordStartTime())
				|| crewEntity.getCrewDateEnd().isBefore(runRecordDto.getRunRecordStartTime())) {
			throw new CrewPermissonDeniedException("크루 활동 기간이 아닐 때의 기록입니다.");
		}
		if (crewEntity.getCrewTimeStart().isAfter(runRecordDto.getRunRecordStartTime().toLocalTime())
				|| crewEntity.getCrewTimeEnd().isBefore(runRecordDto.getRunRecordStartTime().toLocalTime())) {
			throw new CrewPermissonDeniedException("크루 활동 시간이 아닐 때의 기록입니다.");
		}
		List<RunRecordEntity> myToday = null;
		try {
			myToday = runRecordRepo.findByUserEntityAndCrewEntityAndRunRecordStartTimeBetween(userEntity, crewEntity, //
					runRecordDto.getRunRecordStartTime().withHour(0).withMinute(0).withSecond(0), //
					runRecordDto.getRunRecordStartTime().withHour(23).withMinute(59).withSecond(59)); //
		} catch (Exception e) {
			throw new CrewPermissonDeniedException("기록 조회에 실패했습니다.");
		}
		if (myToday != null && myToday.size() > 0) {
			throw new CrewPermissonDeniedException("이미 오늘자 기록이 존재합니다.");
		}
		// 1.크루 런레코드 테이블에 저장
		// runRecordRepo
		RunRecordEntity runRecord = RunRecordEntity.builder().runRecordStartTime(runRecordDto.getRunRecordStartTime())
				.runRecordEndTime(runRecordDto.getRunRecordEndTime())
				.runRecordRunningTime(runRecordDto.getRunRecordRunningTime())
				.runRecordRunningDistance(runRecordDto.getRunRecordRunningDistance())
				.runRecordCalorie(runRecordDto.getRunRecordRunningCalorie())
				.runRecordLat(runRecordDto.getRunRecordRunningLat()).runRecordLng(runRecordDto.getRunRecordRunningLng())
				.runRecordRegTime(LocalDateTime.now()).build();

		// 입력에 혹시모를 null 들어오는지 체크해야함.
		Integer plusPoint = 0;
		if (crewEntity.getCrewGoalType().equals("distance")) {
			if (crewEntity.getCrewGoalAmount() <= runRecord.getRunRecordRunningDistance()) {
				runRecord.setRunRecordCompleteYN("Y");
				// 1km(1000m) 당 50p
				plusPoint = crewEntity.getCrewGoalAmount() / 20;
				userEntity.setPoint(userEntity.getPoint() + plusPoint);
			} else {
				runRecord.setRunRecordCompleteYN("N");
			}
		} else {
			if (crewEntity.getCrewGoalAmount() <= runRecord.getRunRecordRunningTime()) {
				runRecord.setRunRecordCompleteYN("Y");
				// 10분(600s) 당 50p
				plusPoint = crewEntity.getCrewGoalAmount() / 12;
				userEntity.setPoint(userEntity.getPoint() + plusPoint);
			} else {
				runRecord.setRunRecordCompleteYN("N");
			}
		}

		// 기본 속성들 다 넣어주고

		// 이미지가 없을 리 없음 ..
		ImageFileDto imageDto = s3FileService.upload(imgFile, ImageDirectory.RUN_RECORD.getPath());

		ImageFileEntity image = ImageFileEntity.builder().imgOriginalName(imageDto.getImgOriginalName())
				.imgSavedName(imageDto.getImgSavedName()).imgSavedPath(imageDto.getImgSavedPath()).build();
		imageRepo.save(image); // 이미지 저장하고
		//
		runRecordRepo.save(runRecord); // 퍼시스턴스 영역에 등록
		// 등록한 후에 관계 설정
		runRecord.setImageFile(image);
		runRecord.setUserEntity(userEntity);
		runRecord.setCrewEntity(crewEntity);

		// .runRecordCompleteYN()
		// .build();
		// 2.크루 런레코드 누적 테이블을 갱신하고.

		CrewTotalRecordEntity userCrewTotalEntity = null;
		try {

		} catch (Exception e) {
			log.warn("크루-유저 누적 기록 뭔가 이상한다. 무결성 침해가 우려된다.");
		}

		// 베타락을 얻어온다.
		userCrewTotalEntity = userCrewTotalRunRepo.findWithLockByUserEntityAndCrewEntity(userEntity, crewEntity);
		if (userCrewTotalEntity == null) {
			userCrewTotalEntity = new CrewTotalRecordEntity();
			userCrewTotalEntity.setCrewEntity(crewEntity);
			userCrewTotalEntity.setUserEntity(userEntity);
			userCrewTotalRunRepo.save(userCrewTotalEntity);
		}
		// 최대 거리
		userCrewTotalEntity.setTotalLongestDistance(
				Math.max(userCrewTotalEntity.getTotalLongestDistance(), runRecordDto.getRunRecordRunningDistance()));
		// 최대 뛴 시간
		userCrewTotalEntity.setTotalLongestTime(
				Math.max(userCrewTotalEntity.getTotalLongestTime(), runRecordDto.getRunRecordRunningTime()));
		// 총 뛴 시간
		userCrewTotalEntity.setTotalTime(userCrewTotalEntity.getTotalTime() + runRecordDto.getRunRecordRunningTime());
		// 총 뛴 거리
		userCrewTotalEntity
				.setTotalDistance(userCrewTotalEntity.getTotalDistance() + runRecordDto.getRunRecordRunningDistance());
		// 총 칼로리 소모
		userCrewTotalEntity.setTotalCalorie(userCrewTotalEntity.getTotalCalorie() + runRecord.getRunRecordCalorie());

		CrewTotalRecordDto userCrewDto = CrewTotalRecordDto.of(userCrewTotalEntity);
		userCrewDto.setTotalAvgSpeed(3.6 * userCrewTotalEntity.getTotalDistance().doubleValue()
				/ (userCrewTotalEntity.getTotalTime() == 0 ? Long.MAX_VALUE : userCrewTotalEntity.getTotalTime()));

		// 3.업적 로직 계산
		List<AchievementDto> newCompleteDtoList = getNewCompleteAchiveMent(userEntity, runRecord);

		// 4. 시즌제 대회 관련 처리
		competitionCheck(userEntity, runRecord);

		return new RunRecordResultDto(RunRecordDto.of(runRecord), newCompleteDtoList);
	}

	private List<AchievementDto> getNewCompleteAchiveMent(UserEntity userEntity, RunRecordEntity runRecordEntity)
			throws Exception {

		Long runCount = runRecordRepo.countByUserEntity(userEntity);
		CrewTotalRecordDto crewTotalRecordDto = userRepo.getMyTotalRecord(userEntity);

		List<AchievementEntity> nonAchieveMents = achiveRepo.findNotAchivement(userEntity);
		List<AchievementEntity> achieveList = new ArrayList<>();
		// achiveMentType
		for (AchievementEntity nonAchieve : nonAchieveMents) {
			if (nonAchieve.getAchieveType() == AchieveType.DISTANCE) {
				if (nonAchieve.getAchiveValue() <= runRecordEntity.getRunRecordRunningDistance()) {
					achieveList.add(nonAchieve);
				}
			} else if (nonAchieve.getAchieveType() == AchieveType.TIME) {
				if (nonAchieve.getAchiveValue() <= runRecordEntity.getRunRecordRunningTime()) {
					achieveList.add(nonAchieve);
				}
			} else if (nonAchieve.getAchieveType() == AchieveType.TOTALDISTANCE) {
				if (nonAchieve.getAchiveValue() <= crewTotalRecordDto.getTotalDistance()) {
					achieveList.add(nonAchieve);
				}
			} else if (nonAchieve.getAchieveType() == AchieveType.TOTALTIME) {
				if (nonAchieve.getAchiveValue() <= crewTotalRecordDto.getTotalTime()) {
					achieveList.add(nonAchieve);
				}
			} else if (nonAchieve.getAchieveType() == AchieveType.RUNCOUNT) {
				if (nonAchieve.getAchiveValue() <= runCount) {
					achieveList.add(nonAchieve);
				}
			}
		}
		accRepo.saveAll(achieveList.stream().map((entity) -> AchievementCompleteEntity.builder()
				.achieveCompleteRegTime(LocalDateTime.now()).achieveEntity(entity).userEntity(userEntity).build())
				.collect(Collectors.toList()));
		return achieveList.stream().map((entity) -> {
			AchievementDto dto = AchievementDto.of(entity);
			dto.setAchieveRegTime(LocalDateTime.now());
			return dto;
		}).collect(Collectors.toList());
	}

	private void competitionCheck(UserEntity userEntity, RunRecordEntity runRecordEntity) {
		CompetitionEntity competitionEntity = competitionRepo
				.findByCompetitionDateStartBeforeAndCompetitionDateEndAfter(LocalDateTime.now(), LocalDateTime.now())
				.orElse(null);
		// 현재 진행중인 대회 없으면 끝
		if (competitionEntity == null) {
			return;
		}
		// 해당 유저가 참여한 대회가 아니면 끝
		CompetitionUserRecordEntity competitionUserEntity = competitionUserRecordRepo
				.findByUserEntityAndCompetitionEntity(userEntity, competitionEntity).orElse(null);
		if (competitionUserEntity == null) {
			return;
		}
		// 참여한 대회이면 기록 쌓기
		CompetitionUserRecordEntity competitionUserRecordEntity = competitionUserRecordRepo
				.findByUserEntityAndCompetitionEntity(userEntity, competitionEntity).get();
		competitionUserRecordEntity.setCompetitionDistance(
				competitionUserRecordEntity.getCompetitionDistance() + runRecordEntity.getRunRecordRunningDistance());
		competitionUserRecordEntity.setCompetitionTime(
				competitionUserRecordEntity.getCompetitionTime() + runRecordEntity.getRunRecordRunningTime());
		competitionUserRecordRepo.save(competitionUserRecordEntity);
	}

	@Transactional
	@Override
	public int setRecordCoordinate(Long recordSeq, List<CoordinateDto> coordinates) throws Exception {

		int[] results = runRecordRepo.coordinatesInsertBatch(recordSeq, coordinates);

		int success = 0;
		int fail = 0;
		for (int i = 0; i < results.length; i++) {
			if (results[i] == -2) {
				success++;
			} else {
				fail++;
			}
		}

		log.debug("좌표 뱃치 INSERT 중 - 성공 : {}  - 실패 : {}", success, fail);

		if (results.length == success) {
			return 1;
		} else {
			return 0;
		}

	}

	@Transactional
	@Override
	public List<RecordCoordinateDto> getCoordinateByRunRecordSeq(Long recordSeq) throws Exception {

		return runRecordRepo.getCoordinateByRunRecordSeq(recordSeq);
	}

}
