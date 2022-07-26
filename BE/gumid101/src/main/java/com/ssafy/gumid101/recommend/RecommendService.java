package com.ssafy.gumid101.recommend;

import org.springframework.stereotype.Service;

import com.ssafy.gumid101.dto.TrackBoardDto;
import com.ssafy.gumid101.res.TrackBoardFileDto;

public interface RecommendService {

	TrackBoardDto writeTrackBoard(Long userSeq, Long runRecordSeq, Integer hardPoint, Integer envPoint)
			throws Exception;

}
