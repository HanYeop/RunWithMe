package com.ssafy.gumid101.crew;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.aws.S3FileService;
import com.ssafy.gumid101.crew.manager.CrewManagerRepository;
import com.ssafy.gumid101.customexception.CrewNotFoundException;
import com.ssafy.gumid101.customexception.CrewPermissonDeniedException;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.customexception.PasswrodNotMatchException;
import com.ssafy.gumid101.dto.AchievementDto;
import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.CrewTotalRecordDto;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.CrewTotalRecordEntity;
import com.ssafy.gumid101.entity.ImageFileEntity;
import com.ssafy.gumid101.entity.RunRecordEntity;
import com.ssafy.gumid101.entity.UserCrewJoinEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.imgfile.ImageDirectory;
import com.ssafy.gumid101.imgfile.ImageFileRepository;
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
	private final UserCrewJoinRepository ucJoinRepo;
	private final UserRepository userRepo;
	private final UserCrewRunRecordRepository userCrewTotalRunRepo;// (유저,크루) 누적
	private final RunRecordRepository runRecordRepo;
	private final S3FileService s3FileService;
	private final ImageFileRepository imageRepo;
	@Transactional
	@Override
	public CrewUserDto joinCrew(Long userSeq, long crewId, String password) throws Exception {

		CrewEntity crew = crewManagerRepo.findById(crewId)
				.orElseThrow(() -> new CrewNotFoundException("크루 가입 중, 크루를 특정할 수 없습니다."));

		// 가입비 내는 거 구현 안되있다.####

		if (Strings.hasLength(crew.getCrewPassword())) {
			if (crew.getCrewPassword().compareTo(password) != 0) {
				throw new PasswrodNotMatchException("크루 패스워드 불일치");
			}
		}
		UserEntity user = userRepo.findWithLockingById(userSeq)
				.orElseThrow(() -> new UsernameNotFoundException("크루 가입 중 유저를 특정할 수 없습니다."));
		if (user.getPoint() < crew.getCrewCost()) {
			throw new CrewPermissonDeniedException("포인트가 부족하여 가입이 불가능 합니다.");
		}
		user.setPoint(user.getPoint() - crew.getCrewCost());

		UserCrewJoinEntity ucjEntity = UserCrewJoinEntity.builder().build();
		ucjEntity.setCrewEntity(crew);
		ucjEntity.setUserEntity(user);

		ucJoinRepo.save(ucjEntity);

		UserDto userDto = UserDto.of(ucjEntity.getUserEntity());
		CrewDto crewDto = CrewDto.of(ucjEntity.getCrewEntity());
		CrewUserDto crewUserDto = new CrewUserDto(crewDto, userDto, ucjEntity.getCrewUserRegTime());
		return crewUserDto;
	}

	@Transactional
	@Override
	public RunRecordResultDto insertUserRunRecordAsCrew(Long userSeq, Long crewId, RunRecordDto runRecordDto, MultipartFile imgFile) throws Exception {
		// TODO Auto-generated method stub

		 UserEntity userEntity =  userRepo.findById(userSeq).orElseThrow(()->new NotFoundUserException("러닝 완료 중, 유저를 특정할 수 없습니다."));
		CrewEntity crewEntity =  crewManagerRepo.findById(crewId).orElseThrow(()->new CrewNotFoundException("러닝 완료 중 , 크루를 특정할 수 없습니다."));
		
		// 1.크루 런레코드 테이블에 저장
		// runRecordRepo
		RunRecordEntity runRecord = RunRecordEntity.builder()
		.runRecordStartTime(runRecordDto.getRunRecordStartTime())
		.runRecordEndTime(runRecordDto.getRunRecordEndTime())
		.runRecordRunningTime(null)
		.runRecordRunningDistance(runRecordDto.getRunRecordRunningDistance())
		.runRecordAvgSpeed(runRecordDto.getRunRecordRunningAvgSpeed())
		.runRecordCalorie(runRecordDto.getRunRecordRunningCalorie())
		.runRecordLat(runRecordDto.getRunRecordRunningLat())
		.runRecordLng(runRecordDto.getRunRecordRunningLng())
		.runRecordRegTime(LocalDateTime.now()).build();
		//기본 속성들 다 넣어주고
		
		ImageFileDto imageDto =  s3FileService.upload(imgFile, ImageDirectory.RUN_RECORD.getPath());

		ImageFileEntity image = ImageFileEntity.builder()
				.imgOriginalName(imageDto.getImgOriginalName())
				.imgSavedName(imageDto.getImgSavedName())
				.imgSavedPath(imageDto.getImgSavedPath())
				.build();
		imageRepo.save(image); //이미지 저장하고
		//
		runRecordRepo.save(runRecord); //퍼시스턴스 영역에 등록
		//등록한 후에 관계 설정
		runRecord.setUserEntity(userEntity);
		runRecord.setCrewEntity(crewEntity);
		
		
		
		
		//.runRecordCompleteYN()
		//		.build();
		// 2.크루 런레코드 누적 테이블을 갱신하고.

		CrewTotalRecordEntity  userCrewTotalEntity =null;
		try {
			
		}catch(Exception e) {
			log.warn("크루-유저 누적 기록 뭔가 이상한다. 무결성 침해가 우려된다.");
		}
		
		//베타락을 얻어온다.
		 userCrewTotalEntity = userCrewTotalRunRepo.findWithLockByUserAndCrew(userEntity,crewEntity);
		if(userCrewTotalEntity== null) {
			userCrewTotalEntity.setCrewEntity(crewEntity);
			userCrewTotalEntity.setUserEntity(userEntity);
		}
		userCrewTotalEntity.setTotalRecordRegTime(LocalDateTime.now());
		userCrewTotalEntity.setTotalLongestDistance(  Math.max(userCrewTotalEntity.getTotalLongestDistance() ,runRecordDto.getRunRecordRunningDistance()));
		userCrewTotalEntity.setTotalLongestTime(Math.max(userCrewTotalEntity.getTotalLongestTime(), runRecordDto.getRunRecordRunningTime()));
		userCrewTotalEntity.setTotalTime(userCrewTotalEntity.getTotalTime() + runRecordDto.getRunRecordRunningTime());
		userCrewTotalEntity.setTotalDistance(userCrewTotalEntity.getTotalDistance() + runRecordDto.getRunRecordRunningDistance());
		userCrewTotalEntity.setTotalCalorie(userCrewTotalEntity.getTotalCalorie() + runRecord.getRunRecordCalorie());
		userCrewTotalEntity.setTotalAvgSpeed((Double)userCrewTotalEntity.getTotalDistance().doubleValue() / (userCrewTotalEntity.getTotalTime() == 0 ? Long.MAX_VALUE : userCrewTotalEntity.getTotalTime()));
		
		CrewTotalRecordDto userCrewDto = CrewTotalRecordDto.of(userCrewTotalEntity);
		
		ArrayList<AchievementDto> newCompleteDtoList =   getNewCompleteAchiveMent();
		// 3.업적 로직 계산

		return new RunRecordResultDto(RunRecordDto.of(runRecord),newCompleteDtoList);
	}

	private ArrayList<AchievementDto> getNewCompleteAchiveMent() {
		
		ArrayList<AchievementDto> newCompleteDtoList = new ArrayList<>();
		
		return newCompleteDtoList;
	}

	
}
