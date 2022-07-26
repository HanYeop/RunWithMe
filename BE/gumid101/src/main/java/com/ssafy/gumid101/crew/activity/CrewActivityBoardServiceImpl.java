package com.ssafy.gumid101.crew.activity;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.aws.S3FileService;
import com.ssafy.gumid101.crew.manager.CrewManagerRepository;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.dto.CrewBoardDto;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.CrewBoardEntity;
import com.ssafy.gumid101.entity.CrewEntity;
import com.ssafy.gumid101.entity.ImageFileEntity;
import com.ssafy.gumid101.entity.UserEntity;
import com.ssafy.gumid101.imgfile.ImageDirectory;
import com.ssafy.gumid101.imgfile.ImageFileRepository;
import com.ssafy.gumid101.res.CrewBoardFileDto;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CrewActivityBoardServiceImpl implements CrewActivityBoardService{
	
	private final UserRepository userRepo;
	private final CrewManagerRepository crewRepo;
	private final CrewActivityBoardRepository boardRepo;
	private final S3FileService s3FileService;
	private final ImageFileRepository imageRepo;
	
	@Override
	public CrewBoardFileDto writeBoard(UserDto writerDto, MultipartFile image, CrewBoardDto crewBoardDto, Long crewSeq) throws Exception {

		UserEntity writerEntity = userRepo.findById(writerDto.getUserSeq())
				.orElseThrow(() -> new NotFoundUserException("작성자 정보가 올바르지 않습니다."));
		CrewEntity crewEntity = crewRepo.findById(crewSeq)
				.orElseThrow(() -> new NotFoundUserException("크루 정보가 올바르지 않습니다."));
		
		CrewBoardEntity crewBoardEntity = CrewBoardEntity.of(crewBoardDto);
		
		ImageFileEntity imageEntity = null;
		ImageFileDto savedFileDto = null;
		if (image != null) {
			try {
				savedFileDto = s3FileService.upload(image, ImageDirectory.CREW_BOARD.getPath());
				// 이미지쪽 세이브
				imageEntity = ImageFileEntity.builder()
						.imgOriginalName(savedFileDto.getImgOriginalName())
						.imgSavedName(savedFileDto.getImgSavedName())
						.imgSavedPath(savedFileDto.getImgSavedPath())
						.build();
				imageRepo.save(imageEntity);
			} catch (Exception e) {
				throw e;
//				throw new Exception("이미지 저장에 실패했습니다.");
			}
		}
		if (savedFileDto == null) {
			savedFileDto = ImageFileDto.getNotExist();
		}
		
		crewBoardEntity.setUserEntity(writerEntity);
		crewBoardEntity.setCrewEntity(crewEntity);
		crewBoardEntity.setImgFile(imageEntity);
		
		boardRepo.save(crewBoardEntity);
		return new CrewBoardFileDto(CrewBoardDto.of(crewBoardEntity), savedFileDto);
	}
	
	
	@Override
	public List<CrewBoardFileDto> getCrewBoards(Long crewSeq, Integer size, Long offset,Long crewBoardSeq) throws Exception {
		Pageable pageable = PageRequest.of(offset.intValue(), size);
		
		if(crewBoardSeq == null) {
			crewBoardSeq = Long.MAX_VALUE;
		}
		
		return boardRepo.findByCrewEntityAndCrewBoardSeqLessThan(crewRepo.findById(crewSeq).get(),crewBoardSeq,pageable) //
				.stream() 
				.map((entity) -> { 
			return new CrewBoardFileDto( 
					CrewBoardDto.of(entity), 
					entity.getImgFile() == null ? ImageFileDto.getNotExist() : ImageFileDto.of(imageRepo.findById(entity.getImgFile().getImgSeq()).get()) //
					); 
		}).collect(Collectors.toList());
		
		
	}
	
	@Override
	public boolean deleteCrewBoard(Long crewSeq, Long boardSeq) throws Exception {
		CrewBoardEntity boardEntity = boardRepo.findById(boardSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 글 찾을 수 없습니다."));
		if (crewSeq == null || crewSeq != boardEntity.getCrewEntity().getCrewSeq()) {
			throw new NotFoundUserException("크루 정보가 일치하지 않습니다.");
		}
		try {
			boardRepo.deleteById(boardSeq);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
