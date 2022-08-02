package com.ssafy.gumid101.recommend;

import java.util.List;

import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

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
import com.ssafy.gumid101.entity.RunRecordEntity;
import com.ssafy.gumid101.entity.TrackBoardEntity;
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
	
	@Override
	public List<TrackBoardFileDto> getTrackBoard(LatLngParamsDto params) throws Exception{
		List<TrackBoardFileDto> trackBoardFileDtoList = null;
		try { 
			trackBoardFileDtoList = recommendRepo.findByRunRecordEntity_RunRecordLatBetweenAndRunRecordEntity_RunRecordLngBetween
					(params.getLowerLat(), params.getUpperLat(), params.getLeftLng(), params.getRightLng())
					.stream()
					.map((trackBoardEntity) -> {
						return TrackBoardFileDto.builder()
								.trackBoardDto(TrackBoardDto.of(trackBoardEntity))
								.runRecordDto(RunRecordDto.of(trackBoardEntity.getRunRecordEntity()))
								.userDto(UserDto.builder()
										.userSeq(trackBoardEntity.getRunRecordEntity().getUserEntity().getUserSeq())
										.nickName(trackBoardEntity.getRunRecordEntity().getUserEntity().getNickName())
										.build())
								.imageFileDto(trackBoardEntity.getRunRecordEntity().getImageFile() == null ? ImageFileDto.getNotExist() : ImageFileDto.of(trackBoardEntity.getRunRecordEntity().getImageFile()))
								.build();
					}).collect(Collectors.toList());
		} catch (Exception e) {
			throw new CustomException("기록 불러오기에 실패했습니다.");
		}
		return trackBoardFileDtoList;
	}
	
	@Transactional
	@Override
	public TrackBoardDto writeTrackBoard(Long userSeq, Long runRecordSeq, Integer hardPoint, Integer envPoint) throws Exception{

		RunRecordEntity runRecordEntity = runRecoRepo.findById(runRecordSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 기록을 찾을 수 없습니다."));
		
		if (userSeq == null || userSeq != runRecordEntity.getUserEntity().getUserSeq()) {
			

			throw new NotFoundUserException("본인의 기록만 업로드할 수 있습니다.");
		}
		
		
		if (recommendRepo.findByRunRecordEntity(runRecordEntity).isPresent()) {
			throw new DuplicateException("이미 등록한 기록입니다.");
		}
		
		if (hardPoint != null && (hardPoint < 1 || 5 <= hardPoint)) {
			throw new IllegalParameterException("난이도 별점은 기록하지 않거나, 1점 ~ 5점이여야합니다.");
		}
		if (envPoint != null && (envPoint < 1 || 5 < envPoint)) {
			throw new IllegalParameterException("주변 환경 별점은 기록하지 않거나, 1점 ~ 5점이여야합니다.");
		}
		
		TrackBoardEntity trackBoardEntity = TrackBoardEntity.builder()
				.trackBoardEnviromentPoint(envPoint)
				.trackBoardHardPoint(hardPoint)
				.runRecordEntity(runRecordEntity)
				.build();

		
		recommendRepo.save(trackBoardEntity);
		return TrackBoardDto.of(trackBoardEntity);
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
