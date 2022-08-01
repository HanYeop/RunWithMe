package com.ssafy.gumid101.crew.manager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.aws.S3FileService;
import com.ssafy.gumid101.crew.RunRecordRepository;
import com.ssafy.gumid101.crew.UserCrewJoinRepository;
import com.ssafy.gumid101.customexception.CrewAlreadyDistributeException;
import com.ssafy.gumid101.customexception.CrewNotFinishedException;
import com.ssafy.gumid101.customexception.CrewNotFoundException;
import com.ssafy.gumid101.customexception.CrewPermissonDeniedException;
import com.ssafy.gumid101.customexception.IllegalParameterException;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.RecruitmentParamsDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.ImageFileEntity;
import com.ssafy.gumid101.entity.RunRecordEntity;
import com.ssafy.gumid101.entity.UserCrewJoinEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.imgfile.ImageDirectory;
import com.ssafy.gumid101.imgfile.ImageFileRepository;
import com.ssafy.gumid101.res.CrewFileDto;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewManagerServiceImpl implements CrewManagerService {

	private final UserRepository userRepo;
	private final CrewManagerRepository crewManagerRepo;
	private final UserCrewJoinRepository userCrewJoinRepo;
	private final S3FileService s3FileService;
	private final ImageFileRepository imageRepo;
	private final RunRecordRepository runRepo;

	@Override
	public Boolean isUserCrewMember(Long userSeq, Long crewSeq) throws Exception {
	    Optional<UserCrewJoinEntity> ucje = null;
	    ucje = userCrewJoinRepo.findByUserEntity_UserSeqAndCrewEntity_CrewSeq(userSeq, crewSeq);
	    if (ucje == null) {
	        return false;
	    }
	    try {
	        ucje.get();
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	@Transactional
	@Override
	public List<?> getMyCurrentCrew(Long userSeq) throws Exception {
		// new jpabook.jpashop.repository.order.simplequery.
		// OrderSimpleQueryDto(o.id, m.name, o.status, o.orderDate, d.address)

		UserEntity user = userRepo.findById(userSeq).orElseThrow(() -> {
			return new NotFoundUserException("나의 현재 진행중 크루를 찾는 중, 유저를 특정할 수 없습니다.");
		});
	
		
		List<CrewEntity> crews = crewManagerRepo.findByUserSeqActive(user, LocalDateTime.now());
		crews = crewManagerRepo.selectCountCrewUser(crews);
		
		List<CrewFileDto> crewList = crews.stream().map((entity) -> {
			UserDto userDto = UserDto.of(entity.getManagerEntity());
			ImageFileDto imgDto = ImageFileDto.of(entity.getImageFile());
			CrewDto crewDto = CrewDto.of(entity, userDto.getNickName(), userDto.getUserSeq());
			crewDto.setCrewMemberCount(entity.getUserCrewJoinEntitys().size());
			
			if (imgDto == null) {
				imgDto = ImageFileDto.getNotExist();
			}

			return new CrewFileDto(crewDto, imgDto);

		}).collect(Collectors.toList());

		return crewList;
	}

	@Transactional
	@Override
	public CrewFileDto createCrew(MultipartFile image, CrewDto crewDto, UserDto manager) throws Exception {

		UserEntity managerEntity = userRepo.findById(manager.getUserSeq())
				.orElseThrow(() -> new NotFoundUserException("유저 정보가 올바르지 않습니다."));

		ImageFileDto savedFileDto = null;
		CrewEntity crewEntity;
		
		if (crewDto.getCrewName() == null || crewDto.getCrewGoalAmount() == null || crewDto.getCrewGoalType() == null
				|| crewDto.getCrewGoalDays() == null || crewDto.getCrewDateStart() == null
				|| crewDto.getCrewDateEnd() == null || crewDto.getCrewTimeStart() == null
				|| crewDto.getCrewTimeEnd() == null || crewDto.getCrewMaxMember() == null) {
			throw new IllegalParameterException("필수 입력 정보에 누락이 있습니다.");
		}
		
		if (crewDto.getCrewDescription() == null) {
			crewDto.setCrewDescription("");
		}
		if (crewDto.getCrewCost() == null) {
			crewDto.setCrewCost(0);
		}
		if (crewDto.getCrewMaxMember() == null || crewDto.getCrewMaxMember() <= 1) {
			throw new IllegalParameterException("최대 인원 설정이 잘못되었습니다..");
		}
		crewDto.setCrewDateEnd(crewDto.getCrewDateEnd().withHour(23).withMinute(59).withSecond(59));
		crewDto.setCrewDateStart(crewDto.getCrewDateStart().withHour(0).withMinute(0).withSecond(0));

		log.info(String.valueOf(crewDto.getCrewDateEnd().isBefore(crewDto.getCrewDateStart())));
		if (crewDto.getCrewDateEnd().isBefore(crewDto.getCrewDateStart())) {
			throw new IllegalParameterException("크루 종료일은 크루 시작일보다 늦어야합니다.");
		}
		if (crewDto.getCrewTimeEnd().isBefore(crewDto.getCrewTimeStart())) {
			throw new IllegalParameterException("크루 일일 활동 종료시간은 시작시간보다 늦어야합니다.");
		}
		if (managerEntity.getPoint() < crewDto.getCrewCost()) {
			throw new IllegalParameterException("생성을 위한 포인트가 부족합니다.");
		}
		try {
			crewEntity = CrewEntity.builder().crewName(crewDto.getCrewName()) //
					.crewDescription(crewDto.getCrewDescription()) //
					.crewGoalDays(crewDto.getCrewGoalDays()) //
					.crewGoalType(crewDto.getCrewGoalType()) //
					.crewGoalAmount(crewDto.getCrewGoalAmount()) //
					.crewPassword(crewDto.getCrewPassword()) //
					.crewCost(crewDto.getCrewCost()) //
					.crewMaxMember(crewDto.getCrewMaxMember()) //
					.crewDateStart(crewDto.getCrewDateStart()) //
					.crewDateEnd(crewDto.getCrewDateEnd()) //
					.crewTimeStart(crewDto.getCrewTimeStart()) //
					.crewTimeEnd(crewDto.getCrewTimeEnd()) //
					.crewCheckYn("N") //
					.build();
		} catch (Exception e) {
			throw new IllegalParameterException("크루 생성 과정에서 문제가 발생했습니다.");
		}
		ImageFileEntity imageEntity = null;
		if (image != null) {
			try {
				savedFileDto = s3FileService.upload(image, ImageDirectory.CREW_LOGO.getPath());
				// 이미지쪽 세이브
				imageEntity = ImageFileEntity.builder().imgOriginalName(savedFileDto.getImgOriginalName())
						.imgSavedName(savedFileDto.getImgSavedName()).imgSavedPath(savedFileDto.getImgSavedPath())
						.build();
				imageRepo.save(imageEntity);
				crewEntity.setImageFile(imageEntity);
			} catch (Exception e) {
				throw new Exception("이미지 저장에 실패했습니다.");
			}
		}
		if (savedFileDto == null) {
			savedFileDto = ImageFileDto.getNotExist();
		}

		managerEntity.setPoint(manager.getPoint() - crewDto.getCrewCost());
//		userRepo.save(managerEntity);
		crewManagerRepo.save(crewEntity);
		crewEntity.setManagerEntity(managerEntity);

		UserCrewJoinEntity userCrewJoinEntity = UserCrewJoinEntity.builder().crewEntity(crewEntity)
				.userEntity(managerEntity).build();

		userCrewJoinRepo.save(userCrewJoinEntity);
		CrewDto createdDto = CrewDto.of(crewEntity, managerEntity.getNickName(), managerEntity.getUserSeq());
		createdDto.setCrewMemberCount(1);

		return new CrewFileDto(createdDto, savedFileDto);
	}

	/**
	 * return 0 실패 , 0!= 성공
	 */
	@Transactional
	@Override
	public int deleteCrew(long crewSeq, long userSeq) throws Exception {

		UserEntity user = userRepo.findById(userSeq)
				.orElseThrow(() -> new UsernameNotFoundException("크루 삭제중,유저를 특정할 수 없습니다."));
		CrewEntity crew = crewManagerRepo.findById(crewSeq)
				.orElseThrow(() -> new CrewNotFoundException("크루 삭제중,크루를 특정할 수 없습니다."));

		if (crew.getManagerEntity().getUserSeq().longValue() == userSeq) {
			if (LocalDateTime.now().isAfter(crew.getCrewDateStart())) {

				int refundcount = userCrewJoinRepo.pointRefunds(crew, crew.getCrewCost());
				//
				int deletedJoin = userCrewJoinRepo.deleteAllBycrewSeq(crew);
				//
				crewManagerRepo.delete(crew);
				log.info("{}로 부터의 -환급 갯수 :{}, 탈퇴 갯수{}", crew.getCrewSeq(), refundcount, deletedJoin);

			} else {
				throw new CrewPermissonDeniedException("이미 시작한 크루는 삭제할 수 없습니다.");
			}
		} else {
			throw new CrewPermissonDeniedException("크루장이 아니면 크루를 삭제할 수 없습니다.");
		}

		return 1;
	}

	@Transactional
	@Override
	public int exitCrew(long crewSeq, Long userSeq) throws Exception {

		UserEntity user = userRepo.findById(userSeq)
				.orElseThrow(() -> new UsernameNotFoundException("크루 탈퇴 중,유저를 특정할 수 없습니다."));
		CrewEntity crew = crewManagerRepo.findById(crewSeq)
				.orElseThrow(() -> new CrewNotFoundException("크루 탈퇴 중,크루를 특정할 수 없습니다."));

		int result = 0;

		if (LocalDateTime.now().isAfter(crew.getCrewDateStart())) {

			user.setPoint(user.getPoint() + crew.getCrewCost()); // 탈퇴 포인트 환급
			result = userCrewJoinRepo.deleteByUserAndCrew(user, crew);// 유저와 크루 참가 관계 삭제

		} else {
			throw new CrewPermissonDeniedException("이미 시작한 크루는 탈퇴할 수 없습니다.");
		}

		if (result != 1) {
			log.error("해당 오류가 났다는 것은 데이터 무결성이 깨졌다는 것을 의미한다. result = {}", result);
		}
		return result;
	}

	@Override
	public List<CrewFileDto> crewSearcheByRecruitmentParams(RecruitmentParamsDto paramsDto) throws Exception {

		List<CrewFileDto> crewList = crewManagerRepo.crewSearcheByRecruitmentParams(paramsDto);

		return crewList;
	}

	@Transactional
	@Override
	public CrewFileDto getCrewDetail(Long crewId) throws Exception {

		CrewEntity crewEntity = crewManagerRepo.findById(crewId)
				.orElseThrow(() -> new CrewNotFoundException("크루 상세 조회 중 , 크루를 특정할 수 없습니다."));
		CrewDto crewDto = CrewDto.of(crewEntity, crewEntity.getManagerEntity().getNickName(),
				crewEntity.getManagerEntity().getUserSeq());
		
		crewDto.setCrewMemberCount(userCrewJoinRepo.findCountCrewUser(crewDto.getCrewSeq()));
		
		
		ImageFileDto imageDto = ImageFileDto.of(crewEntity.getImageFile());
		CrewFileDto crewFileDto = new CrewFileDto(crewDto, imageDto);

		return crewFileDto;
	}

	@Transactional
    @Override
    public Boolean crewFinishPoint(Long crewSeq) throws Exception {
        CrewEntity crewEntity = crewManagerRepo.findById(crewSeq)
                .orElseThrow(() -> new CrewNotFoundException("크루 상세 조회 중 , 크루를 특정할 수 없습니다."));
        if (crewEntity.getCrewCheckYn() != null && crewEntity.getCrewCheckYn().equals("Y")) {
            throw new CrewAlreadyDistributeException("이미 정산이 완료된 크루입니다.");
        }
        if (crewEntity.getCrewDateEnd().isAfter(LocalDateTime.now())) {
            throw new CrewNotFinishedException("종료기간 이전인 크루입니다.");
        }
        // 연습크루이면
        // 연습크루는 정산 대상이 아닙니다. 라는 오류 뱉기
        
        List<UserCrewJoinEntity> userCrewList = userCrewJoinRepo.findAllByCrewEntity(crewEntity);
        // 참가비가 0이거나 크루원 수가 0인 경우는 정산할 필요 없다.
        if (crewEntity.getCrewCost() == null || crewEntity.getCrewCost() <= 0 || userCrewList.size() == 0) {
            crewEntity.setCrewCheckYn("Y");
            return true;
        }
        
        Map<Long, Long> userSucceedDays = new HashMap<>(); 
        
        // 크루 전체의 기록을 가져온다.
        List<RunRecordEntity> crewRunRecords = runRepo.findByCrewEntityAndRunRecordCompleteYNOrderByRunRecordStartTime(crewEntity, "Y");

        // 전체 참가비 합계
        Long totalPoint = (long) userCrewList.size() * crewEntity.getCrewCost();
        long totalSucceedDay = 0;
        // 시작과 끝 간격을 1주일로 함
        LocalDateTime weeksEnd = crewEntity.getCrewDateStart().plusDays(6).plusHours(23).plusMinutes(59).plusSeconds(59);
        int idx = 0;
        while (idx < crewRunRecords.size() && !weeksEnd.isAfter(crewEntity.getCrewDateEnd())) {
        	Map<Long, Long> weekSucceedDays = new HashMap<>(); 
        	while(idx < crewRunRecords.size() && !crewRunRecords.get(idx).getRunRecordStartTime().isAfter(weeksEnd)) {
        		long userSeq = crewRunRecords.get(idx).getUserEntity().getUserSeq();
        		if (!weekSucceedDays.containsKey(userSeq)) {
        			weekSucceedDays.put(userSeq, 0L);
        		}
        		if (weekSucceedDays.get(userSeq) < (long) crewEntity.getCrewGoalDays()) {
        			weekSucceedDays.put(userSeq, weekSucceedDays.get(userSeq) + 1);
        			if (!userSucceedDays.containsKey(userSeq)) {
        				userSucceedDays.put(userSeq, 0L);
        			}
        			userSucceedDays.put(userSeq, userSucceedDays.get(userSeq) + 1);
        			totalSucceedDay++;
        		}
        		idx++;
        	}
            weeksEnd = weeksEnd.plusDays(7);
        }

        // 크루원 중 그 누구도 하루도 못 한 경우는 정산해주지 않는다.
        if (totalSucceedDay <= 0) {
            crewEntity.setCrewCheckYn("Y");
            return true;
        }

        for (int i = 0; i < userCrewList.size(); i++) {
        	if (userSucceedDays.containsKey(userCrewList.get(i).getUserEntity().getUserSeq())) {
        		// 주어진 조건 내에서 계산결과는 Integer범위에서 안 벗어남. (심지어 괄호 내부계산은 long형이다.) 
        		userCrewList.get(i).getUserEntity().setPoint( (int) (userCrewList.get(i).getUserEntity().getPoint() + totalPoint * userSucceedDays.get(userCrewList.get(i).getUserEntity().getUserSeq()) / totalSucceedDay) );
        	}
        }
        crewEntity.setCrewCheckYn("Y");
        return true;
    }
	
	@Override
	public List<Long> getFinishAndNonDistributeCrews() throws Exception{
		return crewManagerRepo.findByCrewCheckYnAndCrewDateEndBefore("N", LocalDateTime.now()).stream().map((entity) -> entity.getCrewSeq()).collect(Collectors.toList());
	}

}
