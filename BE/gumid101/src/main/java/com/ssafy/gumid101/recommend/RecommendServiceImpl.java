package com.ssafy.gumid101.recommend;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ssafy.gumid101.crew.RunRecordRepository;
import com.ssafy.gumid101.customexception.NotFoundUserException;
import com.ssafy.gumid101.dto.TrackBoardDto;
import com.ssafy.gumid101.entity.RunRecordEntity;
import com.ssafy.gumid101.entity.TrackBoardEntity;
import com.ssafy.gumid101.imgfile.ImageFileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService{
	
	private final RecommendRepository recommendRepo;
	private final RunRecordRepository runRecoRepo;
	private final ImageFileRepository imageRepo;
	
	@Transactional
	@Override
	public TrackBoardDto writeTrackBoard(Long userSeq, Long runRecordSeq, Integer hardPoint, Integer envPoint) throws Exception{

		RunRecordEntity runRecordEntity = runRecoRepo.findById(runRecordSeq)
				.orElseThrow(() -> new NotFoundUserException("해당 기록을 찾을 수 없습니다."));
		if (userSeq == null || userSeq != runRecordEntity.getUserEntity().getUserSeq()) {
			throw new Exception("본인의 기록만 업로드할 수 있습니다.");
		}
		if (hardPoint != null && (hardPoint < 0 || 5 < hardPoint)) {
			throw new Exception("난이도 별점은 기록하지 않거나, 0점 ~ 5점이여야합니다.");
		}
		if (envPoint != null && (envPoint < 0 || 5 < envPoint)) {
			throw new Exception("주변 환경 별점은 기록하지 않거나, 0점 ~ 5점이여야합니다.");
		}
		
		TrackBoardEntity trackBoardEntity = TrackBoardEntity.builder()
				.trackBoardEnviromentPoint(envPoint)
				.trackBoardHardPoint(hardPoint)
				.runRecordEntity(runRecordEntity)
				.build();
		try {
			recommendRepo.save(trackBoardEntity);
		} catch(Exception e) {
			throw new Exception("저장에 실패했습니다.");
		}
		return TrackBoardDto.of(trackBoardEntity);
	}
}
