package com.ssafy.gumid101.recommend;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.gumid101.dto.LatLngParamsDto;
import com.ssafy.gumid101.dto.TrackBoardDto;
import com.ssafy.gumid101.dto.UserDto;
import com.ssafy.gumid101.res.ResponseFrame;
import com.ssafy.gumid101.res.TrackBoardFileDto;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendRestController {
	private final RecommendService recommendService;

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
	 */
	@ApiOperation(value = "장소 추천 게시판에서 위/경도 범위 내의 기록을 검색함.")
	@GetMapping("/boards")
	public ResponseEntity<?> getRecommend(@ModelAttribute LatLngParamsDto latlngParams){

		
		HttpStatus httpStatus = HttpStatus.OK;
		
		ResponseFrame<List<TrackBoardFileDto>> responseFrame = new ResponseFrame<>();
		List<TrackBoardFileDto> trackBoardFileDtoList = null;
		try {
			trackBoardFileDtoList = recommendService.getTrackBoard(latlngParams);
		}catch (Exception e) {
			httpStatus = HttpStatus.OK;
			responseFrame.setData(new ArrayList<>());
			responseFrame.setCount(0);
			responseFrame.setSuccess(false);
			responseFrame.setMsg(e.getMessage());
			return new ResponseEntity<>(responseFrame, httpStatus);
		}
		
		if (trackBoardFileDtoList != null) {
			responseFrame.setCount(trackBoardFileDtoList.size());
			responseFrame.setSuccess(true);
			responseFrame.setMsg("추천 게시판 글 조회에 성공했습니다.");
		}
		responseFrame.setData(trackBoardFileDtoList);
		return new ResponseEntity<>(responseFrame, httpStatus);
	}
	
	@ApiOperation(value = "장소 추천 게시판에 자신의 기록을 등록함 (난이도, 주변환경 별점은 없거나 0 ~ 5의 정수)")
	@PostMapping("/board")
	public ResponseEntity<?> writeRecommend(@RequestParam Long run_record_seq, @RequestParam(required = true) Integer hard_point, @RequestParam(required = true) Integer environment_point){
		UserDto userDto = loadUserFromToken();
		
		HttpStatus httpStatus = HttpStatus.OK;
		
		ResponseFrame<TrackBoardDto> responseFrame = new ResponseFrame<>();
		TrackBoardDto trackBoardDto = null;
		try {
			trackBoardDto = recommendService.writeTrackBoard(userDto.getUserSeq(), run_record_seq, hard_point, environment_point);
		}catch (Exception e) {
			httpStatus = HttpStatus.OK;
			responseFrame.setCount(0);
			responseFrame.setSuccess(false);
			responseFrame.setMsg(e.getMessage());
		}
		
		if (trackBoardDto != null) {
			responseFrame.setCount(1);
			responseFrame.setSuccess(true);
			responseFrame.setMsg("추천 게시판 글 등록에 성공했습니다.");
		}
		responseFrame.setData(trackBoardDto);
		return new ResponseEntity<>(responseFrame, httpStatus);
	}
	
	@DeleteMapping("recommend/boards/{trackBoardSeq}")
	public ResponseEntity<?> deleteRecommend(@PathVariable Long trackBoardSeq){
		UserDto userDto = loadUserFromToken();
		
		HttpStatus httpStatus = HttpStatus.OK;
		
		ResponseFrame<Boolean> responseFrame = new ResponseFrame<>();
		Boolean deleted = false;
		try {
			deleted = recommendService.deleteTrackBoard(userDto.getUserSeq(), trackBoardSeq);
		}catch (Exception e) {
			httpStatus = HttpStatus.OK;
			responseFrame.setCount(0);
			responseFrame.setSuccess(false);
			responseFrame.setMsg(e.getMessage());
		}
		
		if (deleted) {
			responseFrame.setCount(1);
			responseFrame.setSuccess(true);
			responseFrame.setMsg("추천 게시판 글 삭제에 성공했습니다.");
		}
		responseFrame.setData(deleted);
		return new ResponseEntity<>(responseFrame, httpStatus);
	}
}
