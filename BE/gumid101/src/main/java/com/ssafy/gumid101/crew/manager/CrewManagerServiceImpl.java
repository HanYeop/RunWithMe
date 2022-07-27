package com.ssafy.gumid101.crew.manager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.aws.S3FileService;
import com.ssafy.gumid101.crew.UserCrewJoinRepository;
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
	private final UserCrewJoinRepository userCrewJoinRepository;
	private final S3FileService s3FileService;
	private final ImageFileRepository imageRepo;

	@Override
	public List<?> getMyCurrentCruew(Long userSeq) throws Exception {
		// new jpabook.jpashop.repository.order.simplequery.
		// OrderSimpleQueryDto(o.id, m.name, o.status, o.orderDate, d.address)

		UserEntity user = userRepo.findById(userSeq).orElseThrow(() -> {
			return new NotFoundUserException("나의 현재 진행중 크루를 찾는 중, 유저를 특정할 수 없습니다.");
		});
		List<CrewEntity> crews = crewManagerRepo.findByUserSeqActive(user, LocalDateTime.now());

		List<CrewFileDto> crewList = crews.stream().map((entity) -> {
			UserDto userDto = UserDto.of(entity.getManagerEntity());
			ImageFileDto imgDto = ImageFileDto.of(entity.getImageFile());
			CrewDto crewDto = CrewDto.of(entity,userDto.getNickName(),userDto.getUserSeq());
			if (imgDto == null) {
				imgDto = ImageFileDto.getNotExist();
			}
			

			return new CrewFileDto(crewDto,imgDto);

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

		userCrewJoinRepository.save(userCrewJoinEntity);

		return new CrewFileDto(CrewDto.of(crewEntity, managerEntity.getNickName(), managerEntity.getUserSeq()), savedFileDto);
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
				int refundcount = userCrewJoinRepository.pointRefunds(crew, crew.getCrewCost());
				int deletedJoin = userCrewJoinRepository.deleteAllBycrewSeq(crew);
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
			result = userCrewJoinRepository.deleteByUserAndCrew(user, crew);// 유저와 크루 참가 관계 삭제

		} else {
			throw new CrewPermissonDeniedException("이미 시작한 크루는 탈퇴할 수 없습니다.");
		}

		if (result != 1) {
			log.error("해당 오류가 났다는 것은 데이터 무결성이 깨졌다는 것을 의미한다. result = {}", result);
		}
		return result;
	}

	@Override
	public List<CrewDto> crewSearcheByRecruitmentParams(RecruitmentParamsDto paramsDto) throws Exception {

		List<CrewDto> crewList = crewManagerRepo.crewSearcheByRecruitmentParams(paramsDto);

		return null;
	}

}
