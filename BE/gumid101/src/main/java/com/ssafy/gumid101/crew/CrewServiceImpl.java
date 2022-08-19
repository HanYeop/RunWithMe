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

		LocalDateTime crewStartDateAndTime = LocalDateTime.of(crew.getCrewDateStart().toLocalDate(),
				crew.getCrewTimeStart());
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
		int beforeSize = coordinates.size();
		/* 여기서부터 좌표최적화 시작 
		 * 문제발생 시 이 아래로 주석치면 됨
		 * */
		List<CoordinateDto> saveCoordinate = new ArrayList<>();
		try {
			if (coordinates.size() > 0) {
				// 시작 좌표는 무조건 저장
				saveCoordinate.add(coordinates.get(0));
				// 시작 좌표 다음 좌표부터 검사
				int checkIdx = 1;
				while (checkIdx < coordinates.size()) {
					if (checkIdx == coordinates.size() - 1) {
						// 마지막 좌표는 무조건 저장
						saveCoordinate.add(coordinates.get(checkIdx));
						break;
					}
					// 마지막으로 저장된 점을 불러온다.
					CoordinateDto before = saveCoordinate.get(saveCoordinate.size() - 1);
					// 마지막으로 저장된 지점과 지금 저장해보려는 지점 사이의 거리를 구한다.
					double lastDistance = distance(before, coordinates.get(checkIdx));
					if (lastDistance < 10) {
						// 만약 10미터 미만으로 너무 가까운 점이면 저장하지 않고 넘긴다.
						checkIdx++;
						continue;
					}
					if (lastDistance >= 100) {
						// 만약 100미터 이상으로 너무 먼 점이면 이후 각도 상관없이 무조건 저장한다.
						saveCoordinate.add(coordinates.get(checkIdx++));
						continue;
					}
					// 적당한 거리라면 다음 점과의 각도를 봐서 거의 평면이면 저장하지 않는다.
					// 남한 정도 범위인 위도 36~38도에서는 위도 1도당 111km,
					// 위도 n도에서의 경도 1도당 거리는 111km * cos(n)
					// 세 점은 가깝기 때문에 사실상 아무 한 점 기준 위도를 잡고 위도와 경도 좌표를 평면벡터화 해도 오차가 없다.
					Vector v1 = getVector(before, coordinates.get(checkIdx));
					Vector v2 = getVector(coordinates.get(checkIdx), coordinates.get(checkIdx + 1));
					
					double cosine = 
							getVectorDotProduct(v1, v2) / 
							(getVectorDistance(v1) * 
									getVectorDistance(v2));
					if (lastDistance < 50) {
						// 거리가 가까울 땐 여유범위 +- 5도
						if (cosine > Math.cos(deg2rad(20))) {
							// 거의 직선일 경우 저장하지 않고 버림
							checkIdx++;
							continue;
						}
					}
					else {
						// 거리가 멀어지면 여우범위 줄이기
						if (cosine > Math.cos(deg2rad(10))) {
							// 거의 직선일 경우 저장하지 않고 버림
							checkIdx++;
							continue;
						}
					}
					// 위의 모든 경우를 뚫고(?) 온 경우 저장함.
					saveCoordinate.add(coordinates.get(checkIdx++));
				}
			}
			coordinates = saveCoordinate;
		}catch (Exception e) {
			// 최적화 로직에 에러가 생기면 최적화되지 않은 좌표로 저장
		}
		/* 여기서 좌표최적화 종료 
		 * 문제발생 시 이 위로 주석치면 됨.
		 * */
		int afterSize = coordinates.size();
		log.info("최적화 전 좌표 개수 : " + beforeSize + ", 최적화 후 좌표 개수 : " + afterSize);
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

	/**
	 * 두 지점간의 거리 계산
	 *
	 * @param lat1 지점 1 위도
	 * @param lon1 지점 1 경도
	 * @param lat2 지점 2 위도
	 * @param lon2 지점 2 경도
	 * @return
	 */
	private double distance(double lat1, double lon1, double lat2, double lon2) {

		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1609.344;

		return (dist);
	}

	private double distance(CoordinateDto point1, CoordinateDto point2) {
		return distance(point1.getLatitude(), point1.getLongitude(), point2.getLatitude(), point2.getLongitude());
	}

	// This function converts decimal degrees to radians
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	// This function converts radians to decimal degrees
	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	private class Vector {
		double x;
		double y;

		public Vector(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	// 두 좌표를 위도에 따른 경도의 거리를 적용해서 최대한 오차없는 평면벡터화 한 것
	private Vector getVector(CoordinateDto point1, CoordinateDto point2) {
		return new Vector((point2.getLatitude() - point1.getLatitude()) / Math.cos(deg2rad(point2.getLatitude())),
				point2.getLongitude() - point1.getLongitude());
	}
	
	// 벡터의 크기
	private double getVectorDistance(Vector v) {
		return Math.sqrt(v.x * v.x + v.y * v.y);
	}
	
	// 벡터의 내적
	private double getVectorDotProduct(Vector v1, Vector v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}

}
