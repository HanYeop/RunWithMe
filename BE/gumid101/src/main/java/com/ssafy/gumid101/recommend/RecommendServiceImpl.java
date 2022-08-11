package com.ssafy.gumid101.recommend;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.aws.S3FileService;
import com.ssafy.gumid101.crew.RunRecordRepository;
import com.ssafy.gumid101.customexception.CustomException;
import com.ssafy.gumid101.customexception.DuplicateException;
import com.ssafy.gumid101.customexception.IllegalParameterException;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.dto.ImageFileDto;
import com.ssafy.gumid101.dto.LatLngParamsDto;
import com.ssafy.gumid101.dto.RunRecordDto;
import com.ssafy.gumid101.dto.TrackBoardDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.entity.ImageFileEntity;
import com.ssafy.gumid101.entity.RunRecordEntity;
import com.ssafy.gumid101.entity.TrackBoardEntity;
import com.ssafy.gumid101.imgfile.ImageDirectory;
import com.ssafy.gumid101.imgfile.ImageFileRepository;
import com.ssafy.gumid101.res.TrackBoardFileDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService{
	
	private final RecommendRepository recommendRepo;
	private final RunRecordRepository runRecoRepo;
	private final ImageFileRepository imageRepo;
	private final S3FileService s3Serv;
	
	@Override
	public List<TrackBoardFileDto> getTrackBoard(LatLngParamsDto params) throws Exception{
		List<TrackBoardFileDto> trackBoardFileDtoList = null;
		try { 
			trackBoardFileDtoList = recommendRepo.findByRunRecordEntity_RunRecordLatBetweenAndRunRecordEntity_RunRecordLngBetween
					(params.getLowerLat(), params.getUpperLat(), params.getLeftLng(), params.getRightLng())
					.stream()
					.map((trackBoardEntity) -> {
						return TrackBoardFileDto.of(trackBoardEntity);
//								.builder()
//								.trackBoardDto(TrackBoardDto.of(trackBoardEntity))
//								.runRecordDto(RunRecordDto.of(trackBoardEntity.getRunRecordEntity()))
//								.userDto(UserDto.builder()
//										.userSeq(trackBoardEntity.getRunRecordEntity().getUserEntity().getUserSeq())
//										.nickName(trackBoardEntity.getRunRecordEntity().getUserEntity().getNickName())
//										.build())
//								.imageFileDto(trackBoardEntity.getRunRecordEntity().getImageFile() == null ? ImageFileDto.getNotExist() : ImageFileDto.of(trackBoardEntity.getRunRecordEntity().getImageFile()))
//								.build();
					}).collect(Collectors.toList());
		} catch (Exception e) {
			throw new CustomException("기록 불러오기에 실패했습니다.");
		}
		return trackBoardFileDtoList;
	}
	
	@Transactional
	@Override
	public TrackBoardFileDto writeTrackBoard(Long userSeq, TrackBoardDto trackBoardDtoInput, MultipartFile imgFile) throws Exception{

		RunRecordEntity runRecordEntity = runRecoRepo.findById(trackBoardDtoInput.getRunRecordSeq())
				.orElseThrow(() -> new NotFoundUserException("해당 기록을 찾을 수 없습니다."));
		
		if (userSeq == null || userSeq != runRecordEntity.getUserEntity().getUserSeq()) {
			throw new NotFoundUserException("본인의 기록만 업로드할 수 있습니다.");
		}
		if (recommendRepo.findByRunRecordEntity(runRecordEntity).isPresent()) {
			throw new DuplicateException("이미 등록한 기록입니다.");
		}
		
		if (trackBoardDtoInput.getHardPoint() != null && (trackBoardDtoInput.getHardPoint() < 1 || 5 <= trackBoardDtoInput.getHardPoint())) {
			throw new IllegalParameterException("난이도 별점은 기록하지 않거나, 1점 ~ 5점이여야합니다.");
		}
		if (trackBoardDtoInput.getEnvironmentPoint() != null && (trackBoardDtoInput.getEnvironmentPoint() < 1 || 5 < trackBoardDtoInput.getEnvironmentPoint())) {
			throw new IllegalParameterException("주변 환경 별점은 기록하지 않거나, 1점 ~ 5점이여야합니다.");
		}
		
		ImageFileDto imageDto = s3Serv.upload(imgFile, ImageDirectory.RECOMMEND.getPath());

		ImageFileEntity imageEntity = ImageFileEntity.builder() //
				.imgOriginalName(imageDto.getImgOriginalName()) //
				.imgSavedName(imageDto.getImgSavedName()). //
				imgSavedPath(imageDto.getImgSavedPath()). //
				build(); //
		imageRepo.save(imageEntity); // 이미지 저장하고
		
		TrackBoardEntity trackBoardEntity = TrackBoardEntity.builder() //
				.trackBoardImageEntity(imageEntity) //
				.trackBoardContent(trackBoardDtoInput.getContent()) //
				.trackBoardEnviromentPoint(trackBoardDtoInput.getEnvironmentPoint()) //
				.trackBoardHardPoint(trackBoardDtoInput.getHardPoint()) //
				.runRecordEntity(runRecordEntity) //
				.build();

		
		recommendRepo.save(trackBoardEntity);
		return TrackBoardFileDto.of(trackBoardEntity);
	}
	
	@Transactional
	@Override
	public Boolean deleteTrackBoard(Long userSeq, Long trackBoardSeq) throws Exception {
		TrackBoardEntity trackBoardEntity = recommendRepo.findById(trackBoardSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 게시글을 찾을 수 없습니다."));
		if (userSeq == null || userSeq != trackBoardEntity.getRunRecordEntity().getUserEntity().getUserSeq()) {
			throw new IllegalParameterException("본인의 기록만 삭제할 수 있습니다.");
		}
		
		try {
			recommendRepo.deleteById(trackBoardSeq);
		} catch(Exception e) {
			throw new CustomException("삭제에 실패했습니다.");
		}
		return true;
	}
}
