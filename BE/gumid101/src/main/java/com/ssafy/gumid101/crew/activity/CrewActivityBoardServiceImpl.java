package com.ssafy.gumid101.crew.activity;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.aws.S3FileService;
import com.ssafy.gumid101.crew.manager.CrewManagerRepository;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.customexception.ThirdPartyException;
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
import com.ssafy.gumid101.res.CrewBoardRes;
import com.ssafy.gumid101.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CrewActivityBoardServiceImpl implements CrewActivityBoardService {

	private final UserRepository userRepo;
	private final CrewManagerRepository crewRepo;
	private final CrewActivityBoardRepository boardRepo;
	private final S3FileService s3FileService;
	private final ImageFileRepository imageRepo;

	@Override
	public CrewBoardFileDto writeBoard(UserDto writerDto, MultipartFile image, CrewBoardDto crewBoardDto, Long crewSeq)
			throws Exception {

		UserEntity writerEntity = userRepo.findById(writerDto.getUserSeq())
				.orElseThrow(() -> new NotFoundUserException("작성자 정보가 올바르지 않습니다."));
		CrewEntity crewEntity = crewRepo.findById(crewSeq)
				.orElseThrow(() -> new NotFoundUserException("크루 정보가 올바르지 않습니다."));

		CrewBoardEntity crewBoardEntity = CrewBoardEntity.of(crewBoardDto);

		ImageFileEntity imageEntity = null;
		ImageFileDto savedFileDto = null;
		if (image != null && !image.isEmpty()) {
			try {
				savedFileDto = s3FileService.upload(image, ImageDirectory.CREW_BOARD.getPath());
				// 이미지쪽 세이브
				imageEntity = ImageFileEntity.builder().imgOriginalName(savedFileDto.getImgOriginalName())
						.imgSavedName(savedFileDto.getImgSavedName()).imgSavedPath(savedFileDto.getImgSavedPath())
						.build();
				imageRepo.save(imageEntity);
			} catch (Exception e) {
				throw new ThirdPartyException(e.getMessage()) ;
			}
		}

		crewBoardEntity.setUserEntity(writerEntity);
		crewBoardEntity.setCrewEntity(crewEntity);
		crewBoardEntity.setImgFile(imageEntity);

		boardRepo.save(crewBoardEntity);
		return CrewBoardFileDto.builder() //
				.crewBoardDto(CrewBoardRes.of(crewBoardEntity)) //
				.imageFileDto(ImageFileDto.of(crewBoardEntity.getImgFile())) //
				.build(); //
				
	}

	@Override
	public List<CrewBoardFileDto> getCrewBoards(Long crewSeq, Integer size, Long maxCrewBoardSeq) throws Exception {

		Sort sort = Sort.by(Sort.Direction.DESC, "crewBoardRegTime").and(Sort.by(Sort.Direction.DESC, "crewBoardSeq"));

		if(size == null || size == 0) {
			size = Integer.MAX_VALUE;
		}
		Pageable pageable = PageRequest.of(0, size, sort);

		if (maxCrewBoardSeq == null || maxCrewBoardSeq == 0L) {
			maxCrewBoardSeq = Long.MAX_VALUE;
		}

		return boardRepo
				.findByCrewEntityAndCrewBoardSeqLessThan(crewRepo.findById(crewSeq).get(), maxCrewBoardSeq, pageable) //
				.stream().map((entity) -> {
					CrewBoardRes crewBoardRes = CrewBoardRes.builder().crewBoardContent(entity.getCrewBoardContent())
							.crewBoardSeq(entity.getCrewBoardSeq()).crewBoardRegTime(entity.getCrewBoardRegTime())
							.crewName(entity.getCrewEntity().getCrewName())
							.userNickName(entity.getUserEntity().getNickName())
							.userSeq(entity.getUserEntity().getUserSeq()).build();
					CrewBoardFileDto cbf = CrewBoardFileDto.builder().crewBoardDto(crewBoardRes)
							.imageFileDto(ImageFileDto.of(entity.getImgFile())).build();

					return cbf;
				}).collect(Collectors.toList());

	}

	@Override
	public boolean deleteCrewBoard(Long boardSeq) throws Exception {

		try {
			boardRepo.deleteById(boardSeq);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
