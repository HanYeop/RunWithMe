package com.ssafy.gumid101.recommend;

import java.util.List;

import com.ssafy.gumid101.dto.LatLngParamsDto;
import com.ssafy.gumid101.dto.TrackBoardDto;
import com.ssafy.gumid101.res.TrackBoardFileDto;

public interface RecommendService {

	TrackBoardDto writeTrackBoard(Long userSeq, Long runRecordSeq, Integer hardPoint, Integer envPoint)
			throws Exception;

	List<TrackBoardFileDto> getTrackBoard(LatLngParamsDto params) throws Exception;

	Boolean deleteTrackBoard(Long userSeq, Long runRecordSeq) throws Exception;

}
