package com.ssafy.gumid101.crew;


import static org.junit.jupiter.api.Assertions.assertAll;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.aws.S3FileService;
import com.ssafy.gumid101.customexception.IllegalParameterException;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.ImageFileEntity;
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
public class CrewManagerServiceImpl implements CrewManagerService{

	private final UserRepository userRepo;
	private final CrewManagerRepository crewManagerRepo;
	private final UserCrewJoinRepository userCrewJoinRepository;
	private final S3FileService s3FileService;
	private final ImageFileRepository imageRepo;
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
	
	@Transactional
	@Override
	public CrewFileDto createCrew(MultipartFile image, CrewDto crewDto, UserDto manager) throws Exception {

		UserEntity managerEntity = userRepo.findById(manager.getUserSeq())
				.orElseThrow(() -> new NotFoundUserException("유저 정보가 올바르지 않습니다."));
		
		
		ImageFileDto savedFileDto = null;
		CrewEntity crewEntity;
		if (crewDto.getCrewName() == null ||
				crewDto.getCrewGoalAmount() == null ||
				crewDto.getCrewGoalType() == null ||
				crewDto.getCrewGoalDays() == null ||
				crewDto.getCrewDateStart() == null ||
				crewDto.getCrewDateEnd() == null ||
				crewDto.getCrewTimeStart() == null ||
				crewDto.getCrewTimeEnd() == null ||
				crewDto.getCrewMaxMember() == null) {
			throw new IllegalParameterException("필수 입력 정보에 누락이 있습니다.");
		}
		if (crewDto.getCrewDescription() == null) {
			crewDto.setCrewDescription("");
		}
		if (crewDto.getCrewCost() == null) {
			crewDto.setCrewCost(0);
		}
		if (crewDto.getCrewMaxMember() == null || crewDto.getCrewMaxMember() <= 1 ) {
			throw new IllegalParameterException("최대 인원 설정이 잘못되었습니다..");
		}
		if (crewDto.getCrewDateStart().compareTo(crewDto.getCrewDateStart()) >= 0) {
			throw new IllegalParameterException("크루 종료일은 크루 시작일보다 늦어야합니다.");
		}
		if (crewDto.getCrewTimeStart().compareTo(crewDto.getCrewTimeEnd()) >= 0) {
			throw new IllegalParameterException("크루 일일 활동 종료시간은 시작시간보다 늦어야합니다.");
		}
		if (managerEntity.getPoint() < crewDto.getCrewCost()) {
			throw new IllegalParameterException("생성을 위한 포인트가 부족합니다.");
		}
		try {
			crewEntity = CrewEntity.builder()
					.crewName(crewDto.getCrewName())
					.crewDescription(crewDto.getCrewDescription())
					.crewGoalDays(crewDto.getCrewGoalDays())
					.crewGoalType(crewDto.getCrewGoalType())
					.crewGoalAmount(crewDto.getCrewGoalAmount())
					.crewPassword(crewDto.getCrewPassword())
					.crewCost(crewDto.getCrewCost())
					.crewMaxMember(crewDto.getCrewMaxMember())
					.crewDateStart(crewDto.getCrewDateStart())
					.crewDateEnd(crewDto.getCrewDateEnd())
					.crewTimeStart(crewDto.getCrewTimeStart())
					.crewTimeEnd(crewDto.getCrewTimeEnd())
					.managerEntity(managerEntity)
					.build();
		}catch (Exception e) {
			throw new IllegalParameterException("크루 생성 과정에서 문제가 발생했습니다.");
		}
		ImageFileEntity imageEntity = null;
		if (image != null) {
			try {
				savedFileDto = s3FileService.upload(image, ImageDirectory.CREW_LOGO.getPath());
				// 이미지쪽 세이브
				imageEntity = ImageFileEntity.builder()
						.imgOriginalName(savedFileDto.getImgOriginalName())
						.imgSavedName(savedFileDto.getImgSavedName())
						.imgSavedPath(savedFileDto.getImgSavedPath())
						.build();
				imageRepo.save(imageEntity);
				crewEntity.setImageFile(imageEntity);
			} catch (Exception e) {
				throw new Exception("이미지 저장에 실패했습니다.");
			}
		}
		
		
		UserCrewJoinEntity userCrewJoinEntity = UserCrewJoinEntity.builder()
				.crewEntity(crewEntity)
				.userEntity(managerEntity)
				.build();
		
		userCrewJoinRepository.save(userCrewJoinEntity);
		
		managerEntity.setPoint(manager.getPoint() - crewDto.getCrewCost());
		userRepo.save(managerEntity);
		
		return new CrewFileDto(crewDto.of(crewEntity), savedFileDto);
	}
	
	
>>>>>>> BE/gumid101/src/main/java/com/ssafy/gumid101/crew/CrewManagerServiceImpl.java
}
