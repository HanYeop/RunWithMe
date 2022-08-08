package com.ssafy.gumid101.recommend;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.gumid101.customexception.IllegalParameterException;
import com.ssafy.gumid101.dto.CrewDto;
import com.ssafy.gumid101.dto.LatLngParamsDto;
import com.ssafy.gumid101.dto.TrackBoardDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.redis.RedisService;
import com.ssafy.gumid101.res.ResponseFrame;
import com.ssafy.gumid101.res.TrackBoardFileDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
@Api(tags = "장소 추천 게시판")
public class RecommendRestController {
	private final RecommendService recommendService;
	private final ObjectMapper objectMapper;
	private final RedisService redisServ;

	/**
	 * 토큰으로 부터 유저 DTO 로드
	 * 
	 * @return
	 */
	private UserDto loadUserFromToken() {
		Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto tokenUser = (UserDto) autentication.getPrincipal();
		return tokenUser;
	}
	
	/**
	 * lat : 위도 (상하)
	 * lng : 경도 (좌우)
	 * @param latlngParams
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "장소 추천 게시판에서 위/경도 범위 내의 기록을 검색함.")
	@GetMapping("/boards")
	public ResponseEntity<?> getRecommend(@ModelAttribute LatLngParamsDto latlngParams) throws Exception{
		List<TrackBoardFileDto> trackBoardFileDtoList = recommendService.getTrackBoard(latlngParams);
		return new ResponseEntity<>(new ResponseFrame<>(true, trackBoardFileDtoList, trackBoardFileDtoList.size(), "추천 게시판 글 조회에 성공했습니다."), HttpStatus.OK);
	}
	
	@ApiOperation(value = "장소 추천 게시판에 자신의 기록을 등록함 (난이도, 주변환경 별점은 없거나 0 ~ 5의 정수)")
	@PostMapping(value = "/board")
	public ResponseEntity<?> writeRecommend(
			@RequestPart(name = "trackBoardDto", required = true) String trackBoardDtoInputString,
			@RequestPart(required = true) MultipartFile imgFile) throws Exception{
		TrackBoardDto trackBoardDtoInput = objectMapper.readValue(trackBoardDtoInputString, TrackBoardDto.class);
		if (trackBoardDtoInput.getRunRecordSeq() == null) {
			throw new IllegalParameterException("기록 번호가 없습니다.");
		}
		UserDto userDto = loadUserFromToken();
		redisServ.getIsUseable(trackBoardDtoInput.getRunRecordSeq() + "recommend", 10);
		
		TrackBoardFileDto trackBoardFileDto = recommendService.writeTrackBoard(userDto.getUserSeq(), trackBoardDtoInput, imgFile);
		
		return new ResponseEntity<>(new ResponseFrame<>(true, trackBoardFileDto, 1, "경로 추천에 성공했습니다."), HttpStatus.OK);
	}
	
	@DeleteMapping("recommend/boards/{trackBoardSeq}")
	public ResponseEntity<?> deleteRecommend(@PathVariable Long trackBoardSeq) throws Exception{
		UserDto userDto = loadUserFromToken();
		
		Boolean deleted = recommendService.deleteTrackBoard(userDto.getUserSeq(), trackBoardSeq);
		
		return new ResponseEntity<>(new ResponseFrame<>(true, deleted, 1, "추천 게시판 글 삭제에 성공했습니다."), HttpStatus.OK);
	}
}
