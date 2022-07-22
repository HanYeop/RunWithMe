package com.ssafy.gumid101.crew;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.aws.S3FileService;
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
	
	@Transactional
	@Override
	public CrewFileDto createCrew(MultipartFile image, CrewDto crewDto, UserDto manager) throws Exception {

		UserEntity managerEntity = userRepo.findById(manager.getUserSeq())
				.orElseThrow(() -> new NotFoundUserException("해당 유저를 찾을 수 없습니다."));
		
		ImageFileDto savedFileDto = null;
		ImageFileEntity imageEntity = null;
		if (image != null) {
			savedFileDto = s3FileService.upload(image, ImageDirectory.CREW_LOGO.getPath());
			// 이미지쪽 세이브
			imageEntity = ImageFileEntity.builder()
					.imgOriginalName(savedFileDto.getImgOriginalName())
					.imgSavedName(savedFileDto.getImgSavedName())
					.imgSavedPath(savedFileDto.getImgSavedPath())
					.build();
			imageRepo.save(imageEntity);
		}
		
		CrewEntity crewEntity = CrewEntity.builder()
				.crewName(crewDto.getCrewName())
				.crewDescription(crewDto.getCrewDescription())
				.crewGoalDays(crewDto.getCrewGoalDays())
				.crewGoalType(crewDto.getCrewGoalType())
				.crewGoalAmount(crewDto.getCrewGoalAmount())
				.crewPassword(crewDto.getCrewPassword())
				.crewCost(crewDto.getCrewCost())
				.crewMaxMember(crewDto.getCrewMaxMember())
				.managerEntity(managerEntity)
				.build();
		crewEntity.setImageFile(imageEntity);
		
		UserCrewJoinEntity userCrewJoinEntity = UserCrewJoinEntity.builder()
				.crewEntity(crewEntity)
				.userEntity(managerEntity)
				.build();
		
		userCrewJoinRepository.save(userCrewJoinEntity);
		
		return new CrewFileDto(crewDto.of(crewEntity), savedFileDto);
	}
	
	
}
