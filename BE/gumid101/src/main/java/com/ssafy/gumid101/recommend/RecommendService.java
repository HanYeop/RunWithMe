package com.ssafy.gumid101.recommend;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.gumid101.dto.LatLngParamsDto;
import com.ssafy.gumid101.dto.TrackBoardDto;
import com.ssafy.gumid101.res.TrackBoardFileDto;

public interface RecommendService {

	TrackBoardFileDto writeTrackBoard(Long userSeq, TrackBoardDto trackBoardDtoInput, MultipartFile imgFile) throws Exception;

	List<TrackBoardFileDto> getTrackBoard(LatLngParamsDto params) throws Exception;

	Boolean deleteTrackBoard(Long userSeq, Long runRecordSeq) throws Exception;


}
